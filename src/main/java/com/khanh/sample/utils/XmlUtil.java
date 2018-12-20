package com.khanh.sample.utils;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class XmlUtil {

    public static void WriteToWriter(Writer writer, Object data) throws IOException {
        XmlMapper mapper = new XmlMapper();
        //mapper.setDefaultUseWrapper(false);
        mapper.writeValue(writer, data);
    }

    public static void WriteToFile(String name, Object data) throws IOException {
        FileWriter writer = new FileWriter(name);
        WriteToWriter(writer, data);
    }
}
