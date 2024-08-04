package org.example.pong.service.impl


import spock.lang.Specification

class PongServiceImplTest extends Specification {


    def setup() {

    }

    def "test handler request according to common request"() {
        given:
        def pongService = new PongServiceImpl()
        def param = "hello"
        and:
        pongService.linuxLockFile = "/tmp/process_limit.lock"
        pongService.windowsLockFile = "d:/process_limit1.lock"
        when:
        def request = pongService.handlerRequest(param)
        then:
        def block = request.block()
        block.code == 200
        block.msg == "Request sent&Pong Response"
        block.data == "world"
    }

    def "test handler request throw exception"() {
        given:
        def pongService = new PongServiceImpl()
        def param = "hello"
        and:
        pongService.linuxLockFile = "/tmp/process_limit.lock"
        pongService.windowsLockFile = "d:/tmp/process_limit1.lock"
        when:
        pongService.handlerRequest(param)
        then:
        def throwable = thrown(expectedException)
        throwable.message == errorMessage
        where:
        expectedException | errorMessage
        RuntimeException  | "request error"
    }

    def "test handler request param is empty"() {
        given:
        def pongService = new PongServiceImpl()
        def param = ""
        and:
        pongService.linuxLockFile = "/tmp/process_limit.lock"
        pongService.windowsLockFile = "d:/process_limit.lock"
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
        and:
        pongService.linuxLockFile = "/tmp/process_limit.lock"
        pongService.windowsLockFile = "d:/process_limit.lock"
        when:
        def request = pongService.handlerRequest(param)
        then:
        def block = request.block()
        block.code == 400
        block.msg == "REQUEST PARAM IS INVALID,PLEASE CHECK!"
    }

    def "test handler request ratelimit tryaccquire false"() {
        given:
        def pongService = new PongServiceImpl()
        def param = "hello"
        and:
        pongService.linuxLockFile = "/tmp/process_limit.lock"
        pongService.windowsLockFile = "d:/process_limit.lock"
        pongService.rateLimit.setLimit(-1)
        when:
        def request = pongService.handlerRequest(param)
        then:
        def block = request.block()
        block.code == 429
        block.msg == "Request sent & Pong throttled it"
    }

    def "test handler request count lg 2"() {
        given:
        def pongService = new PongServiceImpl()
        def param = "hello"
        and:
        pongService.linuxLockFile = "/tmp/process_limit.lock"
        pongService.windowsLockFile = "d:/process_limit.lock"
        pongService.saveRequestCount.set(5)
        when:
        def request = pongService.handlerRequest(param)
        then:
        def block = request.block()
        block.code == 429
        block.msg == "Request not send as being 'reate limited'"
    }

}

