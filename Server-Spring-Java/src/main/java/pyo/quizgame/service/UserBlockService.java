package pyo.quizgame.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pyo.quizgame.domain.UserBlock;
import pyo.quizgame.domain.FrontUserInfo;
import pyo.quizgame.respository.UserBlockRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserBlockService {

    private final UserBlockRepository userBlockRepository;

    public String doBlockUser(FrontUserInfo frontUserInfo,String blockedNickName) {
        Optional<UserBlock> blockUser = findBlockUser(frontUserInfo,blockedNickName);
        if (blockUser.isPresent())
            return "NO";
        userBlockRepository.save(new UserBlock(frontUserInfo,blockedNickName));
        return "Block";
    }
    public Optional<UserBlock> unBlockUser(FrontUserInfo frontUserInfo,String blockedNickName)
    {
        Optional<UserBlock> blockUser = findBlockUser(frontUserInfo,blockedNickName);
        if (blockUser.isPresent())
            userBlockRepository.delete(blockUser.get());
        return blockUser;
    }
    public Optional<UserBlock> findBlockUser(FrontUserInfo frontUserInfo,String blockedNickName)
    {
        return userBlockRepository.findByFrontUserInfoAndBlockedNickName(frontUserInfo,blockedNickName);
    }

}
