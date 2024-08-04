package org.example.ping.schedule;

import org.example.ping.utils.WebClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class PingScheduleJob {

    @Autowired
    private WebClientUtil webClientUtil;


    @Scheduled(cron = "0/1 * * * * ?")
    public void pingPong() {
        webClientUtil.requestPongService();
    }
}
