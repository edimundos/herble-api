package eu.herble.herbleapi.notifications.service;

import com.google.firebase.messaging.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.herble.herbleapi.notifications.data.PushNotificationRequest;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FCMService {

    @Value("${app.firebase.android.config.ttl}")
    private long ttl;

    public void sendMessageToToken(PushNotificationRequest request) {
        Message message = getPreconfiguredMessageToToken(request);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(message);
        String response = sendAndGetResponse(message);
        log.info(
                "Sent message to token. Device token: {}, {} msg {}",
                request.getToken(),
                response,
                jsonOutput);
    }

    private String sendAndGetResponse(Message message) {
        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            log.error("Firebase exception", e);
        }
        return null;
    }

    private AndroidConfig getAndroidConfig(String topic) {
        return AndroidConfig.builder()
                .setTtl(Duration.ofMinutes(ttl).toMillis())
                .setCollapseKey(topic)
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(AndroidNotification.builder().setTag(topic).build())
                .build();
    }

    private ApnsConfig getApnsConfig(String topic) {
        return ApnsConfig.builder()
                .setAps(Aps.builder().setCategory(topic).setThreadId(topic).build())
                .build();
    }

    private Message getPreconfiguredMessageToToken(PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request).setToken(request.getToken()).build();
    }

    private Message getPreconfiguredMessageWithoutData(PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request).setTopic(request.getTopic()).build();
    }

    private Message getPreconfiguredMessageWithData(
            Map<String, String> data, PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request)
                .putAllData(data)
                .setToken(request.getToken())
                .build();
    }

    private Message.Builder getPreconfiguredMessageBuilder(PushNotificationRequest request) {
        AndroidConfig androidConfig = getAndroidConfig(request.getTopic());
        ApnsConfig apnsConfig = getApnsConfig(request.getTopic());

        // Initialize the data map here
        Map<String, String> data = new HashMap<>();
        data.put("plantID", request.getPlantID());
        data.put("plantName", request.getPlantName());

        Message.Builder messageBuilder =
                Message.builder()
                        .putAllData(data)
                        .setApnsConfig(apnsConfig)
                        .setAndroidConfig(androidConfig)
                        .setNotification(
                                Notification.builder()
                                        .setTitle(request.getTitle())
                                        .setBody(request.getMessage())
                                        .build());

        return messageBuilder;
    }
}
