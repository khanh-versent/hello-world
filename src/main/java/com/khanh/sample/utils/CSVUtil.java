package com.khanh.sample.utils;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.*;
import java.util.List;

public class CSVUtil {

    public static void writeToFile(String fileName, Class objectClass, List data) throws IOException {
        checkDirectory(fileName);

        FileWriter fileWriter = new FileWriter(fileName);
        writeToWriter(fileWriter, objectClass, data);
    }

    public static void writeToWriter(Writer writer, Class objectClass, List data) throws IOException {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(objectClass).withHeader();

        mapper.writer(schema).writeValues(writer).writeAll(data);
    }

    public static <T> List<T> readFromFile(String fileName, Class objectClass) throws IOException {
        FileReader fileReader = new FileReader(fileName);
        return readFromReader(fileReader, objectClass);
    }

    public static <T> List<T> readFromReader(Reader reader, Class objectClass) throws  IOException {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = csvMapper.typedSchemaFor(objectClass).withHeader();

        List<T> result = csvMapper.readerFor(objectClass).with(csvSchema)
                .<T>readValues(reader)
                .readAll();
        return result;
    }

    private static void checkDirectory(String fileName) {
        final File file = new File(fileName);
        final File parent_directory = file.getParentFile();

        if (null != parent_directory)
        {
            parent_directory.mkdirs();
        }
    }
}
