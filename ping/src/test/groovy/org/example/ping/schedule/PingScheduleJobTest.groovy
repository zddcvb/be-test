package org.example.ping.schedule

import org.example.ping.entry.PingResponseVO
import org.example.ping.utils.PingServiceLimit
import reactor.core.publisher.Mono
import spock.lang.Specification

class PingScheduleJobTest extends Specification {

    void setup() {
    }

    def "ping service limit return normal"() {
        given:
        def pingServiceLimit = Mock(PingServiceLimit)
        def pingScheduleJob = new PingScheduleJob(pingServiceLimit)
        def vo = new PingResponseVO()
        vo.setCode(200)
        vo.setMsg("Request sent & Pong Respond")
        vo.setData("world")
        def monoData = Mono.just(vo)
        and:
        pingServiceLimit.tryAcquire() >> monoData
        when:
        pingScheduleJob.pingPong();
        then:
        monoData.block().msg == "Request sent & Pong Respond"
    }

    def "ping service limit return empty"() {
        given:
        def pingServiceLimit = Mock(PingServiceLimit)
        def pingScheduleJob = new PingScheduleJob(pingServiceLimit)
        def monoData = Mono.empty()
        and:
        pingServiceLimit.tryAcquire() >> monoData
        when:
        pingScheduleJob.pingPong();
        then:
        monoData.block() == null
    }
}
