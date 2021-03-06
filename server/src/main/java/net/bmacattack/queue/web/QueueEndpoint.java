package net.bmacattack.queue.web;

import net.bmacattack.queue.mq.MessageQueue;
import net.bmacattack.queue.mq.MessageQueueItem;
import net.bmacattack.queue.mq.UserQueueItem;
import net.bmacattack.queue.mq.UserQueueRepository;
import net.bmacattack.queue.web.dto.QueueDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private UserQueueRepository queueRepository;

    @RequestMapping(value = "/api/queue", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#queueDto, 'Write') or hasRole('ADMIN')")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void enqueueMessage(@RequestBody QueueDto queueDto, Principal principal) {
        String username = principal.getName();
        queue.addMessage(username, queueDto.getDestination(), queueDto.getMessage(), queueDto.getParameters());

    }

    @RequestMapping(value = "/api/queue/{destination}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasPermission(#destination, 'Read') or hasRole('ADMIN')")
    public List<MessageQueueItem> getMessageQueueItems(@PathVariable("destination") String destination, Principal principal) {
        String username = principal.getName();
        return queue.getMessages(username, destination);
    }

    @RequestMapping(value = "/api/stream/queue/{destination}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#destination, 'Read')")
    public SseEmitter getQueueEmitter(@PathVariable("destination") String destination, Principal principal) throws IOException {
        String username = principal.getName();
        SseEmitter emitter = new SseEmitter(50000L);
        UserQueueItem queueItem = queueRepository.getQueueForUser(username);
        queueItem.registerStream(destination, emitter);
        List<MessageQueueItem> queueList = queue.getMessages(username, destination);
        for (MessageQueueItem item : queueList) {
            emitter.send(item);
        }
        emitter.onTimeout(() -> {
            queueItem.unregisterStream(emitter);
        });
        emitter.onCompletion(() -> {
            queueItem.unregisterStream(emitter);
        });
        emitter.onError((e) -> {
            e.printStackTrace();
            queueItem.unregisterStream(emitter);
        });
        return emitter;
    }
}
