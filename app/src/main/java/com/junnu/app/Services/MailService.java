package com.junnu.app.Services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.junnu.app.DTO.UserData;
import com.junnu.app.Models.Credentials;
import com.junnu.app.Repositories.CredentialsRepo;


@Service
public class MailService {
    @Autowired
    private JavaMailSender mail;
    @Autowired
    private CredentialsService creds;
    @Autowired
    private CredentialsRepo repo;
    public ResponseEntity<String> sendEMail(UserData  data){
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(data.getEmail());
        message.setSubject(data.getSubject());
        String code = generateCode(data.getType(),data.getLength());
        message.setText(data.getContent() + code);
        mail.send(message);
        System.out.println("Received data is " + data.toString());
        System.out.println("generated code is :" + code);
        if(data.getLength() > 12 || data.getLength() < 4){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The Length must be between 4 and 12 inclusive");
        }if(data.getExpiry() < 5 || data.getExpiry() > 60){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The expiry must be between 5 and 60 inclusive");
        }
        Credentials c = new Credentials();
        c.setMail(data.getEmail());
        c.setExpiry(data.getExpiry());
        LocalDateTime time = LocalDateTime.now();
        c.setTime(time);
        c.setOtp(code);
        creds.addCreds(c);
        System.out.println("Credential details is : " + c.toString());

        
        return ResponseEntity.status(HttpStatus.OK).body(code);
    }
    public String generateCode(String type, int len){
        double start = Math.pow(10,len - 1);
        double end = Math.pow(10, len) - 1;
        long code = 0L;
        long lstart = (long) start;
        long lend = (long) end;
        if(type.equals( "numeric")){
            code = ThreadLocalRandom.current().nextLong(lstart,lend);
        }else if(type.equals("alphanumeric")){
            String result= "";
            int i = 0;
            while(i < len) {
                int chance = ThreadLocalRandom.current().nextInt(1,3);
                System.out.println("Chance is " + chance);
                if(chance == 1){
                    result += (char)ThreadLocalRandom.current().nextInt(65,91);
                }else{
                    result += Integer.toString(ThreadLocalRandom.current().nextInt(0,10));
                }
                i++;
            }
            return result;
        }else if(type.equals("alpha")){
            String result = "";
            int i = 0;
            while (i < len) {
                result += (char)ThreadLocalRandom.current().nextInt(65,91);
                i++;
            }
            return result;
        }else{
            throw new IllegalArgumentException("Invalid parameter try 'alpha -> only alphabets' 'numeric -> only numbers' 'alphanumeric -> both'");
        }
        return Long.toString(code);
    }
}
