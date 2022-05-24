package pyo.quizgame.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonBackReference
    private UserPost userPost;

    @ManyToOne(fetch = FetchType.LAZY)
    private FrontUserInfo frontUserInfo;

    @Builder
    public UserLike(FrontUserInfo frontUserInfo, UserPost userPost) {
        frontUserInfo.addUserLike(this);
        userPost.addUserLike(this);
    }
}
