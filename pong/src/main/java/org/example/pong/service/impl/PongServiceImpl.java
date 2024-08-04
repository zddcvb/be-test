package org.example.pong.service.impl;

import org.example.pong.entry.vo.PongResponseVO;
import org.example.pong.service.PongService;
import org.example.pong.utils.ProcessLimit;
import org.example.pong.utils.RateLimit;
import org.example.pong.utils.SystemUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PongServiceImpl implements PongService {
    private static Logger LOGGER = Loggers.getLogger(PongServiceImpl.class);
    private RateLimit rateLimit = new RateLimit(1);
    @Value("${linux.lock.file}")
    String linuxLockFile;
    @Value("${windows.lock.file}")
    String windowsLockFile;
    ProcessLimit processLimit = new ProcessLimit();
    public AtomicInteger saveRequestCount = new AtomicInteger(0);
    private static final int MAX_PROCESS = 2;
     Object o = new Object();

    @Override
    public synchronized Mono<PongResponseVO> handlerRequest(String param) {
        String lockFile = SystemUtil.isLinux() ? linuxLockFile : windowsLockFile;
        RandomAccessFile randomAccessFile = null;
        FileLock fileLock = null;
        try {
            File file = new File(lockFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            randomAccessFile = new RandomAccessFile(file, "rw");
            FileChannel channel = randomAccessFile.getChannel();
            fileLock = channel.tryLock();
            if (fileLock != null) {
                //If multiple processes request, only 2 requests are allowed, and the remaining requests are restricted
                if (saveRequestCount.get() <= MAX_PROCESS) {
                    LOGGER.info("============Not exceeding the maximum number of processes");
                    saveRequestCount.incrementAndGet();
                    //每次请求只能处理一个
                    if (rateLimit.tryAcquire()) {
                        PongResponseVO pongResponseVO = new PongResponseVO();
                        if (ObjectUtils.isEmpty(param)) {
                            pongResponseVO.setCode(HttpStatus.BAD_REQUEST.value());
                            pongResponseVO.setMsg("REQUEST PARAM IS EMPTY!");
                        } else if (!"hello".equals(param)) {
                            pongResponseVO.setCode(HttpStatus.BAD_REQUEST.value());
                            pongResponseVO.setMsg("REQUEST PARAM IS INVALID,PLEASE CHECK!");
                        } else {
                            pongResponseVO.setCode(HttpStatus.OK.value());
                            pongResponseVO.setMsg("Request sent&Pong Response");
                            pongResponseVO.setData("world");
                        }
                        Mono<PongResponseVO> just = Mono.just(pongResponseVO);
                        saveRequestCount.decrementAndGet();
                        rateLimit.tryRelease();
                        return just;
                    }
                    PongResponseVO pongResponseVO = new PongResponseVO();
                    pongResponseVO.setCode(HttpStatus.TOO_MANY_REQUESTS.value());
                    pongResponseVO.setMsg("Request sent & Pong throttled it");
                    saveRequestCount.decrementAndGet();
                    return Mono.just(pongResponseVO);
                } else {
                    LOGGER.info("============The maximum number of processes has been exceeded");
                    PongResponseVO pongResponseVO = new PongResponseVO();
                    pongResponseVO.setCode(HttpStatus.TOO_MANY_REQUESTS.value());
                    pongResponseVO.setMsg("Request not send as being 'reate limited'");
                    saveRequestCount.incrementAndGet();
                    return Mono.just(pongResponseVO);
                }
            }
            return Mono.empty();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("request error");
        } finally {
            try {
                if (fileLock != null) {
                    fileLock.release();
                    fileLock.close();
                }
                if (randomAccessFile != null) {
                    randomAccessFile.close();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
