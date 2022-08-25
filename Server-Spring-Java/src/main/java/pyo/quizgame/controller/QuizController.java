package pyo.quizgame.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pyo.quizgame.domain.QuizInfo;
import pyo.quizgame.domain.S3FileInfo;
import pyo.quizgame.dto.QuizInfoDto;
import pyo.quizgame.service.QuizService;
import pyo.quizgame.service.S3Service;

import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@Controller
@Slf4j
public class QuizController {
    private final S3Service s3Service;
    private final QuizService quizService;

    @Operation(summary = "퀴즈 추가 페이지로 이동")
    @GetMapping("/quiz/write")
    public String goAddQuiz(Model model)
    {
        return "addQuiz";
    }


    @Operation(summary = "메인 페이지 불러오기")
    @GetMapping("/")
    public String getQuizPage(Model model, @PageableDefault(size = 10) Pageable pageable,
                             @RequestParam(required = false, defaultValue = "") String searchText)
    {
        Page<QuizInfo> quizInfos = quizService.getQuizPage(searchText, pageable);
        int startPage = 1;
        int totalPages = quizInfos.getTotalPages();
        int currentPageNumber = quizInfos.getPageable().getPageNumber()+1;
        model.addAttribute("currentPageNumber",currentPageNumber);
        model.addAttribute("startPage", startPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("quizs", quizInfos);
        return "main";
    }

    @Operation(summary = "퀴즈 상세페이지 이동")
    @GetMapping("/quiz/{id}")
    public String quiz(@PathVariable Long id, Model model) {
        getQuiz(id,model);
        return "quiz";
    }
    @Operation(summary = "퀴즈 수정페이지 불러오기")
    @GetMapping("/quiz/edit-page/{id}")
    public String editQuiz(@PathVariable Long id, Model model) {
        getQuiz(id, model);
        return "editQuiz";
    }

    /**
     *
     * @param quizInfo
     * 만약 이미지 수정되었으면 수정된거 원래이미지명으로 바꿔줌
     * @return
     */
    @Operation(summary = "퀴즈 추가하는 API")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/quiz")
    public String insertQuiz(QuizInfo quizInfo,RedirectAttributes redirectAttributes)
    {
        insertBasicImage(quizInfo);
        insertQuizInfo(quizInfo);
        redirectAttributes.addAttribute("posted", true);
        return "redirect:/";
    }

    @Operation(summary = "퀴즈 수정 POST")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/quiz/{id}")
    public String modifyQuiz(@PathVariable Long id,QuizInfo quizInfo, RedirectAttributes redirectAttributes)
    {
        insertBasicImage(quizInfo);
        changedImageCheck(quizInfo);
        insertQuizInfo(quizInfo);
        redirectAttributes.addAttribute("changed", true);
        return "redirect:/";
    }
    @Operation(summary = "퀴즈 삭제 API")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/quiz/{id}")
    public String deleteQuiz(@PathVariable Long id,RedirectAttributes redirectAttributes) {
        quizService.delete(id);
        redirectAttributes.addAttribute("deleted", true);
        return "redirect:/";
    }

    @Operation(summary = "퀴즈 번호로 해당퀴즈 가져오는 API")
    @GetMapping("/front-user/quiz/{id}")
    public @ResponseBody QuizInfoDto findQuiz(@PathVariable Long id)
    {
        Optional<QuizInfo> quizInfo = quizService.getQuizById(id);
        if (quizInfo.isEmpty())
            return null;
        QuizInfoDto quizInfoDto = QuizInfoDto.builder()
                .title(quizInfo.get().getTitle())
                .example1(quizInfo.get().getExample1())
                .example2(quizInfo.get().getExample2())
                .example3(quizInfo.get().getExample3())
                .example4(quizInfo.get().getExample4())
                .filePath(quizInfo.get().getS3FileInfo().getFilePath())
                .answer(quizInfo.get().getAnswer())
                .build();

        return quizInfoDto;
    }
    @Operation(summary = "랜덤 퀴즈 가져와 프론트에 전달하는 API")
    @GetMapping("/front-user/random-quiz")
    public @ResponseBody List<QuizInfoDto> findRandomQuiz(@Param("number")Long number)
    {
        List<QuizInfo> randomQuiz = quizService.getRandomQuiz(number);
        List<QuizInfoDto> quizList = new ArrayList<>();
        for (QuizInfo quizInfo : randomQuiz) {
            QuizInfoDto quiz = QuizInfoDto.builder()
                    .title(quizInfo.getTitle())
                    .example1(quizInfo.getExample1())
                    .example2(quizInfo.getExample2())
                    .example3(quizInfo.getExample3())
                    .example4(quizInfo.getExample4())
                    .filePath(quizInfo.getS3FileInfo().getFilePath())
                    .answer(quizInfo.getAnswer())
                    .build();
            quizList.add(quiz);
        }
        return quizList;
    }
    private void getQuiz(Long id, Model model) {
        Optional<QuizInfo> quiz = quizService.getQuizById(id);
        if (!quiz.isEmpty()) {
            model.addAttribute("quiz", quiz);
            model.addAttribute("path", quiz.get().getS3FileInfo().getFilePath());
        }
    }

    private void insertBasicImage(QuizInfo quizInfo) {
        String fileOriginalName = quizInfo.getFileOriginalName();
        //이렇게 get으로가져오면 null 이어도 ""로 가져와서 isEmpty 로 검사해야 정확함
        String stringNoImage="noImage.png";
        if (StringUtils.isEmpty(fileOriginalName)) {
            quizInfo.setFileOriginalName(stringNoImage);
        }
    }

    private void insertQuizInfo(QuizInfo quizInfo) {
        S3FileInfo s3FileInfo = s3Service.getS3FileInfo(quizInfo);
        quizInfo.setS3FileInfo(s3FileInfo);
        String s3FileName = s3FileInfo.getFileOriginalName();
        if (!s3FileName.equals("noImage.png")) {
            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();
            String uploadTime = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
            String fileOriginalName = uploadTime + "_" + s3FileInfo.getFileOriginalName();
            s3FileInfo.setFileOriginalName(fileOriginalName);
            quizInfo.setFileOriginalName(fileOriginalName);
            s3Service.save(s3FileInfo);
        }
        quizService.save(quizInfo);
    }
    private void changedImageCheck(QuizInfo quizInfo) {
        //만약 수정되었으면 수정된 주소를 원래주소에 넣어주고 null값 다시 넣기
        String changeFileOriginalName = quizInfo.getChangeFileOriginalName();
        if (!StringUtils.isEmpty(changeFileOriginalName))
        {
            quizInfo.setFileOriginalName(changeFileOriginalName);
            quizInfo.setChangeFileOriginalName(null);
        }
    }


}
