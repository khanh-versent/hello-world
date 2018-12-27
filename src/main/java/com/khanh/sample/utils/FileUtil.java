package com.khanh.sample.utils;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FileUtil {

    public static List<File> getNewFiles(String directoryPath, long lastCheck) throws IOException {
        List<File> newFiles = new ArrayList<>();

        File[] files = getFiles(directoryPath, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        for (File file : files) {
            if (file.lastModified() > lastCheck) {
                newFiles.add(file);
            } else {
                break;
            }
        }

        return newFiles;
    }

    public static File[] getFiles(String directoryPath, Comparator<File> comparator) throws IOException {
        File directory = new File(directoryPath);
        if (directory.isFile())
            throw new IOException("This path is not a directory.");

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

    public static void deleteDirectory(String path) {
        File f = new File(path);
        if (null != f && f.exists()) {
            File[] files = f.listFiles();
            if(files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
            f.delete();
        }
    }
}
