package com.junnu.app.Services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.junnu.app.Repositories.CredentialsRepo;
import com.junnu.app.Repositories.StatisticsRepo;
import com.junnu.app.Models.Credentials;
import com.junnu.app.Models.Statistics;

@Service
public class CredentialsService {
    @Autowired
    private CredentialsRepo repo;
    @Autowired
    private StatisticsRepo stats;

    public void addCreds(Credentials cred) {
        repo.save(cred);
    }
    public enum states{
        SUCCESSFUL,
        FAILED,
        EXPIRED,
        INVALID
    }
    
    public Boolean isExpired(LocalDateTime t1, LocalDateTime t2, int expiry) {
        Duration duration = Duration.between(t1, t2);
        Long min = duration.toMinutes();
        System.out.println("the difference betwween times is " + min + " minutes ");
        if (Math.abs(min) >= expiry) {
            return true;
        }
        return false;
    }

    public ResponseEntity<String> isValid(String otp, String mail) {
        Optional<Credentials> creds = repo.findByMail(mail);
        LocalDateTime cur = LocalDateTime.now();
        String result = "";
        Statistics s = new Statistics();
        s.setEmail(mail);
        s.setOtp(otp);
        s.setTime(LocalDateTime.now());
        if (creds.isPresent()) {
            Credentials el = creds.get();
            if (el.getOtp().equals(otp)) {
                boolean flag = isExpired(el.getTime(), cur, el.getExpiry());
                if (flag) {
                    s.setStatus(states.EXPIRED.name());
                    
                    result = "OTP HAS BEEN EXPIRED, PLEASE DO TRY RESEND";
                    return ResponseEntity.status(HttpStatus.GONE).body(result);
                } else {
                    s.setStatus(states.SUCCESSFUL.name());
                    result = "OTP VERIFICATION WAS SUCCESSFUL";
                    repo.delete(el);
                    return ResponseEntity.status(HttpStatus.OK).body(result);
                }
            } else {
                int tries = el.getAttempts();
                s.setStatus(states.FAILED.name());
                stats.save(s);
                result = "YOU ENTERED WRONG OTP AND YOU LEFT WITH " + (3- tries) + " tries";
                el.setAttempts(tries + 1);
                repo.save(el);
                if(tries >= 3 ){
                    s.setStatus(states.EXPIRED.name());
                    result = "YOU ENTERED WRONG OTP AND YOU LEFT WITH " + (3- tries) + " tries"; 
                    repo.delete(el);
                }
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(result);
            }
        }else{
            s.setStatus(states.INVALID.name());
            result = "OTP NOT EXIST OR YOU PROVIDED A WRONG EMAIL";
        }
        stats.save(s);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }
}