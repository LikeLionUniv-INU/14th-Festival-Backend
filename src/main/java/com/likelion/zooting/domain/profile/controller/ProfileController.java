package com.likelion.zooting.domain.profile.controller;

import com.likelion.zooting.domain.profile.dto.ProfileResponse;
import com.likelion.zooting.domain.profile.service.ProfileService;
import com.likelion.zooting.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

  private final ProfileService profileService;

  @GetMapping
  public ResponseEntity<ApiResponse<ProfileResponse>> getMyProfile(@RequestAttribute Long userId) {
    ProfileResponse response = profileService.getProfile(userId);

    return ResponseEntity.ok(ApiResponse.success(response));
  }
}