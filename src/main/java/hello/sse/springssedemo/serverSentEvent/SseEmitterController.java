package hello.sse.springssedemo.serverSentEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/sse")
public class SseEmitterController {

    private final SseEmitterService sseEmitterService;

    @GetMapping(path = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe() {
        String sseId = UUID.randomUUID().toString();
        SseEmitter emitter = sseEmitterService.subscribe(sseId);
        return ResponseEntity.ok(emitter);
    }
}
