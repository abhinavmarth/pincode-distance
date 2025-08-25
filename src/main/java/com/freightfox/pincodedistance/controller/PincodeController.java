package com.freightfox.pincodedistance.controller;

import com.freightfox.pincodedistance.model.DistanceRequest;
import com.freightfox.pincodedistance.model.PincodeDistance;
import com.freightfox.pincodedistance.service.PincodeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PincodeController {

    @Autowired
    private PincodeService pinService;

    @PostMapping("/distance")
    public PincodeDistance distance(@Valid @RequestBody DistanceRequest req) {
        return pinService.distanceBtwPincodes(req);
    }
}
