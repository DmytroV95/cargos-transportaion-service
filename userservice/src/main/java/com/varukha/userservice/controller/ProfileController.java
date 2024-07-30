package com.varukha.userservice.controller;

import com.varukha.userservice.dto.UserProfileDTO;
import com.varukha.userservice.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
public class ProfileController {
    private final JwtUtil jwtUtil;

//    @GetMapping("/profile")
//    public UserProfileDTO getUserProfile(@AuthenticationPrincipal Jwt principal) {
//        String username = principal.getClaimAsString("preferred_username");
////        String email = principal.getClaimAsString("email");
//
//        return new UserProfileDTO(username);
//    }

    @GetMapping("/profile")
    public UserProfileDTO getUserProfile(@AuthenticationPrincipal Jwt principal) {
        String username = jwtUtil.getUserName(principal.toString());
        return new UserProfileDTO(username);
    }
}
