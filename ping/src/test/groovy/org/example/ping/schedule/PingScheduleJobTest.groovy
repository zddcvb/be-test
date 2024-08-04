package org.example.ping.schedule

import org.example.ping.utils.WebClientUtil
import org.mockito.InjectMocks
import org.mockito.Mock
import spock.lang.Specification

class PingScheduleJobTest extends Specification {

    void setup() {
    }

    def "PingPong"() {
        given:
        def webClientUtil=Mock(WebClientUtil)
        def pingScheduleJob=new PingScheduleJob(webClientUtil)
        when:
        pingScheduleJob.pingPong();
        then:
        1 * webClientUtil.requestPongService()
    }
}
