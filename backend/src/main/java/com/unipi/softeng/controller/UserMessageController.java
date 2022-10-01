package com.unipi.softeng.controller;

import com.unipi.softeng.model.Response;
import com.unipi.softeng.model.UserMessage;
import com.unipi.softeng.service.implementation.UserMessageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static java.time.LocalDateTime.now;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class UserMessageController {

    private final UserMessageServiceImpl userMessageService;

    @GetMapping("/")
    public ResponseEntity<Response> getAll() {
        return ResponseEntity.ok(
                Response.builder()
                .timeStamp(now())
                .data(Map.of("user_messages",userMessageService.findAllUserMessages()))
                .message("Fetched all user messages")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build()
        );
    }

    @GetMapping("/page/{offset}/{pageSize}")
    public ResponseEntity<Response> getPaginated(@PathVariable int offset, @PathVariable int pageSize) {
        return ResponseEntity.ok(
                Response.builder()
                .timeStamp(now())
                .data(Map.of("user_messages",userMessageService.findMessages_Paginated(offset,pageSize)))
                .message("Fetched "+pageSize+" from page "+offset+" of user messages.")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("user_messages",userMessageService.findUserMessage(id)))
                        .message("Fetched user message #"+id)
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping("/")
    public ResponseEntity<Response> addUserMessage(@RequestBody UserMessage userMessage) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("user_message",userMessageService.addUserMessage(userMessage)))
                        .message("Added user message.")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteUserMessage(@PathVariable Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .message("Deleted user message.")
                        .data(Map.of("deleted",userMessageService.deleteUserMessage(id)))   //maybe wrong
                        .status(HttpStatus.NO_CONTENT)
                        .statusCode(HttpStatus.NO_CONTENT.value())
                        .build()
        );
    }

}
