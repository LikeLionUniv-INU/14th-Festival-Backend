package com.likelion.zooting.domain.dailyreset.controller;

import com.likelion.zooting.domain.dailyreset.dto.DailyResetResponse;
import com.likelion.zooting.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Daily Reset", description = "일일 운영 데이터 초기화 API")
public interface DailyResetControllerDocs {

    @Operation(
            summary = "일일 데이터 초기화",
            description = """
                    매일 낮 10시 59분 59초에 자동 실행되는 일일 운영 데이터 초기화 API입니다.
                    
                    본 API는 수동 초기화 테스트 및 내부 운영 용도로 사용됩니다.
                    
                    삭제 대상:
                    - 매칭 결과 데이터
                    - 사용자 관심사 데이터
                    - 사용자 영화 장르 데이터
                    - 사용자 선호 동물상 데이터
                    - 사용자 데이터
                    
                    이력 테이블 저장은 별도 기능에서 처리되며,
                    본 API는 메인 테이블의 데이터 삭제만 수행합니다.
                    """
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "일일 데이터 초기화 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "초기화할 매칭 결과가 없는 경우"
            )
    })
    ApiResponse<DailyResetResponse> resetDailyData();
}