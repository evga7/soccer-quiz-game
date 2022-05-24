package pyo.quizgame.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QuizInfoDto {
    private String title;
    private String example1;
    private String example2;
    private String example3;
    private String example4;
    private String filePath;
    private Long answer;
}
