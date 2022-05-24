package pyo.quizgame.respository;

import org.apache.catalina.User;
import org.springframework.data.repository.CrudRepository;
import pyo.quizgame.domain.UserComment;

import java.util.List;

public interface CommentRepository extends CrudRepository<UserComment,Long> {

    List<UserComment> findByNickName(String nickName);

}
