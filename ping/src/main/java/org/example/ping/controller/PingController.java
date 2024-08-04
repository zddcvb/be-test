package org.example.ping.controller;

import org.example.ping.entry.PingResponseVO;
import org.example.ping.service.PingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Controller
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
