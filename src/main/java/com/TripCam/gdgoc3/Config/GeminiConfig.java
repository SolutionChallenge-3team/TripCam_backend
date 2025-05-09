package com.TripCam.gdgoc3.Config;


import org.springframework.beans.factory.annotation.Value;

public class GeminiConfig {

    @Value("${GEMINI_API_KEY}")
    private String apikey;
}
