package org.example.ping.schedule;

import org.example.ping.entry.PingResponseVO;
import org.example.ping.utils.PingServiceLimit;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

/**
 * @author Dane
 */
@Component
public class PingScheduleJob {

    private PingServiceLimit pingServiceLimit;
    static Logger LOGGER = Loggers.getLogger(PingScheduleJob.class);

    public PingScheduleJob(PingServiceLimit pingServiceLimit) {
        this.pingServiceLimit = pingServiceLimit;
    }

    /**
     * 1 second per excute pong service
     */
    @Scheduled(cron = "0/1 * * * * ?")
    public void pingPong() {
        Mono<PingResponseVO> pingResponseVOMono = pingServiceLimit.tryAcquire();
        PingResponseVO pingResponseVO = pingResponseVOMono.block();
        if (pingResponseVO != null) {
            LOGGER.info(pingResponseVO.getMsg());
        } else {
            LOGGER.info("Request not send as being 'rate limited'");
        }
    }
}
