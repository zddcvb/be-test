package org.example.ping.utils;

import org.example.ping.entry.PingResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

@Component
public class WebClientUtil {
    private static Logger LOGGER = Loggers.getLogger(WebClientUtil.class);
    @Autowired
    private WebClient webClient;

    public Mono<PingResponseVO> requestPongService() {
        try {
            Mono<PingResponseVO> pingResponseVOMono = webClient.get().uri("http://127.0.0.1:8080/pong/?param={param}", "hello")
                    .retrieve().bodyToMono(PingResponseVO.class);
            pingResponseVOMono.subscribe(pingResponseVO -> {
                LOGGER.info(ObjectUtils.isEmpty(pingResponseVO.toString()) ? "empty" : pingResponseVO.toString());
                LOGGER.info(pingResponseVO.getMsg());
            });
            return pingResponseVOMono;
        } catch (Exception e) {
            throw new RuntimeException("request ping service error");
        }

    }
}
