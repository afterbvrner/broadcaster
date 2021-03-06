package com.github.afterbvrner.broadcaster.service;

import com.github.afterbvrner.broadcaster.exception.EndpointNotAvailableException;
import com.github.afterbvrner.broadcaster.model.Message;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.List;

@Service
@AllArgsConstructor
public class RestBroadcastService implements BroadcastService {

    private final RestTemplate restTemplate;

    @Override
    public void send(Message message, List<URL> receivers) {
        for (var receiver : receivers) {
            try {
                restTemplate.postForLocation(receiver.toString(), message);
            } catch (HttpClientErrorException | ResourceAccessException e) {
                throw new EndpointNotAvailableException(receiver.toString(), e);
            }
        }
    }
}
