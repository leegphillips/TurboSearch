package com.github.leegphillips.TurboSearch;

import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

import static java.util.stream.Collectors.toList;

public class TurboSearch {

    private static final Logger LOG = LoggerFactory.getLogger(TurboSearch.class);

    private static final ExecutorService POOL = Executors.newFixedThreadPool(256);

    public List<File> search(File folder, String find, int start, int end, String suffix) {
        LOG.trace("Start");
        final List<CompletableFuture<File>> futures = new ArrayList<>();
        folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                LOG.trace("File: " + file.getName());
                if (file.getName().endsWith(suffix))
                    futures.add(CompletableFuture.supplyAsync(() -> contains(file, find, start, end), POOL));
                return false;
            }
        });

        return futures.stream().map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .collect(toList());
    }

    private File contains(File file, String find, int start, int end) {
        try {
            return matches(file, find, start, end) ? file : null;
        } catch (IOException e) {
            throw new CompletionException(e);
        }
    }

    private boolean matches(File file, String find, int start, int end) throws IOException {
        LOG.trace("Matching: " + file.getName());
        int length = end - start;
        byte[] buffer = new byte[length];
        fillBuffer(new RandomAccessFile(file, "r"), buffer, start, end);
        LOG.trace(file.getName() + " " + new String(buffer));
        if (new String(buffer).contains(find)) {
            System.out.println(file.getName());
            return true;
        }
        return false;
    }

    private void fillBuffer(RandomAccessFile file, byte[] buffer, int start, int length) throws IOException {
        try {
            file.readFully(buffer, start, length);
        } catch (EOFException e) {
            // do nothing - buffer filled to allowable extent
        }
    }

    public static void main(String[] args) {
        Stopwatch timer = Stopwatch.createStarted();
        new TurboSearch().search(new File("."), args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]), args[3]);
        LOG.trace("Time taken: " + timer.stop());
        Stopwatch shutdown = Stopwatch.createStarted();
        POOL.shutdown();
        LOG.trace("Shutdown time: " + shutdown.stop());
    }
}
