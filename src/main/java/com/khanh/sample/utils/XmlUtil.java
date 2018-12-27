package com.khanh.sample.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.*;

public class XmlUtil {

    public static void writeToWriter(Writer writer, Object data) throws IOException {
        XmlMapper mapper = new XmlMapper();
        mapper.setDefaultUseWrapper(false);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(writer, data);
    }

    public static void writeToFile(String fileName, Object data) throws IOException {
        FileUtil.checkAndCreateDirectory(fileName);

        FileWriter writer = new FileWriter(fileName);
        writeToWriter(writer, data);
    }

    public static <T> T readFromReader(Reader reader, Class<T> objectClass) throws IOException {
        ObjectMapper objectMapper = new XmlMapper();
        return objectMapper.readValue(reader, objectClass);
    }

    public static <T> T readFromFile(String fileName, Class<T> objectClass) throws IOException {
        FileReader reader = new FileReader(fileName);
        return readFromReader(reader, objectClass);
    }
}
