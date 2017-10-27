package net.bmacattack.queue.web;

import net.bmacattack.queue.mq.MessageQueue;
import net.bmacattack.queue.mq.MessageQueueItem;
import net.bmacattack.queue.mq.MessageQueueListener;
import net.bmacattack.queue.web.dto.QueueDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.security.Principal;
import java.util.List;

@Controller
public class QueueEndpoint {

    @Autowired
    private MessageQueue queue;

    @Autowired
    private MessageQueueListener eventListener;

    @RequestMapping(value = "/api/queue", method = RequestMethod.POST)
    @PreAuthorize("#oauth2.hasScope('Write')")
    @ResponseStatus(value = HttpStatus.OK)
    public void enqueueMessage(@RequestBody QueueDto queueDto, Principal principal) {
        String username = principal.getName();
        queue.addMessage(username, queueDto.getDestination(), queueDto.getMessage());

    }

    @RequestMapping(value = "/api/queue/{destination}", method = RequestMethod.GET)
    @ResponseBody
    //@PreAuthorize("hasRole('ADMIN'} or #oauth2.hasScope('Read')")
    public List<MessageQueueItem> getMessageQueueItems(@PathVariable("destination") String destination, Principal principal) {
        String username = principal.getName();
        return queue.getMessages(username, destination);
    }

    @RequestMapping(value = "/queue/{destination}", method = RequestMethod.GET)
    public SseEmitter getQueueEmitter(@PathVariable("destination") String destination, Principal principal) {
        SseEmitter emitter = new SseEmitter();
        eventListener.registerEmiter(destination, emitter);
        return emitter;
    }
}
