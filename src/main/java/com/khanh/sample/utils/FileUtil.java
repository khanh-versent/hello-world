package com.khanh.sample.utils;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class FileUtil {

    public static List<File> getNewFiles(String directoryPath, long lastCheck) throws IOException {
        List<File> newFiles = new ArrayList<>();

        File[] files = getFiles(directoryPath, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        for (File file : files) {
            if(file.isDirectory()) {
                continue;
            }
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

    public static boolean deleteDirectory(String path) {
        File directory = new File(path);
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
            return directory.delete();
        }
        return true;
    }

    public static boolean copyFile(File source, File destination) throws IOException {
        if(!destination.exists())
            destination.mkdirs();

        if(destination.isDirectory()) {
            String path = destination.getPath() + File.separator + source.getName();
            Files.copy(source.toPath(), Path.of(path), StandardCopyOption.REPLACE_EXISTING);
        } else {
            Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        return true;
    }
}
