package com.github.leegphillips.TurboSearch;

import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.stream.Collectors.toList;

public class TurboSearch {

    private static final Logger LOG = LoggerFactory.getLogger(TurboSearch.class);

    private static final ExecutorService POOL = Executors.newFixedThreadPool(4096);

    public List<File> search(File folder, String find, int start, int end, String suffix) throws ExecutionException, InterruptedException {
        LOG.debug("Start");
        final List<CompletableFuture<File>> futures = new ArrayList<>();
        folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                LOG.info("File: " + file.getName());
                if (file.getName().endsWith(suffix))
                    futures.add(CompletableFuture.supplyAsync(() -> contains(file, find, start, end), POOL));
                return false;
            }
        });

        return futures.stream().map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .collect(toList());
    }

    // TBD add proper error handling
    private File contains(File file, String find, int start, int end) {
        try {
            return matches(file, find, start, end) ? file : null;
        } catch (IOException e) {
            return null;
        }
    }

    boolean matches(File file, String find, int start, int end) throws IOException {
        LOG.debug("Matching: " + file.getName());
        int length = end - start;
        byte[] buffer = new byte[length];
        fillBuffer(new RandomAccessFile(file, "r"), buffer, start, end);
        LOG.debug(file.getName() + " " + new String(buffer));
        return new String(buffer).contains(find);
    }

    private void fillBuffer(RandomAccessFile file, byte[] buffer, int start, int length) throws IOException {
        try {
            file.readFully(buffer, start, length);
        } catch (EOFException e) {
            // do nothing - buffer filled to allowable extent
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Stopwatch timer = Stopwatch.createStarted();
        new TurboSearch().search(new File("."), args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]), args[3])
                .stream().forEach(file -> System.out.println(file.getName()));
        LOG.debug("Time taken: " + timer.stop());
        Stopwatch shutdown = Stopwatch.createStarted();
        POOL.shutdown();
        LOG.debug("Shutdown time: " + shutdown.stop());
        System.exit(0);
    }
}
