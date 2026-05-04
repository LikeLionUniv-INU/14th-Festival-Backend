package com.likelion.zooting.domain.profile.controller;

import com.likelion.zooting.domain.profile.dto.ProfileResponse;
import com.likelion.zooting.domain.profile.service.ProfileService;
import com.likelion.zooting.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

  private final ProfileService profileService;

  @GetMapping
  public ResponseEntity<ApiResponse<ProfileResponse>> getProfile(
      @AuthenticationPrincipal Long userId
  ) {
    ProfileResponse response = profileService.getProfile(userId);
    return ResponseEntity.ok(ApiResponse.success(response));
  }
}
