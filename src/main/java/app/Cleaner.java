package app;

import command.downloader.Downloader;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Cleaner {
    private final long period = 30; // 30 Minutes

    public Cleaner() {
        var executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::removeOldFiles, period, period, TimeUnit.MINUTES);
    }

    @SneakyThrows
    private void removeOldFiles() {
        Files.walk(Path.of(Downloader.savePath)).filter(Files::isRegularFile)
                .filter(this::isOld).forEach(this::removeFile);
    }

    @SneakyThrows
    private void removeFile(Path path) {
        Files.delete(path);
    }

    @SneakyThrows
    private boolean isOld(Path path) {
        var createdTime = Files.readAttributes(path, BasicFileAttributes.class)
                .creationTime().to(TimeUnit.MINUTES);
        var systemTime = TimeUnit.MINUTES.convert(
                System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        return systemTime - createdTime > period;
    }
}
