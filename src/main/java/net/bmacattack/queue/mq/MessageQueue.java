package net.bmacattack.queue.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Scope("singleton")
public class MessageQueue {
    private Map<String, Map<String, Queue<MessageQueueItem>>> mq = new HashMap<>();

    @Autowired
    private MessageQueueListener eventListener;

    public synchronized void addMessage(String user, String destination, String message) {
        Map<String, Queue<MessageQueueItem>> destinationMap;
        if (!mq.containsKey(user)) {
            destinationMap = new HashMap<>();
        } else {
            destinationMap = mq.get(user);
        }
        Queue<MessageQueueItem> queue;
        if (!destinationMap.containsKey(destination)) {
            queue = new ArrayDeque<>();
        } else {
            queue = destinationMap.get(destination);
        }

        MessageQueueItem item = new MessageQueueItem(LocalDateTime.now().plusSeconds(15), destination, message);

        queue.add(item);
        destinationMap.put(destination, queue);
        mq.put(user, destinationMap);
        try {
            eventListener.messageEventHandler(new GenericMessage<>(item));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized List<MessageQueueItem> getMessages(String user, String destination) {
        if (mq.containsKey(user)){
            Map<String, Queue<MessageQueueItem>> destinationMap = mq.get(user);
            if (destinationMap.containsKey(destination)) {
                Queue<MessageQueueItem> queue = destinationMap.get(destination);
                List<MessageQueueItem> items = queue.stream()
                        .filter(mq -> !mq.hasExpiered()).collect(Collectors.toList());
                queue.clear();
                return items;
            }
        }
        return Collections.emptyList();
    }
}
