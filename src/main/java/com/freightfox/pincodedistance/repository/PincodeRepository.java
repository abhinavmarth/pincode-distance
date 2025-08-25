package com.freightfox.pincodedistance.repository;

import com.freightfox.pincodedistance.model.PincodeDistance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PincodeRepository extends JpaRepository<PincodeDistance, Long> {
    Optional<PincodeDistance> findByFromPincodeAndToPincode(String fromPincode, String toPincode);
}
