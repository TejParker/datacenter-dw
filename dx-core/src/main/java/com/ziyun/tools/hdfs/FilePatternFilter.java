package com.ziyun.tools.hdfs;

import java.io.File;
import java.io.FilenameFilter;

public class FilePatternFilter implements FilenameFilter {
    private String filePattern;

    public FilePatternFilter(String filePattern) {
        this.filePattern = filePattern;
    }
    @Override
    public boolean accept(File dir, String name) {
        return name.matches(filePattern);
    }
}
