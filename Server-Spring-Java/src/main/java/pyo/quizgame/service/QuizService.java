package pyo.quizgame.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pyo.quizgame.domain.QuizInfo;
import pyo.quizgame.respository.QuizRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;

    public Page<QuizInfo> getQuizPage(String searchText, Pageable pageable) {
        return quizRepository.findByTitleContaining(searchText, pageable);
    }

    public Optional<QuizInfo> getQuizById(Long id) {
        return quizRepository.findById(id);
    }

    public void save(QuizInfo quizInfo) {
        quizRepository.save(quizInfo);
    }

    public void delete(Long id) {
        quizRepository.deleteById(id);
    }

    public List<QuizInfo> getRandomQuiz(Long number) {
        return quizRepository.findRandomQuiz(number);
    }

    public long getAllQuizCount() {
        return quizRepository.count();
    }






}
