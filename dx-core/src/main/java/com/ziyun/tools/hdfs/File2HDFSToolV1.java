package com.ziyun.tools.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class File2HDFSToolV1 {
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

    public static void main(String[] args) throws IOException {
//        String hadoopConfDir = "";
        String localDIr = "data/src";
        String filePattern = ".*";
        String targetRootDir = "/data/ods/log";
        String targetChildDir = "20240702/2050";

        Configuration conf = new Configuration();
//        conf.addResource(new Path(hadoopConfDir + "/core-site.xml"));
//        conf.addResource(new Path(hadoopConfDir + "/hdfs-site.xml"));
        FileSystem fileSystem = FileSystem.get(conf);

        moveLocalToHDFS(fileSystem, localDIr, filePattern, targetRootDir, targetChildDir);
    }
}
