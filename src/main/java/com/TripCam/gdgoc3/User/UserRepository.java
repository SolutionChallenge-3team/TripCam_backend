package com.TripCam.gdgoc3.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<com.TripCam.gdgoc3.user.User, Long> {
    Optional<com.TripCam.gdgoc3.user.User> findByUid(String uid);
}
