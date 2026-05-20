package com.junnu.app.notification;

import com.junnu.app.dto.NotificationRequest;

public interface NotificationInterface {
    void handleRequest(NotificationRequest req);
}
