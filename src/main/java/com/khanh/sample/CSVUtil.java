package com.khanh.sample;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVUtil {

    public static void Write(List data, String[] columns, String name) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

        FileWriter writer = new FileWriter(name);

        ColumnPositionMappingStrategy mappingStrategy =
                new ColumnPositionMappingStrategy();
        mappingStrategy.setType(Customer.class);

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
