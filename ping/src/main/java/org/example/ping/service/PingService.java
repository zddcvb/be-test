package org.example.ping.service;

import org.example.ping.entry.PingResponseVO;
import reactor.core.publisher.Mono;

public interface PingService {
    Mono<PingResponseVO> invokePong(String param);

}
