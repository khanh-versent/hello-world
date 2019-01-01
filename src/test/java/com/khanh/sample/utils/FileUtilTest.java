package com.khanh.sample.utils;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

public class FileUtilTest {
    String rootPath = "data" + File.separator + "test";

    String[] fileNames = {
            rootPath + File.separator + "textfile1.txt",
            rootPath + File.separator + "textfile2.txt",
            rootPath + File.separator + "textfile3.txt",
            rootPath + File.separator + "sub1" + File.separator + "textfile4.txt",
            rootPath + File.separator + "sub2" + File.separator + "textfile5.txt"};
    String[] fileContents = {"textfile1", "textfile2", "textfile3", "textfile4", "textfile5"};
    int expectedFileInRootPath = 3;

    @Test
    public void testGetNewFiles() throws Exception {
        Date lastCheck = new Date();
        createTestFiles();

        List<File> allFiles = FileUtil.getFiles(rootPath, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        int amount = allFiles.size();
        Assert.assertEquals(expectedFileInRootPath, amount);
        List<File> newFiles = FileUtil.getNewFiles(rootPath, lastCheck.getTime());
        Assert.assertEquals(amount, newFiles.size());

        // assuming the textfile1 is the file that read by last check
        // so this check, we will see textfile2 and textfile3 in this check
        long lastCheck2 = allFiles.get(amount - 1).lastModified();
        newFiles = FileUtil.getNewFiles(rootPath, lastCheck2);
        Assert.assertEquals(amount - 1, newFiles.size());
        Assert.assertEquals("textfile3.txt", newFiles.get(0).getName());
        Assert.assertEquals("textfile2.txt", newFiles.get(1).getName());
    }

    @Test
    public void testGetFiles() throws Exception {
        createTestFiles();

        List<File> files = FileUtil.getFiles(rootPath, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        Assert.assertEquals(expectedFileInRootPath, files.size());

        for (int i = 0; i < expectedFileInRootPath - 1; i++) {
            Assert.assertTrue(files.get(i).lastModified() >= files.get(i + 1).lastModified());
        }

        files = FileUtil.getFiles(rootPath, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
        Assert.assertEquals(expectedFileInRootPath, files.size());

        for (int i = 0; i < expectedFileInRootPath - 1; i++) {
            Assert.assertTrue(files.get(i).lastModified() <= files.get(i + 1).lastModified());
        }
    }

    @Test
    public void testCopyFile() throws Exception {
        String destinationPath = "data" + File.separator + "copy";

        FileUtil.deleteDirectory(destinationPath);

        createTestFiles();

        File destinationDirectory = new File(destinationPath);
        for (int i = 0; i < expectedFileInRootPath; i++) {
            File copyingFile = new File(fileNames[i]);
            Assert.assertTrue(FileUtil.copyFile(copyingFile, destinationDirectory));

            File copiedFile = new File(destinationPath + File.separator + copyingFile.getName());
            Assert.assertTrue(copiedFile.exists());
        }
    }

    @Test
    public void testDeleteDirectory() throws Exception {
        File directory = new File(rootPath);

        createTestFiles();
        Assert.assertTrue(FileUtil.deleteDirectory(directory));

        directory = new File(rootPath);
        Assert.assertFalse(directory.exists());
    }

    @Test
    public void testDeleteFilesInDirectory() throws Exception {
        File directory = new File(rootPath);

        createTestFiles();
        Assert.assertTrue(FileUtil.deleteFilesInDirectory(directory));

        File[] files = directory.listFiles();
        Assert.assertNotNull(files);
        Assert.assertEquals(0, files.length);
    }

    private void createTestFiles() throws Exception {
        FileUtil.deleteDirectory(rootPath);

        for (int i = 0; i < fileNames.length; i++) {
            createFile(fileNames[i], fileContents[i]);
            Thread.sleep(100);
        }
    }

    private void createFile(String fileName, String content) throws IOException {
        FileUtil.checkAndCreateDirectory(fileName);

        PrintWriter writer = new PrintWriter(fileName, StandardCharsets.UTF_8);
        writer.println(content);
        writer.close();

        File file = new File(fileName);
        Assert.assertTrue(file.exists());
    }
}