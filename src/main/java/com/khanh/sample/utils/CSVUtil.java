package com.khanh.sample.utils;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.khanh.sample.models.Customer;

import java.io.*;
import java.util.List;

public class CSVUtil {

    public static void WriteToFile(String fileName, Class objectClass, List data) throws IOException {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(objectClass).withHeader();

        ObjectWriter myObjectWriter = mapper.writer(schema);
        File file = new File(fileName);
        FileOutputStream tempFileOutputStream = new FileOutputStream(file);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(tempFileOutputStream, 1024);
        OutputStreamWriter writerOutputStream = new OutputStreamWriter(bufferedOutputStream, "UTF-8");
        myObjectWriter.writeValue(writerOutputStream, data);
    }
}
