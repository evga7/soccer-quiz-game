package pyo.quizgame.respository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pyo.quizgame.domain.UserReport;

@Repository
public interface UserReportRepository extends CrudRepository<UserReport,Long> {

    Page<UserReport> findByReportContentContaining(String content, Pageable pageable);
}
