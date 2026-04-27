package com.likelion.zooting.domain.match.mapper;

import com.likelion.zooting.domain.match.dto.data.UserAnimalMatchCandidate;
import com.likelion.zooting.domain.match.dto.data.UserInterestMatchCandidate;
import com.likelion.zooting.domain.match.dto.data.UserMovieGenreMatchCandidate;
import com.likelion.zooting.domain.user.entity.User;
import com.likelion.zooting.domain.userinterest.entity.UserInterest;
import com.likelion.zooting.domain.usermoviegenre.entity.UserMovieGenre;
import com.likelion.zooting.domain.userpreferredanimaltype.entity.UserPreferredAnimalType;
import org.springframework.stereotype.Component;

@Component
public class MatchMapper {
    public Long mapToUserId(User u){
        return u.getUserId();
    }

    public UserAnimalMatchCandidate mapToUserAnimalMatchCandidate(User u, UserPreferredAnimalType upa){
        return new UserAnimalMatchCandidate(u.getUserId(), u.getAnimalType().getAnimalTypeId(), upa.getAnimalType().getAnimalTypeId());
    }

    public UserInterestMatchCandidate mapToUserInterestMatchCandidate(User u, UserInterest ui){
        return new UserInterestMatchCandidate(u.getUserId(), ui.getInterest().getInterestId());
    }

    public UserMovieGenreMatchCandidate mapToUserMovieGenreMatchCandidate(User u, UserMovieGenre umg){
        return new UserMovieGenreMatchCandidate(u.getUserId(), umg.getMovieGenre().getMovieGenreId());
    }
}
