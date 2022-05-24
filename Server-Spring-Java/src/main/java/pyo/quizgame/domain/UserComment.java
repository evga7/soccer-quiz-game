package pyo.quizgame.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserComment extends BaseTimeEntity{

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;
    private String nickName;
    private String content;

    @ColumnDefault("0")
    private Long likes=0L;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonBackReference
    private UserPost userPost;





}
