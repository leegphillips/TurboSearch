package com.github.leegphillips.TurboSearch;

import org.apache.commons.cli.*;

public class CLIParams {

    private static final String F_OPTION = "f";
    private static final String S_OPTION = "s";
    private static final String E_OPTION = "e";

    private final String toFind;
    private final int start;
    private final int end;

    public CLIParams(String... args) throws ParseException {
        Option findOption = Option.builder(F_OPTION)
                .required(true)
                .desc("to find")
                .longOpt("find")
                .build();
        Option startOption = Option.builder(S_OPTION)
                .required(false)
                .desc("start position")
                .longOpt("start")
                .build();
        Option endOption = Option.builder(E_OPTION)
                .required(true)
                .desc("end position")
                .longOpt("end")
                .build();

        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        options.addOption(findOption);
        options.addOption(startOption);
        options.addOption(endOption);

        CommandLine commandLine = parser.parse(options, args);

        toFind = commandLine.getOptionValue(F_OPTION);
        start = Integer.parseInt(commandLine.getOptionValue(S_OPTION));
        end = Integer.parseInt(commandLine.getOptionValue(E_OPTION));
    }

    public String getToFind() {
        return toFind;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
}
