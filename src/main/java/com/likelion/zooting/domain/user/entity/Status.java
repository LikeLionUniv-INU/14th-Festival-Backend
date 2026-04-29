package com.likelion.zooting.domain.user.entity;

public enum Status {
    IN_PROGRESS,    // 설문 작성 중
    SUBMITTED,      // 설문 제출 완료
    MATCHED,        // 매칭 성공
    FAILED          // 매칭 실패
}
