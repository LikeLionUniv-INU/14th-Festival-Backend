package com.likelion.zooting.domain.match.service.converter;

import com.likelion.zooting.domain.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class MatchUserConverter {
    /**
     * 사용자 리스트를 "사용자 ID -> 인덱스" 맵으로 변환합니다.
     *
     * @param users 사용자 리스트
     * @return 사용자 ID -> 인덱스 맵
     */
    public Map<Long, Integer> getUserIdToIndex(List<User> users) {
        return IntStream.range(0, users.size()).boxed().collect(Collectors.toMap(i -> users.get(i).getUserId(), i -> i));
    }

    /**
     * 사용자 리스트를 "인덱스 -> 사용자 ID" 맵으로 변환합니다.
     *
     * @param users 사용자 리스트
     * @return 인덱스 -> 사용자 ID 맵
     */
    public Map<Integer, Long> getIndexToUserId(List<User> users) {
        return IntStream.range(0, users.size()).boxed().collect(Collectors.toMap(i -> i, i -> users.get(i).getUserId()));
    }
}
