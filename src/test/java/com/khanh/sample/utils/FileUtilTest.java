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

    String[] fileNames = {
            "sub" + File.separator + "textfile1.txt",
            "sub" + File.separator + "textfile2.txt",
            "sub" + File.separator + "textfile3.txt"};
    String[] fileContents = {"textfile1", "textfile2", "textfile3"};

    @Test
    public void testGetNewFiles() throws Exception {
        Date lastCheck = new Date();
        createTestFiles();

        File[] allFiles = FileUtil.getFiles("sub", LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        int amount = allFiles.length;
        Assert.assertEquals(amount, fileNames.length);
        List<File> newFiles = FileUtil.getNewFiles("sub", lastCheck.getTime());
        Assert.assertEquals(amount, newFiles.size());

        // assuming the textfile1 is the file that read by last check
        // so this check, we will see textfile2 and textfile3 in this check
        long lastCheck2 = allFiles[amount - 1].lastModified();
        newFiles = FileUtil.getNewFiles("sub", lastCheck2);
        Assert.assertEquals(amount - 1, newFiles.size());
        Assert.assertEquals("textfile3.txt", newFiles.get(0).getName());
        Assert.assertEquals("textfile2.txt", newFiles.get(1).getName());
    }

    @Test
    public void testGetFiles() throws Exception {
        createTestFiles();

        File[] files = FileUtil.getFiles("sub", LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        Assert.assertEquals(fileNames.length, files.length);

        for (int i = 0; i < fileNames.length - 1; i++) {
            Assert.assertTrue(files[i].lastModified() >= files[i + 1].lastModified());
        }

        files = FileUtil.getFiles("sub", LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
        Assert.assertEquals(fileNames.length, files.length);

        for (int i = 0; i < fileNames.length - 1; i++) {
            Assert.assertTrue(files[i].lastModified() <= files[i + 1].lastModified());
        }
    }

    private void createTestFiles() throws Exception {
        FileUtil.deleteDirectory("sub");

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