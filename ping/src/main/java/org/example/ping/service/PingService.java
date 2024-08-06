package org.example.ping.service;

import org.example.ping.entry.PingResponseVO;
import reactor.core.publisher.Mono;

public interface PingService {
    /**
     * request pong service
     *
     * @param param request param
     * @return Mono<PingResponseVO>
     */
    Mono<PingResponseVO> invokePong(String param);

}
