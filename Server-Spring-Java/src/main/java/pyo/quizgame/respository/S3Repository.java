package pyo.quizgame.respository;

import org.springframework.data.repository.CrudRepository;
import pyo.quizgame.domain.S3FileInfo;

public interface S3Repository extends CrudRepository<S3FileInfo,Long> {


    S3FileInfo findByFileOriginalName(String fileOriginalName);
}
