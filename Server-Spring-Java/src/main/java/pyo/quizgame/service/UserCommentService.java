package pyo.quizgame.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pyo.quizgame.domain.UserComment;
import pyo.quizgame.respository.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCommentService {

    private final CommentRepository commentRepository;

    public List<UserComment>  getUserCommentByNickName(String nickName)
    {
        return commentRepository.findByNickName(nickName);
    }

    public UserComment save(UserComment userComment)
    {
        return commentRepository.save(userComment);
    }

    public Optional<UserComment> getUserCommentById(Long id)
    {
        return commentRepository.findById(id);
    }

    public void delete(UserComment userComment) {
        commentRepository.delete(userComment);
    }
}
