package com.likelion.zooting.domain.user.controller;

import com.likelion.zooting.domain.user.dto.AuthRequest;
import com.likelion.zooting.domain.user.dto.AuthResponse;
import com.likelion.zooting.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/onboarding")
public class AuthController implements AuthControllerDocs {

    private final AuthService authService;

    @Override
    @PostMapping("/instagram")
    public ResponseEntity<ApiResponse<AuthResponse>> verifyInstagram(
            @Valid @RequestBody AuthRequest request
    ) {
        AuthResponse response = authService.authenticate(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
