package org.example.ping.utils


import spock.lang.Specification

class SystemUtilTest extends Specification {
    def "test current system is linux"() {
        when:
        def result = SystemUtil.isLinux()
        then:
        result == Boolean.FALSE
    }

    def "test current system is window"(){
        when:
        def result = SystemUtil.isWindows()
        then:
        result==Boolean.TRUE
    }
}