package com.github.afterbvrner.broadcaster.controller;

import com.github.afterbvrner.broadcaster.model.MessageRequest;
import com.github.afterbvrner.broadcaster.model.scheduled.info.ScheduleMessageInfo;
import com.github.afterbvrner.broadcaster.model.scheduled.request.ScheduledMessageRequest;
import com.github.afterbvrner.broadcaster.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/messages")
@AllArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/default")
    public void sendMessage(@RequestBody MessageRequest messageRequest) {
        messageService.createAndSend(messageRequest.getTemplateId(), messageRequest.getVariables());
    }

    @PostMapping("/scheduled")
    public void setScheduling(@RequestBody ScheduledMessageRequest request) {
        messageService.runScheduledTask(request);
    }

    @DeleteMapping("scheduled/{taskId}/stop")
    public void stopScheduling(@PathVariable UUID taskId) {
        messageService.stopTask(taskId);
    }

    @GetMapping("/scheduled")
    public List<ScheduleMessageInfo> getCurrentScheduledMessages() {
        return messageService.getCurrentScheduledMessages();
    }

    @GetMapping("/scheduled/{taskId}")
    public ScheduleMessageInfo getCurrentScheduledMessages(@PathVariable UUID taskId) {
        return messageService.getInfoById(taskId);
    }
}