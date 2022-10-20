package com.example.demoratelimiter.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.example.demoratelimiter.web.ControllerUtils.doAsync;


import java.util.concurrent.CompletableFuture;

@RestController
public class PingController {

    @GetMapping(path = "/ping", produces = "text/plain")
    public CompletableFuture<ResponseEntity<String>> ping() {
        return doAsync(() -> {
            return ResponseEntity.ok("pong");
        });
    }

    @GetMapping(path = "/pong", produces = "text/plain")
    public CompletableFuture<ResponseEntity<String>> pong() {
        return doAsync(() -> {
            return ResponseEntity.ok("ping");
        });
    }

}
