package com.likelion.zooting.domain.match.service;

import com.likelion.zooting.domain.match.policy.MatchValidationType;
import com.likelion.zooting.global.exception.GeneralException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

@Component
public class MatchValidateService {
    /**
     * 매칭에 사용될 엔티티 리스트의 정합성을 검증합니다.
     *
     * <p><b>[검증 항목]</b></p>
     * <ul>
     * <li><b>도메인 범위:</b> 입력받은 리스트가 비어있거나 유효하지 않은지 확인합니다.</li>
     * <li><b>속성 누락:</b> 엔티티 내 필수 속성값이 누락되었는지 확인합니다.</li>
     * <li><b>중복성:</b> 리스트 내에 동일한 데이터가 중복으로 존재하는지 검사합니다.</li>
     * </ul>
     *
     * @param list 검사 대상 엔티티 리스트
     * @param type 검사 대상 엔티티 타입 (에러 메시지 처리용)
     * @throws GeneralException 데이터 정합성 검증 실패 시 발생
     */
    protected <T> void checkValidate(List<T> list, MatchValidationType type) {
        // 1. list가 비어있는지 여부 확인
        if (list.isEmpty()) {
            throw new GeneralException(type.getErrorCode());
        }
        // 2. 속성 값 누락 검증
        for (T t : list) {
            if (t == null) {
                throw new GeneralException(type.getErrorCode());
            }
        }
        // 3. 중복 데이터 검사
        if (new HashSet<>(list).size() != list.size()) {
            throw new GeneralException(MatchValidationType.DUPLICATED_ID.getErrorCode());
        }
    }
}
