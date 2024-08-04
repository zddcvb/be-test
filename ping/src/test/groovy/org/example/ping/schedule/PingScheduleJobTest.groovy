package org.example.ping.schedule

import org.example.ping.utils.WebClientUtil
import org.mockito.InjectMocks
import org.mockito.Mock
import spock.lang.Specification

class PingScheduleJobTest extends Specification {
    @InjectMocks
    private PingScheduleJob pingScheduleJob;
    @Mock
    private WebClientUtil webClientUtil;

    void setup() {
    }

    def "PingPong"() {
        when:
        pingScheduleJob.pingPong();
        then:
        1 * webClientUtil.requestPongService()
    }
}
