package org.example.pong.service.impl

import org.example.pong.utils.RateLimit
import spock.lang.Specification

class PongServiceImplTest extends Specification {

    def setup() {

    }

    def "test handler request according to common request"() {
        given:
        def pongService = new PongServiceImpl()
        def param = "hello"
        when:
        def request = pongService.handlerRequest(param)
        then:
        def block = request.block()
        block.code == 200
        block.msg == "Request sent&Pong Response"
        block.data == "world"
    }

    def "test handler request param is empty"() {
        given:
        def pongService = new PongServiceImpl()
        def param = ""
        when:
        def request = pongService.handlerRequest(param)
        then:
        def block = request.block()
        block.code == 400
        block.msg == "REQUEST PARAM IS EMPTY!"
    }

    def "test handler request param not equals hello"() {
        given:
        def pongService = new PongServiceImpl()
        def param = "haha"
        when:
        def request = pongService.handlerRequest(param)
        then:
        def block = request.block()
        block.code == 400
        block.msg == "REQUEST PARAM IS INVALID,PLEASE CHECK!"
    }

    def "test handler request ratelimit tryaccquire false"() {
        given:
        def rateLimit = Mock(RateLimit)
        def pongService = new PongServiceImpl()
        def param = "hello"
        and:
        rateLimit.tryAcquire() >> false
        when:
        def request = pongService.handlerRequest(param)
        then:
        def block = request.block()
        block.code == 429
        block.msg == "Request sent & Pong throttled it"
    }
}

