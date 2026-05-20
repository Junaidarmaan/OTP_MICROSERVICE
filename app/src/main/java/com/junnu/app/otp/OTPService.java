package com.junnu.app.otp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junnu.app.dto.NotificationRequest;
import com.junnu.app.notification.MailService;
import com.junnu.app.notification.NotificationInterface;

@Service("EMAIL")
public class OTPService implements NotificationInterface{
    @Autowired
    OtpGenerator generator;

    @Autowired
    Store redis;

    @Autowired
    MailService mail;
    public void handleRequest(NotificationRequest req){
        String otp = generator.generateOTP();
        String to = req.getTarget();
        String content = otp;
        String Subject = "EMAIL OTP VERIFICATION";   
        mail.sendEmail(to, Subject, content);
        redis.add(req.getTarget(), otp);
    }
}
