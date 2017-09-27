package com.github.leegphillips.TurboSearch;

import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CLIParamsTests {
    @Test
    public void happyPath() throws ParseException {
        String toFind = "dog";
        String start = "1";
        String end = "6";
        CLIParams cliParams = new CLIParams(new String[]{"--find", toFind, "--start", start, "--end", end});
        assertEquals(cliParams.getToFind(), toFind);
        assertEquals(cliParams.getStart(), Integer.parseInt(start));
        assertEquals(cliParams.getEnd(), Integer.parseInt(end));
    }

    @Test
    public void startDefaultsToZero() throws ParseException {
        assertEquals(new CLIParams(new String[]{"--find", "dog", "--end", "6"}).getStart(), 0);
    }

    @Test(expected = MissingOptionException.class)
    public void missingToFindThrows() throws ParseException {
        new CLIParams(new String[]{"--end", "6"});
    }

    @Test(expected = MissingOptionException.class)
    public void missingEndThrows() throws ParseException {
        new CLIParams(new String[]{"--find", "dog"});
    }

    @Test(expected = NumberFormatException.class)
    public void endMustBeInteger() throws ParseException {
        new CLIParams(new String[]{"--find", "dog", "--end", "d"});
    }

    @Test(expected = NumberFormatException.class)
    public void ifPresentStartMustBeInteger() throws ParseException {
        new CLIParams(new String[]{"--find", "dog", "--start", "s", "--end", "d"});    }
}
