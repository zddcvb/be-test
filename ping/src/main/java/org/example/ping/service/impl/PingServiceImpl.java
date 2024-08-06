package org.example.ping.service.impl;

import org.example.ping.entry.PingResponseVO;
import org.example.ping.service.PingService;
import org.example.ping.utils.PingServiceLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author Dane
 */
@Service
public class PingServiceImpl implements PingService {

    private final PingServiceLimit pingServiceLimit;

    @Autowired
    public PingServiceImpl(PingServiceLimit pingServiceLimit) {
        this.pingServiceLimit = pingServiceLimit;
    }

    /**
     * request pong service ,but only 2 can request pong service
     *
     * @param param request param
     * @return Mono<PingResponseVO>
     */
    @Override
    public Mono<PingResponseVO> invokePong(String param) {
        try {
            return pingServiceLimit.tryAcquire();
        } catch (Exception e) {
            throw new RuntimeException("REQUEST ERROR");
        }
    }
}
