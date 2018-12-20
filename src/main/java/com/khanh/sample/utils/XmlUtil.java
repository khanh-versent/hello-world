package com.khanh.sample.utils;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class XmlUtil {

    public static void WriteToWriter(Writer writer, Object data) throws IOException {
        XmlMapper mapper = new XmlMapper();
        mapper.setDefaultUseWrapper(false);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(writer, data);
    }

    public static void WriteToFile(String fileName, Object data) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        WriteToWriter(writer, data);
    }
}
