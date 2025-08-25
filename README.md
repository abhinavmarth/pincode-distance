# 📍 Pincode Distance Service

A simple Spring Boot application to calculate the **distance** and **travel duration** between two pincodes using OpenRouteService and OpenStreetMap APIs.

---

## 🚀 Features

- Accepts `fromPincode`, `toPincode`, and optional `country`.
- Calculates:
    - Total distance (in kilometers)
    - Estimated travel duration (in hours)
- Stores results in an H2 in-memory database.
- Avoids recomputation if the same pincode pair is queried again.
- Integrations:
    - 📍 [OpenStreetMap Nominatim](https://nominatim.org/) for geolocation.
    - 🛣️ [OpenRouteService](https://openrouteservice.org/) for route and distance calculation.

---

## 🧠 Why Country Is Important

> ⚠️ For a single pincode, **multiple locations** may exist across the world (e.g., `10001` exists in the USA, and similar codes exist in other countries).

🔹 To avoid inaccurate results or geolocation errors, it is **highly recommended to specify the `country` field** (e.g., `"India"`, `"US"`, etc.).

---

## 📦 API Usage

### POST `/distance`

**Request Body:**

```json
{
  "fromPincode": "560001",
  "toPincode": "110001",
  "country": "India" // optional but highly recommended
}
```

## Response Example:

### json Copy code
```{
  "id": 1,
  "fromPincode": "560001",
  "toPincode": "110001",
  "distance": 2100.54,
  "duration": 26.4,
  "route": "{...full ORS route JSON...}"
}
```

## Database (in-memory)

spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update

## Application
spring.application.name=pincodedistance

## OpenRouteService API Key (register at https://openrouteservice.org)
ors.api.key=YOUR_API_KEY_HERE
> ⚠️ Limitations
🚧 OpenRouteService API
This project uses the free-tier OpenRouteService, which has:

.A maximum distance limit of 6000 kilometers between two locations.

If the fromPincode and toPincode are more than 6000 km apart, an error will be thrown.

## 🧪 Testing

To run the test suite:

./mvnw test

Includes tests for:

Handling of cached pincode distances.

Invalid pincode error handling.

Missing country field cases.

Enforcing the 6000 km distance limit.

## 📊 Entity Relationship Diagram (ERD)

```
+----------------------+
|  PincodeDistance     |
+----------------------+
| id: Long             |
| fromPincode: String  |
| toPincode: String    |
| distance: Double     |
| duration: Double     |
| route: String (JSON) |
+----------------------+
```

# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run


## 📥 Dependencies
Spring Boot

Spring Web

Spring Data JPA

H2 Database

JSON (org.json)

OpenRouteService API

OpenStreetMap Nominatim API

