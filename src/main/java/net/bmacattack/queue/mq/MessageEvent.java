package net.bmacattack.queue.mq;

import org.springframework.context.ApplicationEvent;

public class MessageEvent extends ApplicationEvent {
    private MessageQueueItem item;
    public MessageEvent(Object source, MessageQueueItem item) {
        super(source);
        this.item = item;
    }

    public MessageQueueItem getItem() {
        return item;
    }
}
