package org.example.pong.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Rate limit
 */
public class RateLimit {
    private int limit;
    private final AtomicInteger limitCount;

    public RateLimit(int limit) {
        this.limit = limit;
        limitCount = new AtomicInteger(0);
    }

    /**
     * 添加
     *
     * @return
     */
    public synchronized boolean tryAcquire() {
        if (limitCount.get() < limit) {
            limitCount.incrementAndGet();
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 释放
     *
     * @return
     */
    public synchronized boolean tryRelease() {
        if (limitCount.get() > 0) {
            limitCount.decrementAndGet();
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
