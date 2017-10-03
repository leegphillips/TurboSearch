package com.github.leegphillips.TurboSearch;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TurboSearchTest {

    private final static String FILE_CONTENTS = "the quick brown fox jumped over the lazy dog";
    private final static int FILES_COUNT = 10;
    private final static String[] EXTENSIONS = {".txt", "", ".xml"};

    private List<File> files;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void createFiles() throws IOException {
        for (int i = 0; i < FILES_COUNT; i++) {
            File test = folder.newFile(i + EXTENSIONS[i % EXTENSIONS.length]);
            Files.write(test.toPath(), FILE_CONTENTS.substring(0, FILE_CONTENTS.length() - (2 * i)).getBytes());
        }
    }

    @Test
    public void happyPath() throws IOException {
        File test = folder.newFile("test");
        Files.write(test.toPath(), FILE_CONTENTS.getBytes());
        assertTrue(new TurboSearch().matches(test, "brown", 0, 20));
    }

    @Test
    public void emptyFind() throws IOException {
        File test = folder.newFile("test");
        Files.write(test.toPath(), FILE_CONTENTS.getBytes());
        assertFalse(new TurboSearch().matches(test, "tree", 0, 20));
    }

    @Test
    public void partitionFind() throws IOException {
        File test = folder.newFile("test");
        Files.write(test.toPath(), FILE_CONTENTS.getBytes());
        assertFalse(new TurboSearch().matches(test, "brown", 0, 10));
    }

    @Test
    public void happySearchPath() throws IOException, ExecutionException, InterruptedException {
        assertEquals(new TurboSearch().search(folder.getRoot(), "the", 0, 20, "").size(), FILES_COUNT);
    }

    @Test
    public void happySearchMinPath() throws IOException, ExecutionException, InterruptedException {
        assertEquals(new TurboSearch().search(folder.getRoot(), "dog", 0, FILE_CONTENTS.length(), "").size(), 1);
    }

    @Test
    public void suffixIsUsed() throws ExecutionException, InterruptedException {
        assertEquals(new TurboSearch().search(folder.getRoot(), "the", 0, 20, "txt").size(), 4);
    }
}