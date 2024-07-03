package com.ziyun.tools.hdfs;

import org.apache.commons.cli.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class File2HDFSMultiThreadV1 {
    public static void main(String[] args) throws IOException, InterruptedException, ParseException {
        Options options = new Options();
        options.addOption(new Option("c", "hadoopConfDir", true, "Directory for Hadoop configuration files"));
        options.addOption(new Option("l", "localDir", true, "Local directory to scan for files"));
        Option filePattenOption = new Option("p", "filePattern", true, "Pattern to match files (regex)");
        filePattenOption.setRequired(false);
        options.addOption(filePattenOption);
        options.addOption(new Option("f", "targetRootDir", true, "Root directory in HDFS"));
        options.addOption(new Option("s", "targetChildDir", true, "Child directory in HDFS"));

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        String hadoopConfDir = cmd.getOptionValue("c");
        String localDir = cmd.getOptionValue("l");
        String filePattern = cmd.hasOption("p") ? cmd.getOptionValue("p") : ".*";
        String targetRootDir = cmd.getOptionValue("f");
        String targetChildDir = cmd.getOptionValue("s");

        Configuration conf = new Configuration();
        conf.addResource(new Path(hadoopConfDir + "/core-site.xml"));
        conf.addResource(new Path(hadoopConfDir + "/hdfs-site.xml"));
        FileSystem fileSystem = FileSystem.get(conf);

        ExecutorService executor = Executors.newFixedThreadPool(4);  // Adjust thread count based on your system

        File localDirectory = new File(localDir);
        File[] files = localDirectory.listFiles((dir, name) -> name.matches(filePattern));

        if (files != null) {
            for (File file : files) {
                executor.submit(() -> {
                    try {
                        Path srcPath = new Path(file.getAbsolutePath());
                        Path dstPath = new Path(targetRootDir + "/" + targetChildDir + "/" + file.getName());
                        fileSystem.copyFromLocalFile(srcPath, dstPath);
                        System.out.println("Uploaded: " + file.getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }

        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        fileSystem.close();
    }
}
