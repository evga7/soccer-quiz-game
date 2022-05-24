package pyo.quizgame.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FrontUserInfo extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String nickName;
    private Long totalQuizCount=0L;
    private Long solvedQuizCount=0L;
    private Double percentageOfAnswer=0D;
    private Long numberOfPosts=0L;
    private Long numberOfComments=0L;
    private Long numberOfBlockedUser=0L;
    @OneToMany(mappedBy = "frontUserInfo",fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<UserBlock> userBlocks = new HashSet<>();


    @Builder
    public FrontUserInfo(Long id, String nickName) {
        this.id=id;
        this.nickName=nickName;
    }
    public void addUserLike(UserLike userLike) {
        userLike.setFrontUserInfo(this);
    }
    public void addBlockUser(UserBlock blockUser)
    {
        userBlocks.add(blockUser);
        blockUser.setFrontUserInfo(this);
    }
    public void unBlockUser(UserBlock userBlock)
    {
        this.userBlocks.remove(userBlock);
    }
}

