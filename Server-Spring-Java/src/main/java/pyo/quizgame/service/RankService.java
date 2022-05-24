package pyo.quizgame.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pyo.quizgame.domain.UserRankInfo;
import pyo.quizgame.respository.RankRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
public class RankService {

    private final RankRepository rankRepository;

    public void save(List<UserRankInfo> userRankInfos)
    {
        rankRepository.saveAll(userRankInfos);
    }

    public Iterable<UserRankInfo> getRank()
    {
        return rankRepository.findAll();
    }

}
