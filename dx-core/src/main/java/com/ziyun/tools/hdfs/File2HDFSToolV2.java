package com.ziyun.tools.hdfs;

import org.apache.commons.cli.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class File2HDFSToolV2 {
    /**
     * 监控本地文件目录，将满足patten的文件上传到HDFS
     * @param fileSystem
     * @param localDir
     * @param filePattern
     * @param targetRootDir
     * @param targetChildDir
     */
    public static void moveLocalToHDFS(FileSystem fileSystem,
                                       String localDir,
                                       String filePattern,
                                       String targetRootDir,
                                       String targetChildDir) throws IOException {
        File dirFiles = new File(localDir);
        File[] files = dirFiles.listFiles(new FilePatternFilter(filePattern));

        Set<File> fileSets = new HashSet<>();
        for (File file : files) {
            String fileDoing = file.getAbsoluteFile() + ".doing";
            if (file.renameTo(new File(fileDoing))) {
                fileSets.add(new File(fileDoing));
            }
        }

        String destDir = targetChildDir + "/" + targetChildDir;
        ensureHDFSDirExists(fileSystem, destDir);
        for (File file : fileSets) {
            fileSystem.moveFromLocalFile(new Path(file.getAbsolutePath()), new Path(destDir));
            String hdfsFileDoing = destDir + "/" + file.getName();
            String hdfsFileDone = hdfsFileDoing.substring(0, hdfsFileDoing.lastIndexOf("."));
            fileSystem.rename(new Path(hdfsFileDoing), new Path(hdfsFileDone));
        }
    }

    public static void ensureHDFSDirExists(FileSystem fileSystem,
                                           String dir) throws IOException {
        if(fileSystem.exists(new Path(dir))) {
            fileSystem.mkdirs(new Path(dir));
        }
    }

    public static void main(String[] args) throws IOException, ParseException {
        Options options = new Options();
        Option hadoopConfOption = new Option("c", "hadoopConfDir", true, "");
        Option localDirfOption = new Option("l", "localDir", true, "");
        Option filePatternOption = new Option("p", "filePattern", true, "");
        filePatternOption.setRequired(false);
        Option targetRootDirOption = new Option("f", "targetRootDir", true, "");
        Option targetChildDirfOption = new Option("s", "targetChildDir", true, "");

        options.addOption(hadoopConfOption);
        options.addOption(localDirfOption);
        options.addOption(filePatternOption);
        options.addOption(targetRootDirOption);
        options.addOption(targetChildDirfOption);

        DefaultParser defaultParser = new DefaultParser();
        CommandLine commandLine = defaultParser.parse(options, args);

        String hadoopConfDir = commandLine.getOptionValue("c");
        String localDir = commandLine.getOptionValue("l");
        String filePattern = ".*";
        if (commandLine.getOptionValue("p") != null) {
            filePattern = commandLine.getOptionValue("p");
        }

        String targetRootDir = commandLine.getOptionValue("f");
        String targetChildDir = commandLine.getOptionValue("s");

        Configuration conf = new Configuration();
//        conf.addResource(new Path(hadoopConfDir + "/core-site.xml"));
//        conf.addResource(new Path(hadoopConfDir + "/hdfs-site.xml"));
        FileSystem fileSystem = FileSystem.get(conf);

        moveLocalToHDFS(fileSystem, localDir, filePattern, targetRootDir, targetChildDir);
    }
}
