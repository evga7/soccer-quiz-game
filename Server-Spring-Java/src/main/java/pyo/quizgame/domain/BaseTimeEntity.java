package pyo.quizgame.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@MappedSuperclass
public class BaseTimeEntity {

    @CreatedDate
    private String createdDate;


    @LastModifiedDate
    private String modifiedDate;

    @PrePersist
    public void onPrePersist() {
        this.createdDate = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        this.modifiedDate = this.createdDate;
    }

    @PreUpdate
    public void onPreUpdate() {
        this.modifiedDate = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    }


}
