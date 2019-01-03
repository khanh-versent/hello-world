package com.khanh.sample.cucumber.steps;

import com.khanh.sample.utils.CompressUtil;
import com.khanh.sample.utils.FileUtil;
import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompressUtilSteps {
    Map<String, String> createdFiles = new HashMap<>();

    String tarGzFilePath;

    @Given("I have list of files and their contents with following data")
    public void givenFilePathAndContent(DataTable table) throws IOException {
        List<Map<String, String>> data = table.asMaps(String.class, String.class);
        for(int i = 0; i < data.size(); i++) {
            File file = createFile(data.get(i).get("File path"), data.get(i).get("Content"));
            createdFiles.put(file.getAbsolutePath(), data.get(i).get("Content"));
        }
    }

    @When("CompressUtil compress all file to \"(.*)\" file")
    public void whenCompressToFile(String tarGzFilePath) throws Exception {
        File file = new File(tarGzFilePath);
        if(file.exists()) {
            Assert.assertTrue(file.delete());
        }

        String[] filePaths = new String[createdFiles.size()];
        int index = 0;
        for(String absolutePath : createdFiles.keySet()) {
            filePaths[index++] = absolutePath;
        }

        CompressUtil.createTarFile(tarGzFilePath, filePaths);
    }

    @Then("\"(.*)\" folder has \"(.*)\" file")
    public void thenFolderHasTarGzFile(String directoryPath, String tarGzFileName) {
        tarGzFilePath = directoryPath + File.separator + tarGzFileName;
        File tarGzFile = new File(tarGzFilePath);
        Assert.assertTrue(tarGzFile.exists());
    }

    @And("CompressUtil is able to decompress the same structure to \"(.*)\" folder")
    public void andDeCompressFile(String destination) throws IOException {
        CompressUtil.extractTarFile(tarGzFilePath, destination);

        File directory = new File("data" + File.separator + destination);

        // recursively check content of folder
        assertFileContent(directory);
    }

    private void assertFileContent(File file) throws IOException {
        if(file.isDirectory()) {
            File[] files = file.listFiles();
            for (File fileInDirectory : files) {
                assertFileContent(fileInDirectory);
            }
        } else if (file.isFile()) {
            Path path = file.toPath();
            String content = Files.readString(path).trim();
            String absolutePath = file.getAbsolutePath();

            Assert.assertTrue(createdFiles.containsKey(absolutePath));
            Assert.assertEquals(content, createdFiles.get(absolutePath));
        }
    }

    private File createFile(String fileName, String content) throws IOException {
        FileUtil.checkAndCreateDirectory(fileName);

        PrintWriter writer = new PrintWriter(fileName, StandardCharsets.UTF_8);
        writer.println(content);
        writer.close();

        File file = new File(fileName);
        Assert.assertTrue(file.exists());
        return file;
    }
}
