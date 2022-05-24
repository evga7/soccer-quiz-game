package pyo.quizgame.respository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pyo.quizgame.domain.QuizInfo;

import java.util.List;

public interface QuizRepository extends JpaRepository<QuizInfo,Long> {

    Page<QuizInfo> findByTitleContaining(String title, Pageable pageable);

    @Query(nativeQuery=true, value="SELECT *  FROM quiz_info ORDER BY rand() LIMIT ?1")
    List<QuizInfo> findRandomQuiz(@Param("number")Long number);
}
