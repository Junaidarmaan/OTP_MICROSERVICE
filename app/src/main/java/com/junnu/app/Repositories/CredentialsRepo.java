package com.junnu.app.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.junnu.app.Models.Credentials;

@Repository
public interface CredentialsRepo extends JpaRepository<Credentials,String> {
    Optional<Credentials> findByOtp(String otp);   
    Optional<Credentials> findByMail(String mail);
}
