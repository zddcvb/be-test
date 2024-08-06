package org.example.ping.utils

import org.example.ping.entry.PingResponseVO
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import spock.lang.Specification

import java.nio.channels.FileLock

class WebClientUtilTest extends Specification {
    void setup() {}

    def "request pong service normal"() {
        given:
        def webClient = Mock(WebClient)
        def webClientUtil = new WebClientUtil(webClient)
        def fileLock = Mock(FileLock)
        def vo = new PingResponseVO()
        vo.setCode(200)
        vo.setMsg("success")
        vo.setData("world")
        def monoData = Mono.just(vo)
        and:
        webClient.get() >> Stub(WebClient.RequestHeadersUriSpec) {
            uri("http://127.0.0.1:8080/pong/?param={param}", "hello") >> Stub(WebClient.RequestBodyUriSpec) {
                retrieve() >> Stub(WebClient.ResponseSpec) {
                    bodyToMono(PingResponseVO.class) >> monoData
                }
            }
        }
        when:
        def data = webClientUtil.requestPongService(fileLock)
        then:
        println(data.block())
        monoData.block() == data.block()


    }

    def "request pong service throw exception"() {
        given:
        def webClient = Mock(WebClient)
        def webClientUtil = new WebClientUtil(webClient)
        def fileLock = Mock(FileLock)
        and:
        webClient.get() >> RuntimeException.class
        when:
        webClientUtil.requestPongService(fileLock)
        then:
        def throwable = thrown(expectedException)
        throwable.message == errorMessage
        where:
        expectedException | errorMessage
        RuntimeException  | "request ping service error"
    }
}
