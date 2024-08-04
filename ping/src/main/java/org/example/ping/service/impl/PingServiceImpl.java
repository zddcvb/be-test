package org.example.ping.service.impl;

import org.example.ping.entry.PingResponseVO;
import org.example.ping.service.PingService;
import org.example.ping.utils.WebClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

@Service
public class PingServiceImpl implements PingService {
    private static Logger LOGGER = Loggers.getLogger(PingServiceImpl.class);
    private final WebClientUtil webClientUtil;

    @Autowired
    public PingServiceImpl(WebClientUtil webClientUtil) {
        this.webClientUtil = webClientUtil;
    }

    @Override
    public Mono<PingResponseVO> invokePong(String param) {
        try {
            return webClientUtil.requestPongService();
        } catch (Exception e) {
            throw new RuntimeException("REQUEST ERROR");
        }
    }
}
