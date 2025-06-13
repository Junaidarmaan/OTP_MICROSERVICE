package com.junnu.app.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.junnu.app.DTO.UserData;
import com.junnu.app.Services.CredentialsService;
import com.junnu.app.Services.MailService;
import com.junnu.app.Services.StatisticsServices;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class HomeController {
    @Autowired
    MailService service;
    @Autowired
    CredentialsService cred;
    @Autowired
    StatisticsServices stats;
    @PostMapping("/sendotp")
    public ResponseEntity<String> sendOTP(@RequestBody UserData data,HttpServletRequest request){
        String ip = request.getRemoteAddr();
        System.out.println("IP ADDRESS OF CLIENT IS : " + ip);
        return service.sendEMail(data);
    }
    @PostMapping("/validate/{otp}/{mail}")
    public ResponseEntity<String> validateOTP(@PathVariable String otp,@PathVariable String mail){
        return cred.isValid(otp,mail);
    }
    @GetMapping("/success")
    public float getSuccessRate(){
        return stats.getSuccessRate();
    }

    @GetMapping("/failure")
    public float getFailureRate(){
        return stats.getFailureRate();
    }
    @GetMapping("/expired")
    public float getExpiredRate(){
        return stats.getExpiredRate();
    }

    @GetMapping("/invalid")
    public float getInvalidRate(){
        return stats.getInvalidRate();
    }
}
