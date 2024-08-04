package org.example.pong.service.impl

import org.example.pong.utils.SystemUtil
import org.mockito.Mockito
import spock.lang.Specification

import java.util.concurrent.ConcurrentHashMap

class PongServiceImplTest extends Specification {
    def pongService = Mock(PongServiceImpl)

    def setup() {
        println "demo"
    }

    def "test handler request according to common request"() {
        given:
        def param = "hello"
        when:
        pongService.linuxLockFile = "/tmp/process_limit.lock"
        Mockito.when(SystemUtil.isLinux()).thenReturn(true)
        def request = pongService.handlerRequest(param)
        then:
        request.subscribe(pongResponseVO -> {
            pongResponseVO.getCode() == 200
            pongResponseVO.getMsg() == "Request sent & Pong throttled it"
        })
    }

    def "test handler request param is empty"() {
        given:
        def param = ""

        when:
        pongService.linuxLockFile = "/tmp/process_limit.lock"
        Mockito.when(SystemUtil.isLinux()).thenReturn(true)
        def rockequest = pongService.handlerRequest(param)
        then:
        request.subscribe(pongResponseVO -> {
            pongResponseVO.getCode() == 400
            pongResponseVO.getMsg() == "REQUEST PARAM IS EMPTY!"
        })
    }

    def "test handler request param not equals hello"() {
        given:
        def param = "haha"
        when:
        pongService.linuxLockFile = "/tmp/process_limit.lock"
        Mockito.when(SystemUtil.isLinux()).thenReturn(true)
        def request = pongService.handlerRequest(param)
        then:
        request.subscribe(pongResponseVO -> {
            pongResponseVO.getCode() == 400
            pongResponseVO.getMsg() == "REQUEST PARAM IS INVALID,PLEASE CHECK!"
        })
    }

    def "test handler request intent >2"() {
        given:
        def param = "hello"
        def processMap = new ConcurrentHashMap<Long, Integer>();
        processMap.put(System.currentTimeMillis(), 3)
        when:
        pongService.processLimit.processMap = processMap
        def request = pongService.handlerRequest(param)
        then:
        request.subscribe(pongResponseVO -> {
            pongResponseVO.getCode() == 429
            pongResponseVO.getMsg() == "Request not send as being 'reate limited"
        })
    }

    def "test handler request isLunx is true"() {
        given:
        def param = "hello"

        when:
        pongService.linuxLockFile = "/tmp/process_limit.lock"
        Mockito.when(SystemUtil.isLinux()).thenReturn(true)
        def request = pongService.handlerRequest(param)
        then:
        request.subscribe(pongResponseVO -> {
            pongResponseVO.getCode() == 200
            pongResponseVO.getMsg() == "Request sent & Pong throttled it"
        })
    }

    def "test handler request isLunx is false and isWindows is true"() {
        given:
        def param = "hello"
        when:
        pongService.windowsLockFile = "d:/process_limit.lock"
        Mockito.when(SystemUtil.isLinux()).thenReturn(false)
        Mockito.when(SystemUtil.isLinux()).thenReturn(true)
        def request = pongService.handlerRequest(param)
        then:
        request.subscribe(pongResponseVO -> {
            pongResponseVO.getCode() == 200
            pongResponseVO.getMsg() == "Request sent & Pong throttled it"
        })
    }
}

