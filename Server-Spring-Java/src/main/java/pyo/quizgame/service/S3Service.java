package pyo.quizgame.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pyo.quizgame.domain.QuizInfo;
import pyo.quizgame.domain.S3FileInfo;
import pyo.quizgame.respository.S3Repository;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Repository s3Repository;
    public S3FileInfo getS3FileInfo(QuizInfo quizInfo)
    {
        return s3Repository.findByFileOriginalName(quizInfo.getFileOriginalName());
    }

    public void save(S3FileInfo s3FileInfo)
    {
        s3Repository.save(s3FileInfo);
    }
}
