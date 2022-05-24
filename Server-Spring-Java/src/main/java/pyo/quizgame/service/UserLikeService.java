package pyo.quizgame.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pyo.quizgame.domain.FrontUserInfo;
import pyo.quizgame.domain.UserLike;
import pyo.quizgame.domain.UserPost;
import pyo.quizgame.respository.PostRepository;
import pyo.quizgame.respository.UserLikeRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserLikeService {
    //final 안넣어서 몇시간날렸냐이거...
    private final UserLikeRepository likeRepository;
    private final PostRepository postRepository;
    public String doLikeAndUnLike(FrontUserInfo frontUserInfo, UserPost userPost) {
        Optional<UserLike> alreadyLike = isAlreadyLike(frontUserInfo, userPost);
        if(alreadyLike.isPresent())
         {
             postRepository.unLikes(userPost.getId());
             likeRepository.delete(alreadyLike.get());
             return "unLike";
         }
        postRepository.addLikes(userPost.getId());
        likeRepository.save(new UserLike(frontUserInfo, userPost));
        return "Like";
    }
    public void delLike(UserLike userLike)
    {
        likeRepository.delete(userLike);
    }
    public Optional<UserLike> isAlreadyLike(FrontUserInfo frontUserInfo, UserPost userPost) {
        return likeRepository.findByFrontUserInfoAndUserPost(frontUserInfo, userPost);
    }
    public List<UserLike> getUserLikeByPostId(UserPost userPost)
    {
        return likeRepository.findAllByUserPost(userPost);
    }
}
