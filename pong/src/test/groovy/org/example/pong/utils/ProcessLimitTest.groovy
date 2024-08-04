package org.example.pong.utils

import spock.lang.Specification

class ProcessLimitTest extends Specification {
//    def processLimit = new ProcessLimit("d:/file.lock")

    def testProcess() {
        def processLimit = new ProcessLimit("d:/file.lock")
        when:
        processLimit.process();
        then:
        processLimit.processMap.size() == 1
    }

    def testProcessInvalidFilePath() {
        def processLimit = new ProcessLimit("d:/tmp/file.lock")
        when:
        processLimit.process();
        then:
        def throwable = thrown(expectedException)
        throwable.message == errorMessage
        where:
        expectedException | errorMessage
        RuntimeException  | "create file error"
    }
}
