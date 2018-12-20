package com.khanh.sample.utils;

import com.khanh.sample.models.Customer;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class CSVUtil {

    public static void WriteToFile(String name, Class objectClass, String[] columns, List data)
            throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

        FileWriter writer = new FileWriter(name);
        WriteToWriter(writer, objectClass, columns, data);
    }


    public static void WriteToWriter(Writer writer, Class objectClass, String[] columns, List data)
            throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {


        ColumnPositionMappingStrategy mappingStrategy =
                new ColumnPositionMappingStrategy();
        mappingStrategy.setType(objectClass);

        // Arrange column name as provided in below array.
        mappingStrategy.setColumnMapping(columns);

        // Creating StatefulBeanToCsv object
        StatefulBeanToCsvBuilder<Customer> builder=
                new StatefulBeanToCsvBuilder(writer);
        StatefulBeanToCsv beanWriter =
                builder.withMappingStrategy(mappingStrategy).build();

        // Write list to StatefulBeanToCsv object
        beanWriter.write(data);

        // closing the writer object
        writer.close();
    }
}
