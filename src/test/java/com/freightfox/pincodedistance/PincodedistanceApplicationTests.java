package com.freightfox.pincodedistance;

import com.freightfox.pincodedistance.model.DistanceRequest;
import com.freightfox.pincodedistance.model.PincodeDistance;
import com.freightfox.pincodedistance.repository.PincodeRepository;
import com.freightfox.pincodedistance.service.PincodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PincodedistanceApplicationTests {

    @InjectMocks
    private PincodeService service;

    @Mock
    private PincodeRepository repo;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(service, "orsApiKey", "dummy-key");
    }
    @Test
    void shouldReturnCachedDistanceIfExists() {
        DistanceRequest req = new DistanceRequest("560001", "110001", "India");
        PincodeDistance cached = new PincodeDistance();
        cached.setFromPincode("560001");
        cached.setToPincode("110001");
        cached.setDistance(200.0);
        cached.setDuration(3.5);

        when(repo.findByFromPincodeAndToPincode("560001", "110001")).thenReturn(Optional.of(cached));

        PincodeDistance result = service.distanceBtwPincodes(req);

        assertNotNull(result);
        assertEquals(200.0, result.getDistance());
        assertEquals(3.5, result.getDuration());
        verify(repo, never()).save(any());
    }


    @Test
    void shouldFetchDistanceWhenNotInCache() {
        DistanceRequest req = new DistanceRequest("560001", "110001", "India");
        when(repo.findByFromPincodeAndToPincode(anyString(), anyString())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.distanceBtwPincodes(req));
    }

    @Test
    void shouldThrowExceptionForInvalidPincode() {
        DistanceRequest req = new DistanceRequest("999999", "000000", "India");

        Exception ex = assertThrows(RuntimeException.class, () -> {
            service.distanceBtwPincodes(req);
        });

        assertTrue(ex.getMessage().contains("Pincode not found"));
    }

}
