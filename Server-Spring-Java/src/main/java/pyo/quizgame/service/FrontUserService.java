package pyo.quizgame.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pyo.quizgame.domain.FrontUserInfo;
import pyo.quizgame.respository.FrontUserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FrontUserService {
    private final FrontUserRepository frontUserRepository;
    public Optional<FrontUserInfo> getUserFindById(Long id){
        return frontUserRepository.findById(id);
    }
    public void save(FrontUserInfo frontUserInfo)
    {
        frontUserRepository.save(frontUserInfo);
    }

    public Optional<FrontUserInfo> findUser(String nickName) {
        return frontUserRepository.findByNickName(nickName);
    }

    public boolean checkNickName(String nickName) {
        return frontUserRepository.existsByNickName(nickName);
    }

    public Long getPlayerRank(String nickName) {
        return frontUserRepository.getPlayerRanking(nickName);
    }

    public List<FrontUserInfo> getRank() {
        return frontUserRepository.getPlayerTop100();
    }

    public long getUserCount() {
        return frontUserRepository.count();
    }

    public Optional<FrontUserInfo> getBadPlayer() {
        return frontUserRepository.findBadPlayer();
    }

    public Optional<FrontUserInfo> getBestPlayer() {
        return frontUserRepository.getBestPlayer();
    }

    public Optional<FrontUserInfo> getTopQuizPlayer() {
        return frontUserRepository.findTop1ByOrderByTotalQuizCountDesc();
    }
    public void upPostCount(String nickName)
    {
        frontUserRepository.upPostCount(nickName);
    }
    public void downPostCount(String nickName)
    {
        frontUserRepository.downPostCount(nickName);
    }

    public void upCommentCount(String nickName)
    {
        frontUserRepository.upCommentCount(nickName);
    }
    public void downCommentCount(String nickName)
    {
        frontUserRepository.downCommentCount(nickName);
    }

    public void upBlockCount(String nickName)
    {
        frontUserRepository.upBlockCount(nickName);
    }
    public void downBlockCount(String nickName)
    {
        frontUserRepository.downBlockCount(nickName);
    }


}
