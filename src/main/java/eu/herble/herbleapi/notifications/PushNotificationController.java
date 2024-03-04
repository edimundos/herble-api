package eu.herble.herbleapi.notifications;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import eu.herble.herbleapi.notifications.data.CancelNotificationRequest;
import eu.herble.herbleapi.notifications.data.PushNotificationRequest;
import eu.herble.herbleapi.notifications.data.PushNotificationResponse;
import eu.herble.herbleapi.notifications.service.PushNotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/notifications")
public class PushNotificationController {

    private PushNotificationService pushNotificationService;

    public PushNotificationController(PushNotificationService pushNotificationService) {
        this.pushNotificationService = pushNotificationService;
    }

    @PostMapping
    public ResponseEntity<PushNotificationResponse> sendTokenNotification(
            @RequestBody PushNotificationRequest request) {
        if (request.getScheduleTime() != null) {
            pushNotificationService.schedulePushNotification(request);
        } else {
            pushNotificationService.sendPushNotificationToToken(request);
        }
        return new ResponseEntity<>(
                new PushNotificationResponse(HttpStatus.OK.value(), "Notification scheduled."),
                HttpStatus.OK);
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelNotification(
            @RequestBody CancelNotificationRequest cancelRequest) {
        boolean isCancelled = pushNotificationService.cancelNotification(cancelRequest.getPlantID());
        if (isCancelled) {
            return ResponseEntity.ok("Notification cancelled successfully");
        } else {
            return ResponseEntity.status(NOT_FOUND).body("Notification not found or already executed");
        }
    }
}
