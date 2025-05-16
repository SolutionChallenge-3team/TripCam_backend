package com.TripCam.gdgoc3.Photo;

import com.TripCam.gdgoc3.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByUser(User user);
}
