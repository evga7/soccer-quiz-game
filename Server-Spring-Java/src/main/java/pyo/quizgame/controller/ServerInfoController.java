package pyo.quizgame.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pyo.quizgame.domain.FrontUserInfo;
import pyo.quizgame.domain.ServerInfo;
import pyo.quizgame.service.FrontUserService;
import pyo.quizgame.service.QuizService;
import pyo.quizgame.service.ServerInfoService;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class ServerInfoController {

    private final QuizService quizService;
    private final FrontUserService frontUserService;
    private final ServerInfoService serverInfoService;

    //1시간마다 유저수 퀴즈갯수 badplayer 많이푼 플레이어 보냄
    @Operation(summary = "프론트로 보내는 서버 정보 1시간마다 업데이트")
    @Scheduled(cron = "0 0 0/1 * * *")
    @GetMapping("/cron-total-number-of-quiz")
    public void cronServerInfo()
    {
        long totalQuizCount = quizService.getAllQuizCount();
        long totalUserCount = frontUserService.getUserCount();
        Optional<FrontUserInfo> topQuizPlayer = frontUserService.getTopQuizPlayer();
        Optional<FrontUserInfo> badPlayer = frontUserService.getBadPlayer();
        Optional<FrontUserInfo> bestPlayer = frontUserService.getBestPlayer();
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setTotalNumberOfQuiz(totalQuizCount);
        serverInfo.setTotalNumberOfUser(totalUserCount);
        serverInfo.setId(0L);
        if (!topQuizPlayer.isEmpty())
        serverInfo.setTopQuizPlayer(topQuizPlayer.get());
        if (!badPlayer.isEmpty())
        serverInfo.setBadQuizPlayer(badPlayer.get());
        if (!bestPlayer.isEmpty())
        serverInfo.setBestQuizPlayer(bestPlayer.get());
        serverInfoService.save(serverInfo);
    }

    @Operation(summary = "서버정보 전송하기")
    @GetMapping("/front-user/server-info")
    public Optional<ServerInfo> getServerInfo()
    {
        Optional<ServerInfo> info = serverInfoService.getInfo();
        return info;
    }
    @Operation(summary = "버전체크")
    @GetMapping("/front-user/version-check")
    public String versionCheck()
    {
        return "0.0.5-SNAPSHOT";
    }

}
