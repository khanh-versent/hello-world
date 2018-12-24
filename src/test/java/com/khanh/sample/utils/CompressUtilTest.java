package com.khanh.sample.utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;


public class CompressUtilTest {
    int fileCount = 3;
    String[] fileNames = {"textfile1.txt", "textfile2.txt", "sub/textfile3.txt"};
    String[] fileContents = {"textfile1", "textfile2", "textfile3"};
    String compressFile = "compress.tar.gz";

    @Test
    public void testCompressUtilCreateTarFile() throws IOException {

        for(int i = 0; i < fileCount; i++) {
            createFile(fileNames[i], fileContents[i]);
        }

        CompressUtil.createTarFile(compressFile, fileNames);

        File file = new File(compressFile);
        Assert.assertTrue(file.exists());
    }

    @Test
    public void testCompressUtilExtractTarFile() throws IOException {

        for (String fileName : fileNames) {
            File file = new File(fileName);
            if(file.exists()) {
                Assert.assertTrue(file.delete());
            }
        }

        testCompressUtilCreateTarFile();

        CompressUtil.extractTarFile(compressFile, "compress");

        for(int i = 0; i < fileCount; i++) {
            String path = "compress" + File.separator + fileNames[i];
            Assert.assertTrue(new File(path).exists());

            String content = Files.readString(Paths.get(path)).trim();
            Assert.assertEquals(fileContents[i], content);
        }
    }

    private void createFile(String fileName, String content) throws IOException {
        File file = new File(fileName);
        File parentFile = file.getParentFile();
        if(parentFile != null && !parentFile.exists()) {
            Assert.assertTrue(parentFile.mkdirs());
        }

        PrintWriter writer = new PrintWriter(fileName, "UTF-8");
        writer.println(content);
        writer.close();

        file = new File(fileName);
        Assert.assertTrue(file.exists());
    }
}