package net.bmacattack.queue.mq;

import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import net.bmacattack.queue.persistence.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.SynchronousQueue;
import java.util.stream.Collectors;

@Component
@Scope("singleton")
public class MessageQueue {
    private Map<String, Map<String, Queue<MessageQueueItem>>> mq = new HashMap<>();

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

        queue.add(new MessageQueueItem(LocalDateTime.now().plusSeconds(15), destination, message));
        destinationMap.put(destination, queue);
        mq.put(user, destinationMap);
    }

    public synchronized List<MessageQueueItem> getMessages(String user, String destination) {
        if (mq.containsKey(user)){
            Map<String, Queue<MessageQueueItem>> destinationMap = mq.get(user);
            if (destinationMap.containsKey(destination)) {
                Queue<MessageQueueItem> queue = destinationMap.get(destination);
                List<MessageQueueItem> items = queue.stream()
                        .filter(MessageQueueItem::hasExpiered).collect(Collectors.toList());
                queue.clear();
                return items;
            }
        }
        return Collections.emptyList();
    }
}
