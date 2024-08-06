package org.example.pong.service.impl;

import org.example.pong.entry.vo.PongResponseVO;
import org.example.pong.service.PongService;
import org.example.pong.utils.RateLimit;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

@Service
public class PongServiceImpl implements PongService {
    static Logger LOGGER = Loggers.getLogger(PongServiceImpl.class);
    RateLimit rateLimit = new RateLimit(1);

    @Override
    public Mono<PongResponseVO> handlerRequest(String param) {
        //每次请求只能处理一个
        if (rateLimit.tryAcquire()) {
            PongResponseVO pongResponseVO = new PongResponseVO();
            if (ObjectUtils.isEmpty(param)) {
                pongResponseVO.setCode(HttpStatus.BAD_REQUEST.value());
                pongResponseVO.setMsg("REQUEST PARAM IS EMPTY!");
            } else if (!"hello".equals(param)) {
                pongResponseVO.setCode(HttpStatus.BAD_REQUEST.value());
                pongResponseVO.setMsg("REQUEST PARAM IS INVALID,PLEASE CHECK!");
            } else {
                pongResponseVO.setCode(HttpStatus.OK.value());
                pongResponseVO.setMsg("Request sent&Pong Response");
                pongResponseVO.setData("world");
            }
            LOGGER.info(pongResponseVO.toString());
            Mono<PongResponseVO> just = Mono.just(pongResponseVO);
            rateLimit.tryRelease();
            return just;
        }
        PongResponseVO pongResponseVO = new PongResponseVO();
        pongResponseVO.setCode(HttpStatus.TOO_MANY_REQUESTS.value());
        pongResponseVO.setMsg("Request sent & Pong throttled it");
        LOGGER.info(pongResponseVO.toString());
        return Mono.just(pongResponseVO);
    }
}
