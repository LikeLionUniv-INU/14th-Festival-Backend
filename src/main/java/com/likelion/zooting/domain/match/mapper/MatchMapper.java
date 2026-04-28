package com.likelion.zooting.domain.match.mapper;

import com.likelion.zooting.domain.match.dto.data.*;
import com.likelion.zooting.domain.user.entity.User;
import com.likelion.zooting.domain.userinterest.entity.UserInterest;
import com.likelion.zooting.domain.usermoviegenre.entity.UserMovieGenre;
import com.likelion.zooting.domain.userpreferredanimaltype.entity.UserPreferredAnimalType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MatchMapper {
    /**
     * 사용자와 동물상, 선호 도물상으로 매핑합니다
     * @param u 사용자
     * @param upa 사용자 선호 동물타입
     * @return 사용자와 동물상과의 매칭을 위한 가공데이터
     */
    public UserAnimalMatchCandidate mapToUserAnimalMatchCandidate(User u, UserPreferredAnimalType upa){
        return new UserAnimalMatchCandidate(u.getUserId(), u.getAnimalType().getAnimalTypeId(), upa.getAnimalType().getAnimalTypeId());
    }

    /**
     * 사용자와 관심사를 매핑합니다
     * @param u 사용자
     * @param ui 사용자 관심사
     * @return 사용자와 관심사의 매칭을 위한 가공데이터
     */
    public UserInterestMatchCandidate mapToUserInterestMatchCandidate(User u, UserInterest ui){
        return new UserInterestMatchCandidate(u.getUserId(), ui.getInterest().getInterestId());
    }

    /**
     * 사용자와 영화 장르를 매핑합니다.
     * @param u 사용자
     * @param umg 사용자 영화 장르
     * @return 사용자와 영화 장르의 매칭을 위한 가공데이터
     */
    public UserMovieGenreMatchCandidate mapToUserMovieGenreMatchCandidate(User u, UserMovieGenre umg){
        return new UserMovieGenreMatchCandidate(u.getUserId(), umg.getMovieGenre().getMovieGenreId());
    }
}
