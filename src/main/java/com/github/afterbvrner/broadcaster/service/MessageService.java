package com.github.afterbvrner.broadcaster.service;

import com.github.afterbvrner.broadcaster.entity.TemplateEntity;
import com.github.afterbvrner.broadcaster.exception.CannotStopSchedulingException;
import com.github.afterbvrner.broadcaster.exception.TemplateNotFoundException;
import com.github.afterbvrner.broadcaster.model.Message;
import com.github.afterbvrner.broadcaster.model.scheduled.info.ScheduleMessageInfo;
import com.github.afterbvrner.broadcaster.model.scheduled.request.ScheduledMessageRequest;
import com.github.afterbvrner.broadcaster.repository.TemplateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MessageService {

    private final MessageCreator messageCreator;
    private final BroadcastService broadcastService;
    private final SchedulerService schedulerService;
    private final TemplateRepository templateRepository;

    public void createAndSend(String templateId, Map<String, String> variables) {
        TemplateEntity template = templateRepository
                .findById(templateId)
                .orElseThrow(() -> new TemplateNotFoundException(templateId));
        Message message = messageCreator.createMessage(template.getTemplate(), variables);
        broadcastService.send(message, template.getRecipients());
    }

    public void runScheduledTask(ScheduledMessageRequest request) {
        TemplateEntity template = templateRepository
                .findById(request.getTemplateId())
                .orElseThrow(() -> new TemplateNotFoundException(request.getTemplateId()));
        Message message = messageCreator.createMessage(template.getTemplate(), request.getVariables());
        schedulerService.schedule(
                request.convertToInfo(message, new ArrayList<>(template.getRecipients()))
        );
    }

    public void stopTask(UUID id) {
        boolean stopped = schedulerService.stopTask(id);
        if (!stopped)
            throw new CannotStopSchedulingException(id);
    }

    public List<ScheduleMessageInfo> getCurrentScheduledMessages() {
        return schedulerService.getCurrentTasks();
    }

    public ScheduleMessageInfo getInfoById(UUID id) {
        return schedulerService.getInfoById(id);
    }
}