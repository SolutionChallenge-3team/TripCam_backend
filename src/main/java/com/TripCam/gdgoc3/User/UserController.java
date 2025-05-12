package com.TripCam.gdgoc3.User;

import com.TripCam.gdgoc3.Photo.DTO.PhotoRequestDTO;
import com.TripCam.gdgoc3.Photo.DTO.PhotoResponseDTO;
import com.TripCam.gdgoc3.Photo.Photo;
import com.TripCam.gdgoc3.Photo.PhotoRepository;
import com.TripCam.gdgoc3.Photo.PhotoService;
import com.TripCam.gdgoc3.Photo.Recommendation.RecommendationSpaceDTO;
import com.TripCam.gdgoc3.User.DTO.RecordDetailsResponseDTO;
import com.TripCam.gdgoc3.User.DTO.RecordListResponseDTO;
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

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "/api/user")
public class UserController {
    private final UserService userService;

    // 기록 리스트
    @GetMapping("/{userId}")
    @Operation(summary = "/api/user/{userId}")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RecordListResponseDTO.class))))
    public ResponseEntity<List<RecordListResponseDTO>> getUserPhotos(@PathVariable Long userId) {

        try {
            List<RecordListResponseDTO> photos = userService.getPhotos(userId);
            return ResponseEntity.ok(photos);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    // 기록 상세보기
    @GetMapping("/{userId}/{photoId}")
    @Operation(summary = "/api/user/{userId}/{photoId}")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecordDetailsResponseDTO.class)))
    public ResponseEntity<RecordDetailsResponseDTO> getUserPhotoDetails(@PathVariable Long userId, @PathVariable Long photoId) {

        try {
            RecordDetailsResponseDTO photo = userService.getPhotoDetails(userId, photoId);
            return ResponseEntity.ok(photo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }


}
