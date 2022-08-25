package pyo.quizgame.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pyo.quizgame.domain.FrontUserInfo;
import pyo.quizgame.domain.UserComment;
import pyo.quizgame.domain.UserPost;
import pyo.quizgame.domain.UserRankInfo;
import pyo.quizgame.service.FrontUserService;
import pyo.quizgame.service.RankService;
import pyo.quizgame.service.UserCommentService;
import pyo.quizgame.service.UserPostService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class FrontUserController {

    private final FrontUserService frontUserService;
    private final RankService rankService;
    private final UserPostService userPostService;
    private final UserCommentService userCommentService;


    @Operation(summary = "프론트유저 회원가입 (닉네임 설정)")
    @PostMapping("/front-user")
    @ResponseBody
    public String signup(FrontUserInfo frontUserInfo) {
        frontUserService.save(frontUserInfo);
        return "OK";
    }

    @Operation(summary = "프론트유저 퀴즈 정보 업데이트")
    @PutMapping("/front-user/user")
    @ResponseBody
    public String updateUser(FrontUserInfo frontUserInfo) {
        Optional<FrontUserInfo> user = frontUserService.findUser(frontUserInfo.getNickName());
        if (!user.isEmpty()) {
            long totalCount = user.get().getTotalQuizCount() + frontUserInfo.getTotalQuizCount();
            long solveCount = user.get().getSolvedQuizCount() + frontUserInfo.getSolvedQuizCount();
            double percentage = (double) solveCount / (double) totalCount;
            user.get().setTotalQuizCount(totalCount);
            user.get().setSolvedQuizCount(solveCount);
            user.get().setPercentageOfAnswer(percentage);
            frontUserService.save(user.get());
            return "OK";
        }
        return "NO";
    }
    @Operation(summary = "프론트유저 닉네임 조건설정 API")
    @PostMapping("/front-user/check")
    @ResponseBody
    public String CheckNickName(String nickName) {
        boolean matches = nickName.matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*");
        if (frontUserService.checkNickName(nickName))
            return "exits";
        else if (nickName.contains("운영자")||nickName.contains("admin"))
            return "noNickName";
        else if (nickName.isEmpty())
            return "empty";
        else if (nickName.length()>8)
            return "length";
        else if (!matches)
            return "notMatch";
        else
            return "OK";
    }
    @Operation(summary = "프론트유저 전체등수 가져오기")
    @PostMapping("/front-user/rank")
    @ResponseBody
    public UserRankInfo getPlayerRank(String nickName) {
        Optional<FrontUserInfo> frontUserInfo = frontUserService.findUser(nickName);
        if (!frontUserInfo.isEmpty())
        {
            Long playerRanking = frontUserService.getPlayerRank(nickName);
            return UserRankInfo.builder()
                    .id(playerRanking)
                    .nickName(frontUserInfo.get().getNickName())
                    .percentageOfAnswer(frontUserInfo.get().getPercentageOfAnswer())
                    .solvedQuizCount(frontUserInfo.get().getSolvedQuizCount())
                    .totalQuizCount(frontUserInfo.get().getTotalQuizCount())
                    .build();
        }
        return null;
    }

    //10분마다 랭킹 업데이트
    @Operation(summary = "랭킹 TOP100 10분마다 업데이트하기")
    @Scheduled(cron = "0 0/10 * * * *")
    @GetMapping("/quiz/user-rank")
    @ResponseBody
    public String UpdateRankTable()
    {
        List<FrontUserInfo> rankList = frontUserService.getRank();
        List<UserRankInfo> userRankInfos = new ArrayList();
        Long sequence=0L;
        for (FrontUserInfo frontUserInfo : rankList) {
            userRankInfos.add(UserRankInfo.builder()
                    .id(++sequence)
                    .nickName(frontUserInfo.getNickName())
                    .percentageOfAnswer(frontUserInfo.getPercentageOfAnswer())
                    .solvedQuizCount(frontUserInfo.getSolvedQuizCount())
                    .totalQuizCount(frontUserInfo.getTotalQuizCount())
                    .build());
        }
        rankService.save(userRankInfos);
        return "OK";
    }

    @Operation(summary = "유저 페이지 보여주기")
    @GetMapping("/front-user/user-page")
    public String userPage(@RequestParam(required = true)String nickName, Model model)
    {
        Optional<FrontUserInfo> user = frontUserService.findUser(nickName);
        model.addAttribute("user", user.get());
        return "userPage";
    }

    @Operation(summary = "유저 게시글 정보 보여주기")
    @GetMapping("/front-user/user-post-info")
    public String getUserPostInfo(@RequestParam String nickName,Model model)
    {
        Optional<FrontUserInfo> user = frontUserService.findUser(nickName);
        List<UserPost> userPosts = userPostService.getUserPostByNickName(nickName);
        model.addAttribute("posts", userPosts);
        return "userPostInfo";
    }

    @Operation(summary = "유저 차단정보 보여주기")
    @GetMapping("/front-user/user-block-info")
    public String getUserBlockInfo(@RequestParam String nickName,Model model)
    {
        Optional<FrontUserInfo> user = frontUserService.findUser(nickName);
        model.addAttribute("blocks", user.get().getUserBlocks());
        model.addAttribute("nickName", nickName);
        return "userBlockInfo";
    }
    @Operation(summary = "유저 댓글정보 보여주기")
    @GetMapping("/front-user/user-comment-info")
    public String getUserCommentInfo(@RequestParam String nickName, Model model)
    {
        List<UserComment> userCommentByNickName = userCommentService.getUserCommentByNickName(nickName);
        model.addAttribute("comments", userCommentByNickName);
        return "userCommentInfo";
    }

}
