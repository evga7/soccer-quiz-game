package pyo.quizgame.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pyo.quizgame.dto.UserInfoDto;
import pyo.quizgame.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @Operation(summary = "스프링 시큐리티 회원가입")
    @PostMapping("/user")
    public String signup(UserInfoDto infoDto) { // 회원 추가
        String userId = infoDto.getUserId();
        if (userId.equals("admin")&&(!userService.isIdExists(userId))) {
            infoDto.setAuth("ROLE_ADMIN");
            userService.save(infoDto);
        }
        return "redirect:/login";
    }

    @Operation(summary = "로그아웃")
    @GetMapping(value = "/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

}