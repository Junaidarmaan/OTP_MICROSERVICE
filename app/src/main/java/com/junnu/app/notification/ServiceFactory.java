package com.junnu.app.notification;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceFactory {
    @Autowired
    Map<String,NotificationInterface> registry;

    public NotificationInterface getService(NotificationChannel channel){
        System.out.println("CHANNEL RECEIVED : " + channel.name() + registry.size());

        return registry.get(channel.name());
    }
}
