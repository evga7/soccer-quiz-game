package pyo.quizgame.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class UserPostDto {

    private Long id;
    private String nickName;
    @Size(min = 2,max = 25,message = "제목을 2~25자 사이로 입력해주세요.")
    private String title;
    @Size(min = 2, max = 500,message = "내용은 2~500자 사이로 입력해주세요.")
    private String content;
    private String createdAt;
    private String modifiedAt;
    private List<UserCommentDto> comments;
}
