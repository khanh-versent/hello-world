package com.khanh.sample.utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;


public class CompressUtilTest {
    @Test
    public void testCompressUtilCreateTarFile() throws IOException {
        String[] fileNames = {"textfile1.txt", "textfile2.txt"};

        createFile(fileNames[0], "textfile1");
        createFile(fileNames[1], "textfile2");

        String compressFile = "compress.tar.gz";
        CompressUtil.createTarFile(compressFile, fileNames);

        File file = new File(compressFile);
        Assert.assertEquals(true, file.exists());
    }

    private void createFile(String fileName, String content) throws IOException {
        PrintWriter writer = new PrintWriter(fileName, "UTF-8");
        writer.println(content);
        writer.close();

        File file = new File(fileName);
        Assert.assertEquals(true, file.exists());
    }
}