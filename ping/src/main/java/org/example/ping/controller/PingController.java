package org.example.ping.controller;

import org.example.ping.entry.PingResponseVO;
import org.example.ping.service.PingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/ping")
public class PingController {

    private final PingService pingService;

    @Autowired
    public PingController(PingService pingService) {
        this.pingService = pingService;
    }

    @GetMapping("/")
    public Mono<PingResponseVO> invokePong(String param) {
        return pingService.invokePong(param);
    }
}
