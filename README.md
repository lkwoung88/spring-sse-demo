# Server-Sent Event

[SSE](https://github.com/lkwoung88/react-sse-demo?tab=readme-ov-file#server-sent-events)

---   

#### SseEmitter
* 클라이언트와 연속적인 비동기 통신이 가능
* ResponseBodyEmitter 상속

1. SseEmitter 생성 및 반환
``` java
@GetMapping(path = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)  
public ResponseEntity<SseEmitter> subscribe() {   
    SseEmitter emitter = createEmitter();  // createEmitter(Long timeout);
    return ResponseEntity.ok(emitter);  
}   
```

emitter 객체에 이벤트 핸들러 추가 가능하다.
``` java
// ResponseBodyEmitter.class
public synchronized void onTimeout(Runnable callback) {  
    this.timeoutCallback.setDelegate(callback);  
}  
  
public synchronized void onError(Consumer<Throwable> callback) {  
    this.errorCallback.setDelegate(callback);  
}  

// emitter.complete() -> onCompletion(Runnable callback) 호출 
public synchronized void onCompletion(Runnable callback) {  
    this.completionCallback.setDelegate(callback);  
}
```

```bash
log

[timeout event] server sent event timed out : id=5e2f19f2-1082-42fa-91b1-d567a59d1318

[error event] server sent event error occurred : id=dcecc161-6b44-4716-b82f-8653f11c3728, message=현재 연결은 사용자의 호스트 시스템의 소프트웨어의 의해 중단되었습니다

[completion event] server sent event removed in emitter cache: id=5e2f19f2-1082-42fa-91b1-d567a59d1318
[completion event] disconnected by completed server sent event: id=5e2f19f2-1082-42fa-91b1-d567a59d1318
```

2.  `.send()` 데이터 전송

``` HTTP
event:event
id:event-1
retry:1000
data:{"id":15623,"content":"msg = 15623","registerDate":"2024-07-30T22:50:06"}
```

``` java
emitter.send(SseEmitter.event()  
    .name("event")  // name
    .id("event-1")  // id
    .reconnectTime(RECONNECTION_TIMEOUT)  // retry
    .data(notification, MediaType.APPLICATION_JSON)); // data, media-type : json
```

[SseEmitter](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-ann-async.html#mvc-ann-async-http-streaming)   
[SseEmitter API](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/mvc/method/annotation/SseEmitter.html)
