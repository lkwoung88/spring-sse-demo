package hello.sse.springssedemo.serverSentEvent;

import hello.sse.springssedemo.domain.notification.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SseEmitterService {

    private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();   // thread-safe 한 컬렉션 객체로 sse emitter 객체를 관리해야 한다.
    private static final long TIMEOUT = 1000 * 60;
    private static final long RECONNECTION_TIMEOUT = 1000L;

    public SseEmitter subscribe(String id) {
        SseEmitter emitter = getSseEmitter(id);
        emitterMap.put(id, emitter);

        //초기 연결시에 응답 데이터를 전송할 수도 있다.
        try {
            SseEmitter.SseEventBuilder event = SseEmitter.event()
                    .name("open")                 // event 명 (event:open)
                    .id(String.valueOf("open-1"))       // event id (id:open-1) - 재연결시 클라이언트에서 `Last-Event-ID` 헤더에 마지막 event id 를 설정
                    .data("SSE connected")            // event data payload (data:SSE connected)
                    .reconnectTime(RECONNECTION_TIMEOUT);    // SSE 연결이 끊어진 경우 재접속 하기까지 대기 시간 (retry:1000)
            emitter.send(event);
        } catch (IOException e) {
            log.error("failure send media position data, id={}, {}", id, e.getMessage());
        }
        return emitter;
    }

    private SseEmitter getSseEmitter(String id) {
        SseEmitter emitter = new SseEmitter(TIMEOUT);

        //연결 세션 timeout 이벤트 핸들러 등록
        emitter.onTimeout(() -> {
            log.info("server sent event timed out : id={}", id);
            //onCompletion 핸들러 호출
            emitter.complete();
        });

        //에러 핸들러 등록
        emitter.onError(e -> {
            log.info("server sent event error occurred : id={}, message={}", id, e.getMessage());
            //onCompletion 핸들러 호출
            emitter.complete();
        });

        //SSE complete 핸들러 등록
        emitter.onCompletion(() -> {
            if (emitterMap.remove(id) != null) {
                log.info("server sent event removed in emitter cache: id={}", id);
            }

            log.info("disconnected by completed server sent event: id={}", id);
        });
        return emitter;
    }

    public void broadcast(Notification notification) {
        emitterMap.forEach((id, emitter) -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("event")
                        .id("event-1")
                        .reconnectTime(RECONNECTION_TIMEOUT)
                        .data(notification, MediaType.APPLICATION_JSON));
                log.info("sent notification, id={}, payload={}", id, notification);
            } catch (IOException e) {
                log.error("fail to send emitter id={}, {}", id, e.getMessage());    //SSE 세션이 이미 해제된 경우
            }
        });
    }
}
