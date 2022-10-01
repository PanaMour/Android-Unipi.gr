package com.unipi.softeng.repository;

import com.unipi.softeng.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepo extends JpaRepository<Admin, Long> {

    Admin findByUsername(String username);

}
