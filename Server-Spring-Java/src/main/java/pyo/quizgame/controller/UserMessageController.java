package pyo.quizgame.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pyo.quizgame.domain.UserMessage;
import pyo.quizgame.dto.UserMessageDto;
import pyo.quizgame.service.UserMessageService;

@Controller
@RequiredArgsConstructor
public class UserMessageController {

    private final UserMessageService userMessageService;

    @Operation(summary = "프론트 유저 메시지 받는 API")
    @PostMapping("/front-user/message")
    public @ResponseBody String getUserMessage(UserMessageDto userMessage)
    {
        UserMessage message = UserMessage.builder()
                .nickName(userMessage.getNickName())
                .message(userMessage.getMessage())
                .build();
        userMessageService.save(message);
        return "OK";
    }


    @Operation(summary = "유저 의견 페이지 불러오기")
    @GetMapping("/user-messages")
    public String getUserMessagePage(Model model, @PageableDefault(size = 10) Pageable pageable,
                                     @RequestParam(required = false, defaultValue = "") String searchText)
    {
        Page<UserMessage> userMessages = userMessageService.getUserMessages(searchText, pageable);
        int startPage = 1;
        int totalPages = userMessages.getTotalPages();
        int currentPageNumber = userMessages.getPageable().getPageNumber()+1;
        model.addAttribute("currentPageNumber",currentPageNumber);
        model.addAttribute("startPage", startPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("userMessages", userMessages);
        return "userMessages";
    }
}
