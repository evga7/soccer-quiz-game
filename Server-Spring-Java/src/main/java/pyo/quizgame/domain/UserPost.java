package pyo.quizgame.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@Entity
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class UserPost extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @NotNull
    private String title;

    private String nickName;

    @ColumnDefault("0")
    private Long likeCount=0L;

    @ColumnDefault("0")
    private Long viewCount=0L;

    @NotNull
    private String content;

    @ColumnDefault("0")
    private Long commentCount=0L;

    @OneToMany(mappedBy = "userPost",fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<UserComment> userComments = new ArrayList<>();


    @Builder
    public UserPost(String nickName, String title, String content) {
        this.nickName=nickName;
        this.title=title;
        this.content=content;
    }
    public void addComment(UserComment userComment)
    {
        userComments.add(userComment);
        userComment.setUserPost(this);
    }
    public void deleteComment(UserComment userComment)
    {
        userComments.remove(userComment);
    }

    public void addUserLike(UserLike userLike) {
        userLike.setUserPost(this);
    }
    @Builder
    public UserPost(Long id,String title,String content)
    {
        this.id=id;
        this.title=title;
        this.content=content;
    }


}
