package net.bmacattack.queue.mq;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.Map;

public class MessageQueueItem {
    private LocalDateTime experationDate;
    private String destination;
    private String message;
    private Map<String, String> parameters;

    public MessageQueueItem(LocalDateTime experationDate, String destination, String message, Map<String, String> parameters) {
        this.experationDate = experationDate;
        this.destination = destination;
        this.message = message;
        this.parameters = parameters;
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

    public Map<String, String> getParameters() {
        return parameters;
    }

    @JsonIgnore
    public boolean hasExpiered() {
        return LocalDateTime.now().isAfter(experationDate);
    }
}
