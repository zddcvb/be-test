package org.example.pong.utils


import spock.lang.Specification

class SystemUtilTest extends Specification {
    def testIsLinux() {
        when:
        def result = SystemUtil.isLinux()
        then:
        result == Boolean.FALSE
    }

    def tesstIsWindow(){
        when:
        def result = SystemUtil.isWindows()
        then:
        result==Boolean.TRUE
    }
}