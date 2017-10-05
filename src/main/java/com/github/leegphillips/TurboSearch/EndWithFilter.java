package com.github.leegphillips.TurboSearch;

import java.io.File;
import java.io.FilenameFilter;

public class EndWithFilter implements FilenameFilter {

    private final String suffix;

    public EndWithFilter(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public boolean accept(File dir, String name) {
        return name.endsWith(suffix);
    }
}
