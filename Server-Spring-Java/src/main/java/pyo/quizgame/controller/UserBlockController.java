package pyo.quizgame.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pyo.quizgame.domain.FrontUserInfo;
import pyo.quizgame.domain.UserBlock;
import pyo.quizgame.service.FrontUserService;
import pyo.quizgame.service.UserBlockService;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserBlockController {

    private final UserBlockService userBlockService;
    private final FrontUserService frontUserService;

    @Operation(summary = "유저 차단하기")
    @PostMapping("/front-user/user-block")
    public String addBlockUser(String nickName, String blockedNickName, RedirectAttributes redirectAttributes)
    {
        Optional<FrontUserInfo> user = frontUserService.findUser(nickName);
        userBlockService.doBlockUser(user.get(),blockedNickName);
        frontUserService.upBlockCount(nickName);
        redirectAttributes.addAttribute("nickName", nickName);
        return "redirect:/user-boards";
    }

    @Operation(summary = "유저 차단 해제하기")
    @PostMapping("/front-user/user-un-block")
    public String unBlockUser(String reportNickName, String blockedNickName,Model model)
    {
        Optional<FrontUserInfo> user = frontUserService.findUser(reportNickName);
        Optional<UserBlock> userBlock = userBlockService.unBlockUser(user.get(), blockedNickName);
        if (userBlock.isPresent())
            user.get().unBlockUser(userBlock.get());
        frontUserService.downBlockCount(reportNickName);
        model.addAttribute("blocks",user.get().getUserBlocks());
        model.addAttribute("nickName", reportNickName);
        return "userBlockInfo :: #blockTable";
    }
}
