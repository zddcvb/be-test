package org.example.pong.service;

import org.example.pong.entry.vo.PongResponseVO;
import reactor.core.publisher.Mono;

public interface PongService {
    /**
     * request service according to param
     * @param param args
     * @return  Mono<PongResponseVO> object
     */
    Mono<PongResponseVO> handlerRequest(String param);
}
