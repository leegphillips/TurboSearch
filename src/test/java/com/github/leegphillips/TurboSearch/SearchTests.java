package com.github.leegphillips.TurboSearch;

import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TurboSearchApplication.class)
public class SearchTests {

    private final static String FILE_CONTENTS = "the quick brown fox jumped over the lazy dog";
    private final static int FILES_COUNT = 10;
    private final static String[] EXTENSIONS = {".txt", "", ".xml"};

    @Autowired
    private SearchImpl search;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void createFilesToSearch() throws IOException {
        for (int i = 0; i < FILES_COUNT; i++) {
            File file = folder.newFile(Integer.toString(i) + EXTENSIONS[i % EXTENSIONS.length]);
            Files.write(file.toPath(), FILE_CONTENTS.substring(0, FILE_CONTENTS.length() - (i * 2)).getBytes());
        }
    }

    @Test
    public void happyPath() throws ParseException {
        List<File> result = search.search(new CLIParams(new String[]{"quick", "2", "12"}));
        assertEquals(result.size(), FILES_COUNT);
    }
}
