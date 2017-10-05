package com.github.leegphillips.TurboSearch;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class EndWithFilterTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testPositive() throws IOException {
        String value = "value";
        File test = folder.newFile("start" + value);
        EndWithFilter endWithFilter = new EndWithFilter(value);
        assertEquals(folder.getRoot().listFiles(endWithFilter)[0], test);
    }

    @Test
    public void testNegative() throws IOException {
        String value = "value";
        File test = folder.newFile("start");
        EndWithFilter endWithFilter = new EndWithFilter(value);
        assertEquals(folder.getRoot().listFiles(endWithFilter).length, 0);
    }



}
