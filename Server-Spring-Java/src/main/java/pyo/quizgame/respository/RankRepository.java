package pyo.quizgame.respository;

import org.springframework.data.repository.CrudRepository;
import pyo.quizgame.domain.UserRankInfo;

public interface RankRepository extends CrudRepository<UserRankInfo,Long> {


}
