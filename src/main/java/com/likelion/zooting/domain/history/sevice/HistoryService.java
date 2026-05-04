package com.likelion.zooting.domain.history.sevice;

import com.likelion.zooting.domain.animaltype.repository.AnimalTypeRepository;
import com.likelion.zooting.domain.animaltype.service.converter.AnimalTypeIdToName;
import com.likelion.zooting.domain.history.dto.HistoryResponse;
import com.likelion.zooting.domain.history.entity.History;
import com.likelion.zooting.domain.history.repository.HistoryRepository;
import com.likelion.zooting.domain.history.sevice.converter.HistoryConverter;
import com.likelion.zooting.domain.match.entity.Matches;
import com.likelion.zooting.domain.match.repository.MatchRepository;
import com.likelion.zooting.domain.user.entity.Status;
import com.likelion.zooting.domain.user.entity.User;
import com.likelion.zooting.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * <p><b>[이력 상태 처리]</b></p>
 * <ul>
 * <li>MATCHED 처리 -> 사용자 상태가 MATCHED이며, 매칭 결과에 사용자가 남아있는 경우</li>
 * <li>FAILED 처리 -> 사용자 상태가 SUBMITTED로 남아있고, 매칭 결과에 사용자가 등록되어 있지 않는 경우</li>
 * <li>ERROR 처리 ->
 *     <ul>
 *     <li>(1) 이미 이력 관리에 저장된 사용자와 매칭을 이룰 경우</li>
 *     <li>(2) 상태가 일치하지 않을 경우(SUBMITTED인데 MATCHED일 경우, MATCHED인데 SUBMITTED일 경우)</li></li>
 *     </ul>
 * </ul>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class HistoryService {
    private final AnimalTypeRepository animalTypeRepository;
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final HistoryRepository historyRepository;
    private final AnimalTypeIdToName animalTypeIdToName;
    private final HistoryConverter historyConverter;

    public HistoryResponse createHistories() {
        // 1. 데이터(사용자, 매칭 결과) 불러오기 및 필요한 변수 설정
        List<User> users = userRepository.findAll();
        List<Matches> matches = matchRepository.findAll();

        // 동물상(ID -> name)처리에 필요한 map
        Map<Long, String> animalTypeMap = animalTypeIdToName.animalTypesToNames(animalTypeRepository.findAll());

        // 방문 여부 체크를 하기 위한 리스트와 그 리스트와 user 리스트(index -> User) 매핑을 위한 인덱스 생성
        boolean[] isVisited = new boolean[users.size()];
        Map<Long, Integer> userIndexMap = IntStream.range(0, users.size())
                .boxed()
                .collect(Collectors.toMap(i -> users.get(i).getUserId(), i -> i));

        // Batch 처리할 이력 결과물들
        List<History> histories = new ArrayList<>();

        // 결과 집계용 변수
        int totalUserCount = 0;
        int matchedUserCount = 0;
        int unmatchedUserCount = 0;
        int erredUserCount = 0;

        // 2. 매칭 성공 데이터 처리
        for (Matches m : matches) {
            User male = m.getMaleUser();
            User female = m.getFemaleUser();

            int maleIdx = userIndexMap.get(male.getUserId());
            int femaleIdx = userIndexMap.get(female.getUserId());

            // ERROR 검증(일관된 상태를 유지하는가?)
            // 이미 이전에 이력처리된 사용자
            if (male.getStatus() == Status.IN_HISTORY && female.getStatus() == Status.IN_HISTORY) {
                // 건너뜀
            }
            // ERROR일 경우(1) (이미 이력 생성한 사용자와 매칭)
            else if (male.getStatus() != Status.IN_HISTORY && female.getStatus() == Status.IN_HISTORY) {
                histories.add(historyConverter.toErrorHistory(male, animalTypeMap));
                totalUserCount += 1;
                erredUserCount += 1;
            } else if (male.getStatus() == Status.IN_HISTORY && female.getStatus() != Status.IN_HISTORY) {
                histories.add(historyConverter.toErrorHistory(female, animalTypeMap));
                totalUserCount += 1;
                erredUserCount += 1;
            }
            // ERROR일 경우(2) (user의 상태와 matches의 불일치)
            else if (male.getStatus() != Status.MATCHED || female.getStatus() != Status.MATCHED) {
                histories.add(historyConverter.toErrorHistory(male, animalTypeMap));
                histories.add(historyConverter.toErrorHistory(female, animalTypeMap));
                totalUserCount += 2;
                erredUserCount += 2;
            }
            // MATCHED일 경우(user의 상태와 matches의 상태 일치)
            else {
                histories.add(historyConverter.toMaleHistory(m, animalTypeMap));
                histories.add(historyConverter.toFemaleHistory(m, animalTypeMap));
                totalUserCount += 2;
                matchedUserCount += 2;
            }

            // 작업 완료 후 상태 업데이트
            male.updateStatus(Status.IN_HISTORY);
            female.updateStatus(Status.IN_HISTORY);
            isVisited[maleIdx] = true;
            isVisited[femaleIdx] = true;
        }

        // 3. 매칭 실패(미방문) 유저 처리
        for (int i = 0; i < users.size(); i++) {
            // 아직 방문하지 않는 사용자
            if (!isVisited[i]) {
                User user = users.get(i);
                // 이미 IN_HISTORY인 유저는 제외 (혹시라도 이전에 이력 처리 시도했을 경우)
                if (user.getStatus() == Status.IN_HISTORY) {
                    // 건너뜀
                }
                // ERROR일 경우(2) (user의 상태와 matches의 불일치)
                else if (user.getStatus() == Status.MATCHED) {
                    histories.add(historyConverter.toErrorHistory(user, animalTypeMap));
                    totalUserCount++;
                    erredUserCount++;
                } else {
                    histories.add(historyConverter.toUnmatchedHistory(user, animalTypeMap));
                    totalUserCount++;
                    unmatchedUserCount++;
                }
                user.updateStatus(Status.IN_HISTORY);
            }
        }

        // 4. Batch 처리(모든 생성한 이력 결과들 저장)
        // 먼저 DB에 이력 결과 밀어 넣기(혹시 에러 발생 시 이후의 작업이 멈춤)
        historyRepository.saveAllAndFlush(histories);

        // 5. 결과 반환
        return HistoryResponse.builder()
                .isComplete(true)
                .totalUserCount(totalUserCount)
                .matchedUserCount(matchedUserCount)
                .unmatchedUserCount(unmatchedUserCount)
                .erredUserCount(erredUserCount)
                .simulatedAt(LocalDateTime.now())
                .build();
    }
}
