package com.khanh.sample.utils;

import com.fasterxml.jackson.databind.ObjectWriter;
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

    public static List ReadFromFile(String fileName, Class objectClass) throws IOException {
        FileReader fileReader = new FileReader(fileName);
        return ReadFromReader(fileReader, objectClass);
    }

    public static List ReadFromReader(Reader reader, Class objectClass) throws  IOException {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = csvMapper.typedSchemaFor(objectClass).withHeader();

        List result = csvMapper.readerFor(objectClass)
                .with(csvSchema.withColumnSeparator(CsvSchema.DEFAULT_COLUMN_SEPARATOR))
                .readValues(reader)
                .readAll();
        return result;
    }
}
