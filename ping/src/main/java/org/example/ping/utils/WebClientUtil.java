package org.example.ping.utils;

import org.example.ping.entry.PingResponseVO;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.nio.channels.FileLock;

@Component
public class WebClientUtil {
    private static Logger LOGGER = Loggers.getLogger(WebClientUtil.class);
    private final WebClient webClient;
    private String url = "http://127.0.0.1:8080/pong/?param={param}";

    public WebClientUtil(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<PingResponseVO> requestPongService(FileLock fileLock) {
        try {
            Mono<PingResponseVO> pingResponseVOMono = webClient.get().uri(url, "hello")
                    .retrieve().bodyToMono(PingResponseVO.class);
            fileLock.release();
            return pingResponseVOMono;
        } catch (Exception e) {
            throw new RuntimeException("request ping service error");
        }
    }
}
