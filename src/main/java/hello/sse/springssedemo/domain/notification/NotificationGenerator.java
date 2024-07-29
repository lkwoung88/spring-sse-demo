package hello.sse.springssedemo.domain.notification;

import hello.sse.springssedemo.domain.notification.Notification;
import hello.sse.springssedemo.domain.notification.NotificationRepository;
import hello.sse.springssedemo.serverSentEvent.SseEmitterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationGenerator {

    private final NotificationRepository notificationRepository;
    private final SseEmitterService sseEmitterService;
    private static int sequence = 1;

    @Scheduled(fixedRate = 1000 * 5, initialDelay = 1000)
    public void generateNotification() {


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = Timestamp.valueOf(dateFormat.format(new Date())).toLocalDateTime();


        Notification notification = Notification.builder()
                .content("msg = " + sequence++)
                .registerDate(now)
                .build();

        notificationRepository.save(notification);

        sseEmitterService.broadcast(notification);
    }
}
