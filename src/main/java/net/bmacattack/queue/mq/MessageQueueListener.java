package net.bmacattack.queue.mq;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
//TODO make this better with multiple subscribers...
public class MessageQueueListener {
    private Map<String, MessageHandler> emitterMap = new HashMap<>();
    @EventListener
    public void messageEventHandler(Message<MessageQueueItem> event) throws IOException {
        String destination = event.getPayload().getDestination();
        MessageHandler emitter = emitterMap.get(destination);
        if (emitter == null) {
            //todo error handle
            return;
        }
        emitter.handleMessage(event);
    }

    public void subscribe(String destination, MessageHandler handler) {
        emitterMap.put(destination, handler);
    }

    public void unsubscribe(String destination) {
        emitterMap.remove(destination);
    }
}
