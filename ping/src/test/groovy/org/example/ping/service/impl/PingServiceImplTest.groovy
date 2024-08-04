package org.example.ping.service.impl

import org.example.ping.entry.PingResponseVO
import org.example.ping.utils.WebClientUtil
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import reactor.core.publisher.Mono
import spock.lang.Specification

class PingServiceImplTest extends Specification {
    @InjectMocks
    private PingServiceImpl pingService;
    @Mock
    private WebClientUtil webClientUtil;

    void setup() {
    }

    def "invoke pong normal"() {
        given:
        def param = "hello"
        def vo = new PingResponseVO();
        vo.setData("world")
        vo.setMsg("success")
        vo.setCode(200)
        def monoVo = Mono.just(vo)
        when:
        Mockito.when(webClientUtil.requestPongService()).thenReturn(monoVo)
        def responseVO = pingService.invokePong(param)
        then:
        responseVO.toString() == monoVo.toString()
    }

    def "invoke pong throw exception"() {
        given:
        def param = "hello"
        when:
        Mockito.when(webClientUtil.requestPongService()).thenThrow(Exception.class)
        def responseVO = pingService.invokePong(param)
        then:
        responseVO == Mono.empty()
    }
}
