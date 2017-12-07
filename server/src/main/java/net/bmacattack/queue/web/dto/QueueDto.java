package net.bmacattack.queue.web.dto;

import java.util.Map;

public class QueueDto {
    private String destination;
    private String message;
    private Map<String, String> parameters;

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    //TODO params
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueueDto queueDto = (QueueDto) o;

        if (!destination.equals(queueDto.destination)) return false;
        return message.equals(queueDto.message);
    }

    @Override
    //TODO params
    public int hashCode() {
        int result = destination.hashCode();
        result = 31 * result + message.hashCode();
        return result;
    }
}
