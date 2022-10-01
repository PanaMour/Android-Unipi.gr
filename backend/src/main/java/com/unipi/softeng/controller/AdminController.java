package com.unipi.softeng.controller;

import com.unipi.softeng.model.Admin;
import com.unipi.softeng.model.Response;
import com.unipi.softeng.service.implementation.AdminServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminServiceImpl adminService;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody Admin admin) {
        if(adminService.register(admin)) {
            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("REGISTERED",Boolean.TRUE))
                            .message("Admin registered")
                            .status(HttpStatus.CREATED)
                            .statusCode(HttpStatus.CREATED.value())
                            .build()
            );
        } else {
            return ResponseEntity.ok(
                    Response.builder()
                    .timeStamp(LocalDateTime.now())
                    .data(Map.of("REGISTERED",Boolean.FALSE))
                    .message("Unsuccessful register.")
                    .status(HttpStatus.CONFLICT)
                    .statusCode(HttpStatus.CONFLICT.value())
                    .build()
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody Map<String,String> data) {
        String username = data.get("username");
        String password = data.get("password");
        if(adminService.login(username,password)!=null) {
            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("admin",adminService.login(username,password)))
                            .message("Admin log in")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );
        } else {
            return ResponseEntity.ok(
                    Response.builder()
                    .timeStamp(LocalDateTime.now())
                    .message("Unsuccessful login. ")
                    .status(HttpStatus.NOT_FOUND)
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .build()
            );
        }

    }

}
