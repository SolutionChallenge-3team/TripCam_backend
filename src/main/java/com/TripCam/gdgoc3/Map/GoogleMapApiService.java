package com.TripCam.gdgoc3.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleMapApiService {
    @Value("${GOOGLE_MAPS_API}")
    private String apiKey;
    public GoogleMapApiInfo getGoogleMapApiInfo() {
        return new GoogleMapApiInfo(apiKey);
    }
}
