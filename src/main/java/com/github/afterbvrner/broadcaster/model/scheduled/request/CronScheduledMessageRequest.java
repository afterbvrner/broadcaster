package com.github.afterbvrner.broadcaster.model.scheduled.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.afterbvrner.broadcaster.model.Message;
import com.github.afterbvrner.broadcaster.model.scheduled.info.CronScheduledMessageInfo;
import com.github.afterbvrner.broadcaster.model.scheduled.info.ScheduledMessageInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class CronScheduledMessageRequest extends ScheduledMessageRequest {

    @NotNull
    private String expression;

    @JsonCreator
    public CronScheduledMessageRequest(@JsonProperty("templateId") String templateId,
                                       @JsonProperty("variables") Map<String, String> variables,
                                       @JsonProperty("expression") String expression) {
        this.setTemplateId(templateId);
        this.setVariables(variables);
        this.expression = expression;
    }

    @Override
    public ScheduledMessageInfo convertToInfo(Message message, List<URL> recipients) {
        return new CronScheduledMessageInfo(message, recipients, expression);
    }
}
