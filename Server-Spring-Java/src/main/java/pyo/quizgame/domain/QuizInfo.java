package pyo.quizgame.domain;


import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class QuizInfo extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String title;
    @NotNull
    private Long answer;
    private String example1;
    private String example2;
    private String example3;
    private String example4;
    private String fileOriginalName;
    private String changeFileOriginalName;
    @OneToOne
    @JoinColumn
    private S3FileInfo s3FileInfo;

}
