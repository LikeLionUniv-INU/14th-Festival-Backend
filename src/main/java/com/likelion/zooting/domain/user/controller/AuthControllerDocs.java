package com.likelion.zooting.domain.user.controller;

import com.likelion.zooting.domain.user.dto.AuthRequest;
import com.likelion.zooting.domain.user.dto.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import com.likelion.zooting.global.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthControllerDocs {

    @Operation(
            summary = "인스타 ID 및 본인확인 숫자 검증 API",
            description = """
                    사용자가 입력한 인스타 ID와 본인확인용 4자리 숫자를 검증합니다.

                    - 최초 접속 사용자라면 사용자 정보를 생성합니다.
                    - 기존 사용자라면 인스타 ID와 본인확인 숫자 일치 여부를 검증합니다.
                    - 이미 설문을 완료한 사용자는 재참여를 제한합니다.
                    - 검증 성공 시 이후 요청에 사용할 액세스 토큰을 발급합니다.
                    """
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "검증 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "본인확인 숫자 불일치"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 설문 완료한 사용자")
    })
    ResponseEntity<ApiResponse<AuthResponse>> verifyInstagram(
            @Parameter(description = "인스타 ID 및 본인확인 숫자 요청 정보", required = true)
            @Valid @RequestBody AuthRequest request
    );
}