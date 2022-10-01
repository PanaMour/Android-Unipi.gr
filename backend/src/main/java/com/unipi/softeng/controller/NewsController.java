package com.unipi.softeng.controller;

import com.unipi.softeng.model.News;
import com.unipi.softeng.model.NewsType;
import com.unipi.softeng.model.Response;
import com.unipi.softeng.service.implementation.NewsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static java.time.LocalDateTime.now;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
public class NewsController {

    private final NewsServiceImpl newsService;

    @GetMapping("/")
    public ResponseEntity<Response> getAll() {
        return ResponseEntity.ok(
                Response.builder()
                .timeStamp(now())
                .data(Map.of("news",newsService.findAllNews()))
                .message("Fetched all news")
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build()
        );
    }

    @GetMapping("/page/{offset}/{pageSize}")
    public ResponseEntity<Response> getPaginated(@PathVariable int offset,@PathVariable int pageSize) {
        return ResponseEntity.ok(
                Response.builder()
                .timeStamp(now())
                .data(Map.of("news",newsService.findNews_Paginated(offset,pageSize)))
                .message("Fetched "+pageSize+" from page "+offset+" of news.")
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
                .data(Map.of("news",newsService.findNews(id)))
                .message("Fetched news #"+id)
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build()
        );
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<Response> getByType(@PathVariable String type) {
        return ResponseEntity.ok(
                Response.builder()
                .timeStamp(now())
                .data(Map.of("news",newsService.findAllNewsType(NewsType.valueOf(type))))
                .message("Fetched all news of type: "+type)
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build()
        );
    }

    @GetMapping("/{type}/{offset}/{pageSize}")
    public ResponseEntity<Response> getByTypePaginated(@PathVariable String type, @PathVariable int offset, @PathVariable int pageSize) {
        return ResponseEntity.ok(
                Response.builder()
                .timeStamp(now())
                .data(Map.of("news",newsService.findNewsType_Paginated(NewsType.valueOf(type),offset,pageSize)))
                .message("Fetched "+pageSize+" from page "+offset+" of news of type "+type)
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build()
        );
    }

    @PostMapping("/")
    public ResponseEntity<Response> addNews(@RequestBody News news) {
        return ResponseEntity.ok(
                Response.builder()
                .timeStamp(now())
                .data(Map.of("news",newsService.addNews(news)))
                .message("Added news.")
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteNews(@PathVariable Long id) {
        return ResponseEntity.ok(
                Response.builder()
                .timeStamp(now())
                .message("Deleted news.")
                .data(Map.of("deleted",newsService.deleteNews(id))) //maybe wrong
                .status(HttpStatus.NO_CONTENT)
                .statusCode(HttpStatus.NO_CONTENT.value())
                .build()
        );
    }

    @PutMapping("/")
    public ResponseEntity<Response> editNews(@RequestBody News news) {
        return ResponseEntity.ok(
              Response.builder()
              .timeStamp(now())
              .message("Edited news.")
              .data(Map.of("news",newsService.editNews(news)))
              .status(HttpStatus.OK)
              .statusCode(HttpStatus.OK.value())
              .build()
        );
    }

}
