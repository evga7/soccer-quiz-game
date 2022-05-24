package pyo.quizgame.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import pyo.quizgame.domain.FrontUserInfo;
import pyo.quizgame.domain.UserPost;
import pyo.quizgame.service.FrontUserService;
import pyo.quizgame.service.UserLikeService;
import pyo.quizgame.service.UserPostService;

import java.util.Optional;

@Controller
@RequiredArgsConstructor

public class UserLikeController {
    private final UserLikeService likeService;
    private final FrontUserService frontUserService;
    private final UserPostService userPostService;


    @Operation(summary = "좋아요 혹은 좋아요 취소")
    @PostMapping("/front-user/user-post/like")
    public String addLike(Long id, String nickName, Model model) {
        Optional<FrontUserInfo> frontUserInfo = frontUserService.findUser(nickName);
        Optional<UserPost> userPostByPostId = userPostService.getUserPostByPostId(id);
        String s = likeService.doLikeAndUnLike(frontUserInfo.get(), userPostByPostId.get());
        Long likeCount = userPostByPostId.get().getLikeCount();
        String likeCheck="0";
        if (s.equals("Like")) {
            likeCount++;
            likeCheck="1";
        }
        else
            likeCount--;
        model.addAttribute("likeCount", likeCount);
        model.addAttribute("likeCheck", likeCheck);
        model.addAttribute("postNickName", userPostByPostId.get().getNickName());
        model.addAttribute("commentCount", userPostByPostId.get().getCommentCount());
        model.addAttribute("postTitle", userPostByPostId.get().getTitle());
        model.addAttribute("postContent", userPostByPostId.get().getContent());
        return "posts :: #likeTable";
    }

}
