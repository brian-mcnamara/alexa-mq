package net.bmacattack.queue.mq;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class UserQueueItem {
    private Map<String, Queue<MessageQueueItem>> userQueue = new HashMap<>();
    private Multimap<String, SseEmitter> streamClients = HashMultimap.create();

    public void enqueueMessage(String destination, String message, Map<String, String > parameters) {
        MessageQueueItem item = new MessageQueueItem(LocalDateTime.now().plusSeconds(15), destination, message, parameters);
        if (streamClients.containsKey(destination)) {
            for (SseEmitter emitter : streamClients.get(destination)) {
                try {
                    emitter.send(item);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return;
        }
        Queue<MessageQueueItem> destinationQueue = userQueue.get(destination);
        if (destinationQueue == null) {
            destinationQueue = new ArrayDeque<>();
            userQueue.put(destination, destinationQueue);
        }
        destinationQueue.add(item);
    }

    //todo, is the list linked?
    public List<MessageQueueItem> getMessages(String destination) {
        if (userQueue.containsKey(destination)) {
            Queue<MessageQueueItem> queue = userQueue.get(destination);
            List<MessageQueueItem> items = queue.stream()
                    .filter(mq -> !mq.hasExpiered()).collect(Collectors.toList());
            queue.clear();
            return items;
        }
        return Collections.emptyList();
    }

    public synchronized void registerStream(String destination, SseEmitter emitter) throws IOException {
        streamClients.put(destination, emitter);
        List<MessageQueueItem> items = getMessages(destination);
        for (MessageQueueItem item : items) {
            emitter.send(item);
        }
    }

    public void unregisterStream(SseEmitter emitter) {
        Map.Entry<String, SseEmitter> emiter = streamClients.entries().stream()
                .filter(e -> e.getValue().equals(emitter)).findFirst().orElse(null);
        if (emiter != null) {
            streamClients.remove(emiter.getKey(), emiter.getValue());
        }
    }
}
