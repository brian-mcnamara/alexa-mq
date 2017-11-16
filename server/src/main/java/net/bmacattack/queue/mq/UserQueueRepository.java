package net.bmacattack.queue.mq;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Scope("singleton")
public class UserQueueRepository {
    private Map<String, UserQueueItem> queueMap = new HashMap<>();

    public UserQueueItem getQueueForUser(String user) {
        UserQueueItem userQueueItem = queueMap.get(user);
        if (userQueueItem == null) {
            userQueueItem = new UserQueueItem();
            queueMap.put(user, userQueueItem);
        }
        return userQueueItem;
    }
}
