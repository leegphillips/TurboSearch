package com.github.leegphillips.TurboSearch;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class Runner implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class);

    @Override
    public void run(String... args) throws ParseException {
        LOGGER.info("Application starting");

        List<File> results = new SearchImpl().search(new CLIParams(args));
    }
}
