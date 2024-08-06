package org.example.ping.utils


import reactor.core.publisher.Mono
import spock.lang.Specification

class PingServiceLimitTest extends Specification {
    def setup() {

    }

    def "test try acuquire fileLock1 exist"() {
        given:
        def webClientUtil = Mock(WebClientUtil)
        def pingServiceLimit = new PingServiceLimit(webClientUtil)
        and:
        pingServiceLimit.linuxLockFile = "/tmp/process_limit.lock"
        pingServiceLimit.windowsLockFile = "d:/process_limit.lock"
        when:
        def respData = pingServiceLimit.tryAcquire()
        then:
        respData == null
    }

    def "test try acuquire fileLock1 not exist and fileLock2 exist"() {
        given:
        def webClientUtil = Mock(WebClientUtil)
        def pingServiceLimit = new PingServiceLimit(webClientUtil)
        and:
        pingServiceLimit.linuxLockFile = "/tmp/process_limit.lock"
        pingServiceLimit.windowsLockFile = "d:/tempp/process_limit3.lock"
        pingServiceLimit.linuxLockFile2 = "/tmp/process_limit2.lock"
        pingServiceLimit.windowsLockFile = "d:/process_limit2.lock"
        when:
        def respData = pingServiceLimit.tryAcquire()
        then:
        respData == null
    }

    def "test try acuquire fileLock1 not exist and fileLock2 not exist"() {
        given:
        def webClientUtil = Mock(WebClientUtil)
        def pingServiceLimit = new PingServiceLimit(webClientUtil)
        and:
        pingServiceLimit.linuxLockFile = "/tmp/process_limit.lock"
        pingServiceLimit.windowsLockFile = "d:/temp/process_limit3.lock"
        pingServiceLimit.linuxLockFile2 = "/tmp/process_limit2.lock"
        pingServiceLimit.windowsLockFile2 = "d:/temp/process_limit2.lock"
        when:
        def resp = pingServiceLimit.tryAcquire()
        then:
        resp == Mono.empty()
    }

}
