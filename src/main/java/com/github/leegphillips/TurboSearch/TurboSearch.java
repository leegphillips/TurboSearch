package com.github.leegphillips.TurboSearch;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.util.stream.Collectors.toList;

public class TurboSearch {
    public List<File> search(File folder, String find, int start, int end) throws ExecutionException, InterruptedException {

        List<CompletableFuture<File>> futures = new ArrayList<>();
        for (File file : folder.listFiles()) {
            CompletableFuture<File> future = CompletableFuture
                    .supplyAsync(() -> contains(file, find, start, end));
            futures.add(future);
        }
//
//        CompletableFuture<Void> barrier = CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[]{}));
//        barrier.get();

        return futures.stream().map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .collect(toList());
    }

    private File contains(File file, String find, int start, int end) {
        try {
            return matches(file, find, start, end) ? file : null;
        } catch (IOException e) {
            return null;
        }
    }

    boolean matches(File file, String find, int start, int end) throws IOException {
        int length = end - start;
        char[] buffer = new char[length];
        try (FileReader fileReader = new FileReader(file);
             BufferedReader reader = new BufferedReader(fileReader)) {
            reader.read(buffer, start, length);
            return new String(buffer).contains(find);
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        new TurboSearch().search(new File("."), args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]))
                .stream().forEach(file -> System.out.println(file.getName()));
    }
}
