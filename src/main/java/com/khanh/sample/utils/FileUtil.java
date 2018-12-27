package com.khanh.sample.utils;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.*;

public class FileUtil {

    public static List<File> getNewFiles(String directoryPath, Date lastCheck) throws Exception {
        List<File> newFiles = new ArrayList<>();

        File[] files = getFiles(directoryPath, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        for (File file : files) {
            if (file.lastModified() > lastCheck.getTime()) {
                newFiles.add(file);
            } else {
                break;
            }
        }

        return newFiles;
    }

    public static File[] getFiles(String directoryPath, Comparator<File> comparator) throws Exception {
        File directory = new File(directoryPath);
        if (directory.isFile())
            throw new Exception("This path is not a directory.");

        File[] files = directory.listFiles();
        Arrays.sort(files, comparator);

        return files;
    }

    public static void checkAndCreateDirectory(String filePath) {
        final File file = new File(filePath);
        final File parentDirectory = file.getParentFile();

        if (null != parentDirectory && !parentDirectory.exists()) {
            parentDirectory.mkdirs();
        }
    }
}
