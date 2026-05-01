package com.likelion.zooting.domain.user.controller;

import com.likelion.zooting.domain.user.dto.PrivacyRequest;
import com.likelion.zooting.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;

public interface PrivacyControllerDocs {

    @Operation(
            summary = "개인정보 동의 여부 제출 API",
            description = """
                    로그인 후 발급받은 액세스 토큰을 기반으로 현재 사용자를 식별하고,
                    개인정보 수집 및 이용 동의 여부를 저장합니다.
                    """
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "개인정보 동의 여부 저장 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "개인정보 동의 거부"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자 없음")
    })
    ResponseEntity<ApiResponse<Void>> submitPrivacyConsent(
            @Parameter(hidden = true) Authentication authentication,
            @Parameter(description = "개인정보 동의 여부 요청 정보", required = true)
            @Valid @RequestBody PrivacyRequest request
    );
}