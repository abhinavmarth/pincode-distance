package com.freightfox.pincodedistance.service;

import com.freightfox.pincodedistance.model.DistanceRequest;
import com.freightfox.pincodedistance.model.PincodeDistance;
import com.freightfox.pincodedistance.repository.PincodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONArray;
import org.json.JSONObject;

@Service
public class PincodeService {

    @Value("${ors.api.key}")
    private String orsApiKey;

    @Autowired
    private PincodeRepository pinRepo;

    private final RestTemplate restTemplate = new RestTemplate();

    public PincodeDistance distanceBtwPincodes(DistanceRequest req) {
        return pinRepo.findByFromPincodeAndToPincode(req.getFromPincode(), req.getToPincode())
                .orElseGet(() -> fetchDetails(req));
    }

    private PincodeDistance fetchDetails(DistanceRequest req) {
        double[] fromCoords = getLatLong(req.getFromPincode(), req.getCountry());
        double[] toCoords = getLatLong(req.getToPincode(), req.getCountry());

        System.out.println("From coords: lon=" + fromCoords[0] + ", lat=" + fromCoords[1]);
        System.out.println("To coords: lon=" + toCoords[0] + ", lat=" + toCoords[1]);

        String orsUrl = "https://api.openrouteservice.org/v2/directions/driving-car";

        JSONObject body = new JSONObject()
                .put("coordinates", new JSONArray()
                        .put(new JSONArray(fromCoords))
                        .put(new JSONArray(toCoords)));

        var headers = new org.springframework.http.HttpHeaders();
        headers.set("Authorization", orsApiKey);
        headers.set("Content-Type", "application/json");

        var entity = new org.springframework.http.HttpEntity<>(body.toString(), headers);
        var response = restTemplate.postForObject(orsUrl, entity, String.class);

        JSONObject json = new JSONObject(response);
        JSONObject summary = json
                .getJSONArray("routes")
                .getJSONObject(0)
                .getJSONObject("summary");

        double distanceKm = summary.getDouble("distance") / 1000.0;
        double durationHr = summary.getDouble("duration") / 3600.0;

        if (distanceKm > 6000) {
            System.out.println("Distance is greater than 6000 km");
        }

        PincodeDistance pd = new PincodeDistance();
        pd.setFromPincode(req.getFromPincode());
        pd.setToPincode(req.getToPincode());
        pd.setDistance(distanceKm);
        pd.setDuration(durationHr);
        pd.setRoute(json.toString());

        pinRepo.save(pd);
        return pd;
    }

    private double[] getLatLong(String pincode, String country) {
        String url = "https://nominatim.openstreetmap.org/search?postalcode=" + pincode + "&format=json&limit=1";
        if (country != null && !country.isBlank()) {
            url += "&country=" + country;
        }

        var headers = new org.springframework.http.HttpHeaders();
        headers.set("User-Agent", "SpringBootApp");

        var entity = new org.springframework.http.HttpEntity<>(headers);
        var response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, String.class);
        JSONArray array = new JSONArray(response.getBody());

        if (array.isEmpty()) {
            throw new RuntimeException("Pincode not found: " + pincode + (country != null ? " in " + country : ""));
        }

        JSONObject loc = array.getJSONObject(0);
        return new double[]{
                loc.getDouble("lon"),
                loc.getDouble("lat")
        };
    }

}
