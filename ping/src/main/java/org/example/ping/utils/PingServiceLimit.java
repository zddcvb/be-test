package org.example.ping.utils;

import org.example.ping.entry.PingResponseVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Objects;

@Component
public class PingServiceLimit {
    @Value("${linux.lock.file}")
    String linuxLockFile;
    @Value("${windows.lock.file}")
    String windowsLockFile;
    @Value("${linux.lock.file2}")
    String linuxLockFile2;
    @Value("${windows.lock.file2}")
    String windowsLockFile2;

    private final WebClientUtil webClientUtil;
    static Logger LOGGER = Loggers.getLogger(PingServiceLimit.class);

    public PingServiceLimit(WebClientUtil webClientUtil) {
        this.webClientUtil = webClientUtil;
    }

    public Mono<PingResponseVO> tryAcquire() {

        FileLock fileLock1 = getFileLock(linuxLockFile, windowsLockFile);
        if (Objects.nonNull(fileLock1)) {
            return webClientUtil.requestPongService(fileLock1);
        } else {
            LOGGER.info("已有服务持有锁");
        }
        FileLock fileLock2 = getFileLock(linuxLockFile2, windowsLockFile2);
        if (Objects.nonNull(fileLock2)) {
            return webClientUtil.requestPongService(fileLock2);
        } else {
            LOGGER.info("已有服务持有锁");
        }
        return Mono.empty();
    }

    public FileLock getFileLock(String linuxLockFile, String windowsLockFile) {
        try {
            String lockFile = SystemUtil.isLinux() ? linuxLockFile : windowsLockFile;
            File file = new File(lockFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            FileChannel channel = randomAccessFile.getChannel();
            return channel.tryLock();
        } catch (Exception e) {
            return null;
        }
    }
}
