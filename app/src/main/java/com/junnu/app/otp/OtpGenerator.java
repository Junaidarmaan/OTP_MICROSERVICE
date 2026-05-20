package com.junnu.app.otp;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

@Service
public class OtpGenerator {
    String generateOTP(){
        int otp = ThreadLocalRandom.current().nextInt(100000,999999);
        return Integer.toString(otp);
    }
}
