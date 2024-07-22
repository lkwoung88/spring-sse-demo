package hello.sse.springssedemo.domain.notification;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_notification")
@Getter
public class Notification {

    @Id
    @GeneratedValue
    private long id;

    private String content;

    private LocalDateTime registerDate;
}
