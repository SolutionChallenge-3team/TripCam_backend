package com.TripCam.gdgoc3.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/maps")
@RequiredArgsConstructor
public class GoogleMapApiController {

    private final GoogleMapApiService googleMapApiService;

    @GetMapping("/getGoogleMapApiInfo")
    @Operation(summary = "/api/maps/getGoogleMapApiInfo")
    @ApiResponse(responseCode = "200", description = "API 정보 반환 성공")
    public GoogleMapApiInfo getGoogleMapApiInfo() {
        return googleMapApiService.getGoogleMapApiInfo();
    }


}
