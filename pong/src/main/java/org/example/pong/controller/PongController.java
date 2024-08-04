package org.example.pong.controller;

import org.example.pong.entry.vo.PongResponseVO;
import org.example.pong.service.PongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/pong")
public class PongController {
    private final PongService pongService;

    @Autowired
    public PongController(PongService pongService) {
        this.pongService = pongService;
    }

    @GetMapping("/")
    public Mono<PongResponseVO> handlerRequest(String param) {
        return pongService.handlerRequest(param);
    }
}
