package pyo.quizgame.respository;

import org.springframework.data.repository.CrudRepository;
import pyo.quizgame.domain.ServerInfo;

public interface ServerInfoRepository extends CrudRepository<ServerInfo,Long> {
}
