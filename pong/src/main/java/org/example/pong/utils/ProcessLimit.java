package org.example.pong.utils;

import reactor.util.Logger;
import reactor.util.Loggers;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ProcessLimit {
    private String lockFile;
    private static final int MAX_PROCESS = 2;
    public final AtomicInteger saveRequestCount;
    public final ConcurrentHashMap<Long, Integer> processMap = new ConcurrentHashMap<>();
    private static final Long CURRENT_TIME = System.currentTimeMillis();
    private static Logger LOGGER = Loggers.getLogger(ProcessLimit.class);
    public final AtomicInteger requestCount = new AtomicInteger();

    public ProcessLimit() {

        saveRequestCount = new AtomicInteger(0);
    }

    public synchronized void process(String lockFile) {
        try {
            File file = new File(lockFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            FileChannel channel = randomAccessFile.getChannel();
            FileLock fileLock = channel.tryLock();
            if (fileLock != null) {
                //获取锁成功
                if (requestCount.get() <= MAX_PROCESS) {
                    LOGGER.info("============Not exceeding the maximum number of processes");
                    saveRequestCount.incrementAndGet();
                    processMap.put(CURRENT_TIME, saveRequestCount.get());
                } else {
                    LOGGER.info("============The maximum number of processes has been exceeded");
//                    requestCount.decrementAndGet();
//                    System.exit(0);
//                    return;
                }
                requestCount.incrementAndGet();
//                processMap.put(CURRENT_TIME, requestCount.get());
                fileLock.release();
                fileLock.close();
                randomAccessFile.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("create file error");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
