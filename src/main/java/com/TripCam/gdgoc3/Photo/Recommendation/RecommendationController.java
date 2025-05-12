package com.TripCam.gdgoc3.Photo.Recommendation;

import com.TripCam.gdgoc3.Photo.PhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/photo")
@RequiredArgsConstructor
@Tag(name = "/api/photo")
public class RecommendationController {
    private final PhotoService photoService;

    @PostMapping("/recommend")
    @Operation(summary = "주변 장소 추천")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RecommendationSpaceDTO.class))))
    public ResponseEntity<List<RecommendationSpaceDTO>> recommend(@RequestBody RecommendationRequestDTO response) throws IOException {

        Long photoId = response.getPhotoId();

        try {
            List<RecommendationSpaceDTO> recommendations = photoService.recommendNearby(photoId);
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
