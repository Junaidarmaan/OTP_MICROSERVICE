package com.junnu.app.Models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Credentials {
    @Id
    String mail;
    String otp;
    LocalDateTime time;
    int expiry;
    int attempts;
    int maxLimit;

    public Credentials(){  
        this.attempts = 1;
    }
    
    public String getOtp() {
        return otp;
    }
    public void setOtp(String otp) {
        this.otp = otp;
    }
    public LocalDateTime getTime() {
        return time;
    }
    public void setTime(LocalDateTime time) {
        this.time = time;
    }
    public int getExpiry() {
        return expiry;
    }
    public void setExpiry(int expiry) {
        this.expiry = expiry;
    }
    public int getAttempts() {
        return attempts;
    }
    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }
    public String getMail() {
        return mail;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }
    @Override
    public String toString() {
        return "Credentials [mail=" + mail + ", otp=" + otp + ", time=" + time + ", expiry=" + expiry + ", attempts="
                + attempts + "]";
    }

    public int getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(int maxLimit) {
        this.maxLimit = maxLimit;
    }

}
