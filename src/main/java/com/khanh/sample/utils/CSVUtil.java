package com.khanh.sample.utils;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.*;
import java.util.List;

public class CSVUtil {

    public static void WriteToFile(String fileName, Class objectClass, List data) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName);
        WriteToWriter(fileWriter, objectClass, data);
    }

    public static void WriteToWriter(Writer writer, Class objectClass, List data) throws IOException {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(objectClass).withHeader();

        mapper.writer(schema).writeValues(writer).writeAll(data);
    }

    public static <T> List<T> ReadFromFile(String fileName, Class objectClass) throws IOException {
        FileReader fileReader = new FileReader(fileName);
        return ReadFromReader(fileReader, objectClass);
    }

    public static <T> List<T> ReadFromReader(Reader reader, Class objectClass) throws  IOException {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = csvMapper.typedSchemaFor(objectClass).withHeader();

        List<T> result = csvMapper.readerFor(objectClass).with(csvSchema)
                .<T>readValues(reader)
                .readAll();
        return result;
    }
}
