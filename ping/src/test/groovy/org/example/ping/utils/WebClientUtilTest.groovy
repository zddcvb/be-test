package org.example.ping.utils

import org.example.ping.entry.PingResponseVO
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import spock.lang.Specification

class WebClientUtilTest extends Specification {
    void setup() {
    }

//    def "request pong service normal"() {
//        given:
//        def webClient = WebClient.create()
//        def webClientUtil = new WebClientUtil(webClient)
//        def vo = new PingResponseVO()
//        vo.setCode(200)
//        vo.setMsg("success")
//        vo.setData("world")
//        def monoData = Mono.just(vo)
//        and:
//        webClient.get().uri("http://127.0.0.1:8080/pong/?param={param}", "hello").retrieve().bodyToMono(PingResponseVO.class) >> monoData
//        when:
//        webClientUtil.requestPongService()
//        then:
//        def throwable = thrown(expectedException)
//        throwable.message == errorMessage
//        where:
//        expectedException | errorMessage
//        Exception  | "request ping service error"
//
//    }

    def "request pong service throw exception"() {
        given:
        def webClient = Mock(WebClient)
        def webClientUtil = new WebClientUtil(webClient)
        and:
        webClient.get() >> RuntimeException.class
        when:
        webClientUtil.requestPongService()
        then:
        def throwable = thrown(expectedException)
        throwable.message == errorMessage
        where:
        expectedException | errorMessage
        RuntimeException  | "request ping service error"
    }
}
