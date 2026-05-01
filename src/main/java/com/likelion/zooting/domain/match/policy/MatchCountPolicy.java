package com.likelion.zooting.domain.match.policy;

import com.likelion.zooting.domain.match.service.data.MatchedPair;

import java.util.List;
import java.util.Map;

/**
 * <p><b>매칭 속성별 개수 산정 정책</b></p>
 * <p><b>[동물상 매칭 수]</b></p>
 * <ul>
 * <li>자신에 대한 관심사 : 1 택</li>
 * <li>선호하는 관심사 : 2 택(상관없음 -> 실질적으로 6개)</li>
 * </ul>
 * <p><b>[관심사 매칭 수]</b></p>
 * <ul>
 * <li>관심사 : 3 택</li>
 * </ul>
 * <p><b>[영화 장르 매칭 수]</b></p>
 * <ul>
 * <li>영화 장르 : 2 택</li>
 * </ul>
 */
public interface MatchCountPolicy {
    /**
     * 주어진 쌍에 대해 매칭된 동물상의 수를 나타냅니다.
     * <p><b>[세부 설명]</b></p>
     * <ul>
     * <li>주어진 쌍은 단방향성이므로 매칭 점수와 개수 변화 간 관계를 따지면 안됩니다.</li>
     * <li><b>개수 산정 : </b>[남성 동물상] == [여성 선호 동물상] || [여성 동물상] == [남성 선호 동물상] 일 경우의 개수</li>
     * <li><b>최소 개수 : </b>0 개</li>
     * <li><b>최대 개수 : </b>2 개</li>
     * </ul>
     *
     * @param pair 매칭 쌍(남성, 여성)
     * @param maleUserAnimalMap 남성 사용자에 대한 동물상 맵
     * @param femaleUserAnimalMap 여성 사용자에 대한 동물상 맵
     * @param maleUserPreferredAnimalMap 남성 사용자에 대한 선호 동물상 맵
     * @param femaleUserPreferredAnimalMap 여성 사용자에 대한 선호 동물상 맵
     * @return 매칭된 동물쌍 수
     */
    Integer getAnimalTypeCount(MatchedPair pair,
                               Map<Long, Long> maleUserAnimalMap,
                               Map<Long, Long> femaleUserAnimalMap,
                               Map<Long, List<Long>> maleUserPreferredAnimalMap,
                               Map<Long, List<Long>> femaleUserPreferredAnimalMap);

    /**
     * 주어진 쌍에 대해 매칭된 관심사의 수를 나타냅니다.
     * <p><b>[세부 설명]</b></p>
     * <ul>
     * <li>양방향성으로 교차 검증을 통해 개수를 산정합니다.</li>
     * <li><b>최소 개수 : </b>0 개</li>
     * <li><b>최대 개수 : </b>3 개</li>
     * </ul>
     *
     * @param pair 매칭 쌍(남성, 여성)
     * @param maleUserInterestMap 남성 사용자에 대한 관심사 맵
     * @param femaleUserInterestMap 여성 사용자에 대한 관심사 맵
     * @return 매칭된 관심사 수
     */
    Integer getInterestCount(MatchedPair pair,
                             Map<Long, List<Long>> maleUserInterestMap,
                             Map<Long, List<Long>> femaleUserInterestMap);

    /**
     * 주어진 쌍에 대해 매칭된 영화 장르의 수를 나타냅니다.
     * <p><b>[세부 설명]</b></p>
     * <ul>
     * <li>양방향성으로 교차 검증을 통해 개수를 산정합니다.</li>
     * <li><b>최소 개수 : </b>0 개</li>
     * <li><b>최대 개수 : </b>2 개</li>
     * </ul>
     *
     * @param pair 매칭 쌍(남성, 여성)
     * @param maleUserMovieGenreMap 남성 사용자에 대한 영화 장르 맵
     * @param femaleUserMovieGenreMap 여성 사용자에 대한 영화 장르 맵
     * @return 매칭된 영화 장르 수
     */
    Integer getMovieGenreCount(MatchedPair pair,
                               Map<Long, List<Long>> maleUserMovieGenreMap,
                               Map<Long, List<Long>> femaleUserMovieGenreMap);
}
