package net.bmacattack.queue.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("singleton")
public class MessageQueue {

    @Autowired
    private UserQueueRepository userQueueRepository;

    public synchronized void addMessage(String user, String destination, String message) {
        UserQueueItem userQueueItem = userQueueRepository.getQueueForUser(user);
        userQueueItem.enqueueMessage(destination, message);
    }

    public synchronized List<MessageQueueItem> getMessages(String user, String destination) {
        UserQueueItem userQueueItem = userQueueRepository.getQueueForUser(user);
        return userQueueItem.getMessages(destination);
    }
}
