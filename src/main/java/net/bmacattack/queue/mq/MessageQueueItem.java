package net.bmacattack.queue.mq;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public class MessageQueueItem {
    private LocalDateTime experationDate;
    private String destination;
    private String message;

    public MessageQueueItem(LocalDateTime experationDate, String destination, String message) {
        this.experationDate = experationDate;
        this.destination = destination;
        this.message = message;
    }

    @JsonIgnore
    public LocalDateTime getExperationDate() {
        return experationDate;
    }

    public String getDestination() {
        return destination;
    }

    public String getMessage() {
        return message;
    }

    @JsonIgnore
    public boolean hasExpiered() {
        return LocalDateTime.now().isAfter(experationDate);
    }
}
