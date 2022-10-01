package com.unipi.softeng.repository;

import com.unipi.softeng.model.UserMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserMessageRepo extends JpaRepository<UserMessage, Long> {

}
