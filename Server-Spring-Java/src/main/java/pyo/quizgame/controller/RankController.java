package pyo.quizgame.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pyo.quizgame.domain.UserRankInfo;
import pyo.quizgame.service.RankService;

@RestController
@RequiredArgsConstructor
public class RankController {

    private final RankService rankService;

    @Operation(summary = "유저 랭킹테이블 가져오기")
    @GetMapping("/front-user/user-rank")
    public Iterable<UserRankInfo> getUserRank()
    {
        return rankService.getRank();
    }
}
