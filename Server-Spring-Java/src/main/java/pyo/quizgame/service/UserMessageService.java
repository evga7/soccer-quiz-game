package pyo.quizgame.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pyo.quizgame.domain.UserMessage;
import pyo.quizgame.respository.MessageRepository;

@Service
@RequiredArgsConstructor
public class UserMessageService {
    private final MessageRepository messageRepository;

    public Page<UserMessage> getUserMessages(String searchText, Pageable pageable) {
        return messageRepository.findByMessageContaining(searchText, pageable);
    }

    public void save(UserMessage userMessage)
    {
        messageRepository.save(userMessage);
    }
}
