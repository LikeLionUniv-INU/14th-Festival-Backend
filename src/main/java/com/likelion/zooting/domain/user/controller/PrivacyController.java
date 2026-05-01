package com.likelion.zooting.domain.user.controller;

import com.likelion.zooting.domain.user.dto.PrivacyRequest;
import com.likelion.zooting.domain.user.service.PrivacyService;
import com.likelion.zooting.global.exception.GeneralException;
import com.likelion.zooting.global.exception.code.GlobalErrorCode;
import com.likelion.zooting.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/onboarding")
public class PrivacyController implements PrivacyControllerDocs {

    private final PrivacyService privacyService;

    @Override
    @PostMapping("/privacy")
    public ResponseEntity<ApiResponse<Void>> submitPrivacyConsent(
            Authentication authentication,
            @Valid @RequestBody PrivacyRequest request
    ) {
        if (authentication == null) {
            throw new GeneralException(GlobalErrorCode.UNAUTHORIZED);
        }

        String instagramId = authentication.getName();
        privacyService.submitPrivacyConsent(instagramId, request);

        return ResponseEntity.ok(
                ApiResponse.success(null)
        );
    }
}
