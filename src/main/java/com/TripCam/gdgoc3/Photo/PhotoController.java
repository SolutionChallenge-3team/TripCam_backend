package com.TripCam.gdgoc3.Photo;

import com.TripCam.gdgoc3.Photo.DTO.PhotoRequestDTO;
import com.TripCam.gdgoc3.Photo.DTO.PhotoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/photo")
@RequiredArgsConstructor
public class PhotoController {
    private final PhotoService photoService;

    @PostMapping(value = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> createAnalyze(@ModelAttribute PhotoRequestDTO requestDTO) throws IOException {

        MultipartFile image = requestDTO.getImage();
        try {

            Map<String, String> result = photoService.createAnalyze(image.getInputStream());

            Map<String, Object> response = new HashMap<>();

            response.put("locationName", result.get("locationName"));
            response.put("description", result.get("description"));
            response.put("story", result.get("story"));
            response.put("createdAt", LocalDateTime.now());
            // response.put("imageFile", image);


            return ResponseEntity.ok(response);
            // return null;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }

    }


}


