package com.unipi.softeng.service.implementation;

import com.unipi.softeng.model.Admin;
import com.unipi.softeng.repository.AdminRepo;
import com.unipi.softeng.security.Encryption;
import com.unipi.softeng.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.SecureRandom;

@RequiredArgsConstructor @Service @Transactional @Slf4j
public class AdminServiceImpl implements AdminService {

    private final AdminRepo adminRepo;

    @Override
    public Boolean register(Admin admin) {
        Admin found = adminRepo.findByUsername(admin.getUsername());
        if (found!=null) {
            log.error("Admin with this username already exists! Registration failed");
            return Boolean.FALSE;
        } else {
            SecureRandom random = new SecureRandom();
            byte[] bytes = new byte[20];
            random.nextBytes(bytes);
            admin.setPassword(Encryption.getHashMD5(admin.getPassword(),random.toString()));
            adminRepo.save(admin);
            return Boolean.TRUE;
        }
    }

    @Override
    public Admin login(String username, String password) {
        Admin found = adminRepo.findByUsername(username);
        if(found!=null) {
            SecureRandom random = new SecureRandom();
            byte[] bytes = new byte[20];
            random.nextBytes(bytes);
            password = Encryption.getHashMD5(password,random.toString());

            if(found.getPassword().equals(password)) {
                log.info("Successful admin login.");
                return found;
            } else {
                log.error("Incorrect password, login failed.");
                return null;
            }
        } else {
            log.error("Admin not found. Login failed.");
            return null;
        }
    }

}
