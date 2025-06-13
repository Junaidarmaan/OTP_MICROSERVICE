package com.junnu.app.Services;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import com.junnu.app.Models.Credentials;
import com.junnu.app.Repositories.CredentialsRepo;


@Component
public class Cleaner {
    @Autowired
    CredentialsRepo db;
    // @Scheduled(fixedRate = 120000)
    public void clearDataBase(){
        System.out.println("database cleaning is initiated :");
        List<Credentials> entities = db.findAll();
        for(Credentials entity : entities){
            LocalDateTime time = entity.getTime();
            int expiry = entity.getExpiry();
            LocalDateTime cur = LocalDateTime.now();
            Duration duration = Duration.between(time, cur);
            Long min = duration.toMinutes();
            if(Math.abs(min) >= expiry){
                db.delete(entity);
            System.out.println("Object expired : " + entities.toString());
            }

        }
    }
}
