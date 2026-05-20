package com.junnu.app.dto;

import com.junnu.app.notification.NotificationChannel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class NotificationRequest {

    @NotBlank(message = "Target cannot be empty")
    private String target;

    @NotNull(message = "Channel is required")
    private NotificationChannel channel;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public NotificationChannel getChannel() {
        return channel;
    }

    public void setChannel(NotificationChannel channel) {
        this.channel = channel;
    }
}