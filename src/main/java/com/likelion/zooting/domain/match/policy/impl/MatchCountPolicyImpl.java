package com.likelion.zooting.domain.match.policy.impl;


import com.likelion.zooting.domain.match.policy.MatchCountPolicy;
import com.likelion.zooting.domain.match.service.data.MatchedPair;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class MatchCountPolicyImpl implements MatchCountPolicy {

    @Override
    public Integer getAnimalTypeCount(MatchedPair pair, Map<Long, Long> maleUserAnimalMap, Map<Long, Long> femaleUserAnimalMap, Map<Long, List<Long>> maleUserPreferredAnimalMap, Map<Long, List<Long>> femaleUserPreferredAnimalMap) {
        int count = 0;
        Long male = pair.maleUser().getUserId();
        Long female = pair.femaleUser().getUserId();
        Long maleAnimal = maleUserAnimalMap.get(male);
        Long femaleAnimal = femaleUserAnimalMap.get(female);
        List<Long> malePreferredAnimals = maleUserPreferredAnimalMap.get(male);
        List<Long> femalePreferredAnimals = femaleUserPreferredAnimalMap.get(female);
        // 남성 동물상이 여성 선호 동물상안가?
        if (femalePreferredAnimals.contains(maleAnimal)){
            count += 1;
        }
        // 여성 동물상이 남성 선호 동물상인가?
        if (malePreferredAnimals.contains(femaleAnimal)){
            count += 1;
        }

        return count;
    }

    @Override
    public Integer getInterestCount(MatchedPair pair, Map<Long, List<Long>> maleUserInterestMap, Map<Long, List<Long>> femaleUserInterestMap) {
        int count = 0;
        Long male = pair.maleUser().getUserId();
        Long female = pair.femaleUser().getUserId();
        List<Long> maleInterests = maleUserInterestMap.get(male);
        List<Long> femaleInterests = femaleUserInterestMap.get(female);

        for (Long maleInterest : maleInterests) {   // 각 남성의 관심사가
            if(femaleInterests.contains(maleInterest)){ // 여성의 관심사일 경우
                count += 1;
            }
        }

        return count;
    }

    @Override
    public Integer getMovieGenreCount(MatchedPair pair, Map<Long, List<Long>> maleUserMovieGenreMap, Map<Long, List<Long>> femaleUserMovieGenreMap) {
        int count = 0;
        Long male = pair.maleUser().getUserId();
        Long female = pair.femaleUser().getUserId();
        List<Long> maleMovieGenres = maleUserMovieGenreMap.get(male);
        List<Long> femaleMovieGenres = femaleUserMovieGenreMap.get(female);

        for (Long maleMovieGenre : maleMovieGenres) {   // 각 남성의 영화 장르가
            if(femaleMovieGenres.contains(maleMovieGenre)){ // 여성의 영화 취향일 경우
                count += 1;
            }
        }

        return count;
    }
}
