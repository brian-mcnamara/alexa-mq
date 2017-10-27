package net.bmacattack.queue.mq;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class MessageQueueListener {
    private Map<String, SseEmitter> emitterMap = new HashMap<>();
    @EventListener
    public void messageEventHandler(MessageEvent event) throws IOException {
        String destination = event.getItem().getDestination();
        SseEmitter emitter = emitterMap.get(destination);
        if (emitter == null) {
            //todo error handle
            return;
        }
        emitter.send(event.getItem());
    }

    public void registerEmiter(String destination, SseEmitter emitter) {
        emitterMap.put(destination, emitter);
    }
}
