package com.github.afterbvrner.broadcaster.service;

import com.github.afterbvrner.broadcaster.model.Message;

import java.util.List;

public interface BroadcastService {
    void send(Message message, List<String> receiver);
}
