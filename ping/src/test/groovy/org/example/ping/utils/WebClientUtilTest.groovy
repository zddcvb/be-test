package org.example.ping.utils

import org.example.ping.entry.PingResponseVO
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import spock.lang.Specification

class WebClientUtilTest extends Specification {
    @InjectMocks
    private WebClientUtil webClientUtil;
    @Mock
    private WebClient webClient;

    void setup() {
    }

    def "request pong service normal"() {
        given:
        def vo = new PingResponseVO()
        vo.setCode(200)
        vo.setMsg("success")
        vo.setData("world")
        def monoData = Mono.just(vo)
        when:
        Mockito.when(webClient.get().uri(any(), any())..retrieve().bodyToMono(PingResponseVO.class)).thenReturn(monoData)
        def requestData = webClientUtil.requestPongService()
        then:
        monoData.toString() == requestData
    }

    def "request pong service throw exception"() {
        when:
        Mockito.when(webClient.get().uri(any(), any())..retrieve().bodyToMono(PingResponseVO.class)).thenThrow(RuntimeException.class)
        webClientUtil.requestPongService()
        then:
        def throwable = thrown(expectedException)
        throwable.message == errorMessage
        where:
        expectedException | errorMessage
        RuntimeException  | "request ping service error"
    }
}
