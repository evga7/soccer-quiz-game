package pyo.quizgame.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class UserRankInfo {

    @Id
    private Long id;
    private String nickName;
    private Long totalQuizCount;
    private Long solvedQuizCount;
    private Double percentageOfAnswer;


    @Builder
    public UserRankInfo(Long id, String nickName, Long totalQuizCount, Long solvedQuizCount, Double percentageOfAnswer) {
        this.id = id;
        this.nickName = nickName;
        this.totalQuizCount = totalQuizCount;
        this.solvedQuizCount = solvedQuizCount;
        this.percentageOfAnswer = percentageOfAnswer;
    }
}
