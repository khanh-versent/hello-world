package com.khanh.sample.utils;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class FileUtilTest {

    @Test
    public void getNewFiles() {
    }

    @Test
    public void getFiles() throws Exception {
        File f = new File("sub");
        if (null != f && f.exists()) {
            File[] files = f.listFiles();
            for (File file : files) {
                Assert.assertTrue(file.delete());
            }

            Assert.assertTrue(f.delete());
        }

        String[] fileNames = {"sub" + File.separator + "textfile1.txt", "sub" + File.separator + "textfile2.txt", "sub" + File.separator + "textfile3.txt"};
        String[] fileContents = {"textfile1", "textfile2", "textfile3"};

        for (int i = 0; i < fileNames.length; i++) {
            createFile(fileNames[i], fileContents[i]);
            Thread.sleep(100);
        }

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

    private void createFile(String fileName, String content) throws IOException {
        FileUtil.checkAndCreateDirectory(fileName);

        PrintWriter writer = new PrintWriter(fileName, StandardCharsets.UTF_8);
        writer.println(content);
        writer.close();

        File file = new File(fileName);
        Assert.assertTrue(file.exists());
    }
}