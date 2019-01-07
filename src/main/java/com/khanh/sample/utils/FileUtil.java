package com.khanh.sample.utils;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class FileUtil {

    public static List<File> getNewFiles(String directoryPath, long lastCheck, String extension) {
        List<File> newFiles = new ArrayList<>();
        List<File> allFiles = null;

        try {
            allFiles = getFiles(directoryPath, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (allFiles != null && allFiles.size() > 0) {
            for (File file : allFiles) {
                // We are getting new files only, so if the lastModified starts being older than
                // lastCheck, we should break the iteration
                if (file.lastModified() > lastCheck) {
                    if (file.getName().endsWith(extension)) {
                        newFiles.add(file);
                    }
                } else {
                    break;
                }
            }
        }

        return newFiles;
    }

    public static List<File> getNewFiles(String directoryPath, long lastCheck) throws IOException {
        List<File> newFiles = new ArrayList<>();

        List<File> allFiles = getFiles(directoryPath, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        for (File file : allFiles) {
            if (file.lastModified() > lastCheck) {
                newFiles.add(file);
            } else {
                break;
            }
        }

        return newFiles;
    }

    public static List<File> getFiles(String directoryPath, Comparator<File> comparator) throws IOException {
        File directory = new File(directoryPath);
        if (directory.isFile())
            throw new IOException("This path is not a directory.");

        File[] files = directory.listFiles();

        if (files == null) {
            throw new IOException("ListFiles returns null.");
        }

        List<File> result = new ArrayList<>();

        for (File file : files) {
            if (file.isFile()) {
                result.add(file);
            }
        }

        result.sort(comparator);
        return result;
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
        return deleteDirectory(directory);
    }

    public static boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            if (deleteFilesInDirectory(directory)) {
                return directory.delete();
            }
            return false;
        }
        return true;
    }

    public static boolean deleteFilesInDirectory(File directory) {
        File[] files = directory.listFiles();

        if (files != null) {
            int amount = files.length, count = 0;

            // In order to delete folder, all files in the folder need to be deleted first
            for (File file : files) {
                if (file.isDirectory()) {
                    if (deleteDirectory(file)) {
                        count++;
                    }
                } else {
                    if (file.delete()) {
                        count++;
                    }
                }
            }
            return amount == count;
        }
        return true;
    }

    public static boolean copyFile(File sourceFile, File destinationDirectory) throws IOException {
        if (!destinationDirectory.exists())
            destinationDirectory.mkdirs();

        if (destinationDirectory.isDirectory()) {
            String path = destinationDirectory.getPath() + File.separator + sourceFile.getName();
            Files.copy(sourceFile.toPath(), Path.of(path), StandardCopyOption.REPLACE_EXISTING);
        }
        return true;
    }

    public static String combinePath(String... parts) {
        return String.join(File.separator, parts);
    }
}
