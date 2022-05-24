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
import pyo.quizgame.domain.UserReport;
import pyo.quizgame.service.UserReportService;

@Controller
@RequiredArgsConstructor
public class UserReportController {

    private final UserReportService userReportService;


    @Operation(summary = "게시글이나 댓글 신고하기")
    @PostMapping("/front-user/report-content")
    @ResponseBody
    public String reportContent(Long postId,String reportContent,String reporterNickName,Long commentId)
    {
        if (commentId==null)
            commentId=0L;
        UserReport userReport = UserReport.builder()
                .commentId(commentId)
                .postId(postId)
                .reporterNickName(reporterNickName)
                .reportContent(reportContent)
                .build();
        userReportService.save(userReport);

        return "";
    }


    @Operation(summary = "유저신고 조회(관리자)")
    @GetMapping("/user-reports")
    public Page<UserReport> getUserReport(Model model, @PageableDefault(size = 10) Pageable pageable,
                                          @RequestParam(required = false, defaultValue = "") String searchText)
    {
        Page<UserReport> userReports = userReportService.getUserReports(searchText, pageable);
        model.addAttribute("userReposts",userReports);
        int startPage = 1;
        int totalPages = userReports.getTotalPages();
        int currentPageNumber = userReports.getPageable().getPageNumber()+1;
        model.addAttribute("currentPageNumber",currentPageNumber);
        model.addAttribute("startPage", startPage);
        model.addAttribute("totalPages", totalPages);
        return userReportService.getUserReports(searchText, pageable);
    }

}
