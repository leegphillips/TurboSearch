package com.github.leegphillips.TurboSearch;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;

public class TurboSearchTest {

    private final static String FILE_CONTENTS = "the quick brown fox jumped over the lazy dog";
    private final static int FILES_COUNT = 10;
    private final static String[] EXTENSIONS = {".txt", "", ".xml"};

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
    public void happyPath() {
        assertEquals(new TurboSearch().search(folder.getRoot(), "brown", 0, 20, "").size(), FILES_COUNT);
    }

    @Test
    public void emptyFind() {
        assertEquals(new TurboSearch().search(folder.getRoot(), "tree", 0, 20, "").size(), 0);
    }

    @Test
    public void partitionFind() {
        assertEquals(new TurboSearch().search(folder.getRoot(), "brown", 0, 10, "").size(), 0);
    }

    @Test
    public void happySearchMinPath() {
        assertEquals(new TurboSearch().search(folder.getRoot(), "dog", 0, FILE_CONTENTS.length(), "").size(), 1);
    }

    @Test
    public void suffixIsUsed() {
        assertEquals(new TurboSearch().search(folder.getRoot(), "the", 0, 20, "txt").size(), 4);
    }
}