package com.github.afterbvrner.broadcaster.service;

import com.github.afterbvrner.broadcaster.exception.ScheduledTaskNotFound;
import com.github.afterbvrner.broadcaster.model.Message;
import com.github.afterbvrner.broadcaster.model.scheduled.info.CronScheduledMessageInfo;
import com.github.afterbvrner.broadcaster.model.scheduled.info.FixedRateScheduledMessageInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SchedulerServiceTest {

    @Autowired
    private SchedulerService schedulerService;

    @AfterEach
    public void tearDown() {
        schedulerService.shutdown();
    }

    @Test
    public void createCronScheduling_ReturnValidInfo() throws MalformedURLException {
        List<URL> recipients = new ArrayList<>();
        recipients.add(new URL("https://httpbin.org/post"));
        CronScheduledMessageInfo initialInfo = new CronScheduledMessageInfo(
                new Message("testmessage"),
                recipients,
                "* * * * * *"
        );
        UUID taskId = schedulerService.schedule(initialInfo);
        CronScheduledMessageInfo info = (CronScheduledMessageInfo) schedulerService.getInfoById(taskId);
        assertEquals(initialInfo.getMessage(), info.getMessage(), "Messages are not equal");
        assertEquals(initialInfo.getExpression(), info.getExpression(), "Expressions are not equal");
    }

    @Test
    public void createFixedScheduling_ReturnValidInfo() throws MalformedURLException {
        List<URL> recipients = new ArrayList<>();
        recipients.add(new URL("https://httpbin.org/post"));
        FixedRateScheduledMessageInfo initialInfo = new FixedRateScheduledMessageInfo(
                new Message("testmessage"),
                recipients,
                10000
        );
        UUID taskId = schedulerService.schedule(initialInfo);
        FixedRateScheduledMessageInfo info = (FixedRateScheduledMessageInfo) schedulerService.getInfoById(taskId);
        assertEquals(initialInfo.getMessage(), info.getMessage(), "Messages are not equal");
        assertEquals(initialInfo.getFixedRate(), info.getFixedRate(), "Rates are not equal");
    }

    @Test
    public void getNonExistentTask_ThrowException() {
        UUID taskId = UUID.randomUUID();
        assertThrows(ScheduledTaskNotFound.class, () -> schedulerService.getInfoById(taskId));
    }

    @Test
    public void runTasks_GetValidTasks() throws MalformedURLException {
        List<URL> recipients = new ArrayList<>();
        recipients.add(new URL("https://httpbin.org/post"));
        FixedRateScheduledMessageInfo fixedInitialInfo = new FixedRateScheduledMessageInfo(
                new Message("testmessage"),
                recipients,
                10000
        );
        CronScheduledMessageInfo cronInitialInfo = new CronScheduledMessageInfo(
                new Message("testmessage"),
                recipients,
                "* * * * * *"
        );
        UUID fixedTaskId = schedulerService.schedule(fixedInitialInfo);
        UUID cronTaskId = schedulerService.schedule(cronInitialInfo);
        assertEquals(
                fixedInitialInfo,
                schedulerService.getInfoById(fixedTaskId),
                "Fixed rate infos are not equal"
        );
        assertEquals(
                cronInitialInfo,
                schedulerService.getInfoById(cronTaskId),
                "Cron infos are not equal"
        );
    }
}
