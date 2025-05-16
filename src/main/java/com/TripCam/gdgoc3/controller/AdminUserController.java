package com.TripCam.gdgoc3.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class AdminUserController {

    @GetMapping("/me")
    public String getMyInfo(Authentication authentication) {
        String uid = (String) authentication.getPrincipal();
        return "로그인한 UID: " + uid;
    }
}
