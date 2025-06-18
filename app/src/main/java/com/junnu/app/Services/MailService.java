package com.junnu.app.Services;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.junnu.app.DTO.UserData;
import com.junnu.app.Models.Credentials;
import com.junnu.app.Repositories.CredentialsRepo;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;


@Service
public class MailService {
    @Autowired
    private JavaMailSender mail;
    @Autowired
    private CredentialsService creds;
    @Autowired
    private CredentialsRepo repo;
    
    public ResponseEntity<String> sendEMail(UserData  data) throws MessagingException, UnsupportedEncodingException{
        MimeMessage mime = mail.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mime,true);
        helper.setFrom(new InternetAddress("Junnubest@gmail.com",data.getAppName())); 
        helper.setTo(data.getEmail());
        helper.setSubject(data.getSubject());
        helper.setText(data.getContent());
        String code = generateCode(data);
        helper.setText(data.getContent() + code);
        if(data.isAttachmentNeeded()){
            File file = new File(data.getFilePath());
            helper.addAttachment(file.getName(), file);
        }
        mail.send(mime);
        
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
    public String generateCode(UserData data){
        int len = data.getLength();
        String type = data.getType();
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
