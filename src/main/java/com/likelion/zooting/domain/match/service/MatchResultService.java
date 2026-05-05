package com.likelion.zooting.domain.match.service;

import com.likelion.zooting.domain.match.dto.MatchResultRequest;
import com.likelion.zooting.domain.match.dto.MatchResultResponse;
import com.likelion.zooting.domain.match.entity.Matches;
import com.likelion.zooting.domain.match.exception.MatchErrorCode;
import com.likelion.zooting.domain.match.repository.MatchRepository;
import com.likelion.zooting.domain.user.entity.User;
import com.likelion.zooting.domain.user.exception.UserErrorCode;
import com.likelion.zooting.domain.user.repository.UserRepository;
import com.likelion.zooting.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchResultService {

    private static final LocalTime MATCH_RESULT_OPEN_TIME = LocalTime.of(18, 0);
    private static final ZoneId KOREA_ZONE_ID = ZoneId.of("Asia/Seoul");

    private final UserRepository userRepository;
    private final MatchRepository matchRepository;

    public MatchResultResponse getMatchResult(MatchResultRequest request) {
        validateAfterOpenTime();

        User user = userRepository.findByInstagramId(request.instagramId())
                .orElseThrow(() -> new GeneralException(UserErrorCode.USER_NOT_FOUND));

        validateVerificationPin(user, request.verificationPin());

        return matchRepository.findByUserIdWithUsers(user.getUserId())
                .map(matches -> {
                    User partner = findPartnerUser(matches, user.getUserId());
                    return MatchResultResponse.matched(partner.getInstagramId());
                })
                .orElseGet(MatchResultResponse::notMatched);
    }

    // 현재 사용자가 포함된 매칭 결과에서 상대방 사용자를 찾는다.
    private User findPartnerUser(Matches matches, Long userId) {
        if (matches.getMaleUser().getUserId().equals(userId)) {
            return matches.getFemaleUser();
        }

        if (matches.getFemaleUser().getUserId().equals(userId)) {
            return matches.getMaleUser();
        }

        throw new GeneralException(MatchErrorCode.MATCH_RESULT_NOT_FOUND);
    }

    // 매칭 결과는 18시 이후에만 조회할 수 있다.
    private void validateAfterOpenTime() {
        LocalTime now = LocalTime.now(KOREA_ZONE_ID);

        if (now.isBefore(MATCH_RESULT_OPEN_TIME)) {
            throw new GeneralException(MatchErrorCode.MATCH_TIME_FORBIDDEN);
        }
    }

    // 사용자가 입력한 본인확인용 숫자가 DB에 저장된 값과 일치하는지 검증한다.
    private void validateVerificationPin(User user, String verificationPin) {
        if (!user.getUserPw().equals(verificationPin)) {
            throw new GeneralException(UserErrorCode.PIN_MISMATCH);
        }
    }
}