package pyo.quizgame.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String blockedNickName;

    @ManyToOne
    @JsonBackReference
    private FrontUserInfo frontUserInfo;

    @Builder
    public UserBlock(FrontUserInfo frontUserInfo,String blockedNickName) {
        this.blockedNickName = blockedNickName;
        frontUserInfo.addBlockUser(this);
    }

}
