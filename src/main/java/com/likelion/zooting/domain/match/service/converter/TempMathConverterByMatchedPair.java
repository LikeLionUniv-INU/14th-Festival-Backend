package com.likelion.zooting.domain.match.service.converter;

import com.likelion.zooting.domain.match.service.data.MatchedPair;
import com.likelion.zooting.domain.match.service.data.TempMatch;
import com.likelion.zooting.domain.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TempMathConverterByMatchedPair {
    public MatchedPair tempMatchToMatchedPair(TempMatch tempMatch, List<User> maleUsers, List<User> femaleUsers){
        return new MatchedPair(
                maleUsers.get(tempMatch.maleUserIndex()),
                femaleUsers.get(tempMatch.femaleUserIndex()),
                tempMatch.score());
    }
}
