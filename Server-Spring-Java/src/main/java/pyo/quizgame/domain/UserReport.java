package pyo.quizgame.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class UserReport extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long postId=0L;
    private Long commentId=0L;
    private String reporterNickName;
    private String reportContent;

    @Builder
    public UserReport(Long postId,String reportContent,String reporterNickName,Long commentId) {
        this.commentId=commentId;
        this.reporterNickName=reporterNickName;
        this.postId=postId;
        this.reportContent=reportContent;
    }

}
