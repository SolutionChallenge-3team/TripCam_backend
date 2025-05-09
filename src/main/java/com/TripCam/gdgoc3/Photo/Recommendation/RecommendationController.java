package com.TripCam.gdgoc3.Photo.Recommendation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/photo")
@RequiredArgsConstructor
public class RecommendationController {

    @PostMapping("{id}/recommendation")
    public void recommendation(@PathVariable Long id) {




    }
}
