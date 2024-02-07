package eu.herble.herbleapi.notifications.service;

import eu.herble.herbleapi.notifications.data.PushNotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.*;

@Service
@Slf4j
public class PushNotificationService {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private FCMService fcmService;
    private Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    public PushNotificationService(FCMService fcmService) {
        this.fcmService = fcmService;
    }

    public void sendPushNotificationToToken(PushNotificationRequest request) {
        try {
            fcmService.sendMessageToToken(request);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void schedulePushNotification(PushNotificationRequest request) {
        LocalDateTime now = LocalDateTime.now();
        long delay = ChronoUnit.MILLIS.between(now, request.getScheduleTime());
        ScheduledFuture<?> future = scheduler.schedule(() ->
                sendPushNotificationToToken(request), delay, TimeUnit.MILLISECONDS);

        scheduledTasks.put(request.getPlantID(), future);
    }

    public boolean cancelNotification(String plantID) {
        ScheduledFuture<?> future = scheduledTasks.get(plantID);
        if (future != null) {
            boolean cancelled = future.cancel(false);
            scheduledTasks.remove(plantID); // Remove the task from the map
            return cancelled;
        }
        return false;
    }

}