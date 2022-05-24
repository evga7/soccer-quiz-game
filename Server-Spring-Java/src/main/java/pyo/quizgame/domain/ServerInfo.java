package pyo.quizgame.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Data
@Entity
public class ServerInfo {

    @Id
    private Long id;
    private Long totalNumberOfQuiz;
    private Long totalNumberOfUser;
    @OneToOne
    private FrontUserInfo topQuizPlayer;
    @OneToOne
    private FrontUserInfo badQuizPlayer;
    @OneToOne
    private FrontUserInfo bestQuizPlayer;
}
