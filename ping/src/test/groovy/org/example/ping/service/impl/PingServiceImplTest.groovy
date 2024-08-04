package org.example.ping.service.impl

import org.example.ping.entry.PingResponseVO
import org.example.ping.utils.WebClientUtil
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import reactor.core.publisher.Mono
import spock.lang.Specification

class PingServiceImplTest extends Specification {

    void setup() {

    }


    def "invoke pong normal"() {
        given:
        def webClientUtil = Mock(WebClientUtil)
        def pingService = new PingServiceImpl(webClientUtil)
        def param = "hello"
        def vo = new PingResponseVO();
        vo.setData("world")
        vo.setMsg("success")
        vo.setCode(200)
        def monoVo = Mono.just(vo)
        and:
        webClientUtil.requestPongService() >> monoVo
        when:
        def responseVO = pingService.invokePong(param)
        then:
        responseVO.toString() == monoVo.toString()
    }

    def "invoke pong throw exception"() {
        given:
        def webClientUtil = Mock(WebClientUtil)
        def pingService = new PingServiceImpl(webClientUtil)
        def param = "hello"
        webClientUtil.requestPongService() >> RuntimeException.class
        when:
        pingService.invokePong(param)
        then:
        def throwable = thrown(expectedException)
        throwable.message == errorMessage
        where:
        expectedException | errorMessage
        RuntimeException  | "REQUEST ERROR"

    }
}
