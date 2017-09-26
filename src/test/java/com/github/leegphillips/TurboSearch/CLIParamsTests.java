package com.github.leegphillips.TurboSearch;

import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CLIParamsTests {
    @Test
    public void happyPath() throws ParseException {
        String toFind = "dog";
        String start = "1";
        String end = "6";
        CLIParams cliParams = new CLIParams(new String[]{"-f", toFind, "-s", start, "-e", end});
        assertEquals(cliParams.getToFind(), toFind);
        assertEquals(cliParams.getStart(), Integer.parseInt(start));
        assertEquals(cliParams.getEnd(), Integer.parseInt(end));
    }
}
