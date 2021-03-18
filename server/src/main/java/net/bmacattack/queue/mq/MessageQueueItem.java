package net.bmacattack.queue.mq;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.Map;

public class MessageQueueItem {
    private LocalDateTime expirationDate;
    private String destination;
    private String message;
    private Map<String, String> parameters;

    public MessageQueueItem(LocalDateTime expirationDate, String destination, String message, Map<String, String> parameters) {
        this.expirationDate = expirationDate;
        this.destination = destination;
        this.message = message;
        this.parameters = parameters;
    }

    @JsonIgnore
    public LocalDateTime getExpirationDate() {
        return expirationDate;
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
        return LocalDateTime.now().isAfter(expirationDate);
    }
}
