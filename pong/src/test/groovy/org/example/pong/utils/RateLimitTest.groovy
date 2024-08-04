package org.example.pong.utils


import spock.lang.Specification

class RateLimitTest extends Specification {
    def rateLimit = new RateLimit(1);

    def testTryAcquireForSingle() {
        when:
        def result = rateLimit.tryAcquire()
        then:
        result == Boolean.TRUE;
    }

    def testTryAcquireForMulti() {
        when:
        def result = false;
        for (i in 0..<5) {
            result = rateLimit.tryAcquire()
        }
        then:
        result == Boolean.FALSE
    }

    def testTryReleaseForSingle() {
        when:
        def result = rateLimit.tryRelease()
        then:
        result == Boolean.FALSE
    }

    def testTryReleaseForMulti() {
        when:
        def result = false;
        for (i in 0..<5) {
            rateLimit.tryAcquire()
        }
        result = rateLimit.tryRelease()
        then:
        result == Boolean.TRUE
    }
}
