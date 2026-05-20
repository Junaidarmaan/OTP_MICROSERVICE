package com.junnu.app.otp;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Store {
    @Value("${otp.DEFAULT_EXPIRY}")
    int expiryInMinutes;

    ConcurrentHashMap<String, OtpData> db = new ConcurrentHashMap<>();

    public void add(String user, String otp) {
        Long EXPIRY = System.currentTimeMillis() + (expiryInMinutes * 60 * 1000);
        db.put(user, new OtpData(otp, EXPIRY, 0));
    }

    public boolean isValid(String user, String otp) {
        if (db.containsKey(user)) {
            OtpData data = db.get(user);
            if (data.getExpiry() > System.currentTimeMillis() && data.getOtp().equals(otp)) {
                db.remove(user);
                return true;
            } else {
                if (data.getTries() >= 3) {
                    db.remove(user);
                } else {
                    data.setTries(data.getTries() + 1);
                    db.put(user, data);
                }
                System.out.println("otp is expired or not matched in REDIS request OTP again");
                return false;
            }
        } else {
            System.out.println("otp does not existed in REDIS request OTP again");
        }
        return false;
    }

    public List<String> getAll() {
        return db.entrySet().stream().map(entry -> entry.getKey() + ": " + entry.getValue()).toList();
    }
}
