package com.likelion.zooting.domain.history.sevice.converter;

import com.likelion.zooting.domain.history.entity.History;
import com.likelion.zooting.domain.match.entity.Matches;
import com.likelion.zooting.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class HistoryConverter {
    /**
     * 남성 사용자에 대한 매칭 상태 history 변환(남성 -> user, 여성 -> partner)
     * @param matches 매칭 정보
     * @return 남성 기준으로 생성된 이력 결과
     */
    public History toMaleHistory(Matches matches){
        return History.createMatchedUserHistory(matches.getMaleUser(), matches.getFemaleUser(), matches);
    }
    /**
     * 여성 사용자에 대한 매칭 상태 history 변환(여성 -> user, 남성 -> partner)
     * @param matches 매칭 정보
     * @return 여성 기준으로 생성된 이력 결과
     */
    public History toFemaleHistory(Matches matches){
        return History.createMatchedUserHistory(matches.getFemaleUser(), matches.getFemaleUser(), matches);
    }

    /**
     * 사용자에 대한 매칭되지 않는 상태의 history 변환
     * @param user 매칭되지 않는 사용자
     * @return 매칭 안됨 상태의 이력 결과
     */
    public History toUnmatchedHistory(User user){
        return History.createUnmatchedUserHistory(user);
    }

    /**
     * 사용자에 대한 오류 상태의 history 변환
     * @param user 오류가 발생한 사용자
     * @return 오류 상태의 이력 결과
     */
    public History toErrorHistory(User user){
        return History.createErroredUserHistory(user);
    }
}
