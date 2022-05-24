package pyo.quizgame.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Setter
@Getter
public class UserCommentDto {
    private Long id;
    private String nickName;
    @NotEmpty(message = "댓글을 입력해주세요")
    @Size(min = 1,max = 45,message = "댓글은 1~45자 사이로 입력해주세요.")
    private String content;
    private String createdDate;
    private String modifiedDate;

}
