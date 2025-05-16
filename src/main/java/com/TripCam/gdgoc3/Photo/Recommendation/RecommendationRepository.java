package com.TripCam.gdgoc3.Photo.Recommendation;

import com.TripCam.gdgoc3.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    @Query("SELECT r FROM Recommendation r WHERE r.user = :user")
    List<Recommendation> findRecommendationByUser(@Param("user") User user);
}
