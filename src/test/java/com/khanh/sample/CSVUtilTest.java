package com.khanh.sample;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CSVUtilTest {
    @Test
    public void testCSVUtilWrite() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        List customers =  new ArrayList();
        customers.add(new Customer("Khanh", 29));
        customers.add(new Customer("Khanh2", 39));

        String[] columns =  { "Name", "Age" };
        CSVUtil.Write(customers, columns, "Customer.csv");
    }
}