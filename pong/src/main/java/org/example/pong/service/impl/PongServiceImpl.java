package org.example.pong.service.impl;

import org.example.pong.entry.vo.PongResponseVO;
import org.example.pong.service.PongService;
import org.example.pong.utils.ProcessLimit;
import org.example.pong.utils.RateLimit;
import org.example.pong.utils.SystemUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

@Service
public class PongServiceImpl implements PongService {
    private RateLimit rateLimit = new RateLimit(1);
    @Value("${linux.lock.file}")
    String linuxLockFile;
    @Value("${windows.lock.file}")
    String windowsLockFile;
    ProcessLimit processLimit = new ProcessLimit();

    @Override
    public Mono<PongResponseVO> handlerRequest(String param) {
        long currentTime = System.currentTimeMillis();
        String lockFile;
        if (SystemUtil.isLinux()) {
            lockFile = linuxLockFile;
        } else if (SystemUtil.isWindows()) {
            lockFile = windowsLockFile;
        } else {
            lockFile = "";
        }
        //If multiple processes request, only 2 requests are allowed, and the remaining requests are restricted
        processLimit.process(lockFile);
        if (processLimit.processMap.get(currentTime) <= 2) {
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
                Mono<PongResponseVO> just = Mono.just(pongResponseVO);
                rateLimit.tryRelease();
                return just;
            }
            PongResponseVO pongResponseVO = new PongResponseVO();
            pongResponseVO.setCode(HttpStatus.TOO_MANY_REQUESTS.value());
            pongResponseVO.setMsg("Request sent & Pong throttled it");
            return Mono.just(pongResponseVO);
        }
        PongResponseVO pongResponseVO = new PongResponseVO();
        pongResponseVO.setCode(HttpStatus.TOO_MANY_REQUESTS.value());
        pongResponseVO.setMsg("Request not send as being 'reate limited'");
        return Mono.just(pongResponseVO);
    }
}
