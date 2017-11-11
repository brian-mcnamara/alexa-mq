package net.bmacattack.queue.web;

import net.bmacattack.queue.mq.MessageQueue;
import net.bmacattack.queue.mq.MessageQueueItem;
import net.bmacattack.queue.mq.MessageQueueListener;
import net.bmacattack.queue.web.dto.QueueDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
public class QueueEndpoint {

    @Autowired
    private MessageQueue queue;

    @Autowired
    private MessageQueueListener eventListener;

    @RequestMapping(value = "/api/queue", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#queueDto, 'Write')")
    @ResponseStatus(value = HttpStatus.OK)
    public void enqueueMessage(@RequestBody QueueDto queueDto, Principal principal) {
        String username = principal.getName();
        queue.addMessage(username, queueDto.getDestination(), queueDto.getMessage());

    }

    @RequestMapping(value = "/api/queue/{destination}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasPermission(#destination, 'Read')")
    public List<MessageQueueItem> getMessageQueueItems(@PathVariable("destination") String destination, Principal principal) {
        String username = principal.getName();
        return queue.getMessages(username, destination);
    }

    @RequestMapping(value = "/api/stream/queue/{destination}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#destination, 'Read')")
    public SseEmitter getQueueEmitter(@PathVariable("destination") String destination, Principal principal) throws IOException {
        SseEmitter emitter = new SseEmitter(50000L);
        eventListener.registerEmiter(destination, emitter);
        String username = principal.getName();
        List<MessageQueueItem> queueList = queue.getMessages(username, destination);
        for (MessageQueueItem item : queueList) {
            emitter.send(item);
        }
        return emitter;
    }
}
