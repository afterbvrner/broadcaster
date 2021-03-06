package com.github.afterbvrner.broadcaster.model.scheduled.info;

import com.github.afterbvrner.broadcaster.entity.ScheduledMessageEntity;
import com.github.afterbvrner.broadcaster.model.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class ScheduledMessageInfo {
    private Message message;
    private List<URL> recipients;

    public abstract ScheduledMessageEntity toEntity();
}
