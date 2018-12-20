package com.khanh.sample.utils;

import com.khanh.sample.utils.CSVUtil;
import com.khanh.sample.models.Customer;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CSVUtilTest {
    private String fileName = "Customers.csv";

    @Test
    public void testCSVUtilWriteToFile() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        File f = new File(fileName);
        if(f.exists()) {
            f.delete();
        }

        List customers =  new ArrayList();
        customers.add(new Customer("Khanh", 29));
        customers.add(new Customer("Khanh2", 39));

        String[] columns =  { "Name", "Age", "Credit" };
        CSVUtil.WriteToFile(fileName, Customer.class, columns, customers);

        f = new File(fileName);
        Assert.assertNotNull(f);
        Assert.assertEquals(true, f.exists());
    }

    @Test
    public void testCSVUtilWriteToWriter() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {


        File f = new File(fileName);
        if(f.exists()) {
            f.delete();
        }

        List customers =  new ArrayList();
        customers.add(new Customer("Khanh", 29));
        customers.add(new Customer("Khanh2", 39));

        String[] columns =  { "Name", "Age", "Credit" };
        FileWriter writer = new FileWriter(fileName);
        CSVUtil.WriteToWriter(writer, Customer.class, columns, customers);

        f = new File(fileName);
        Assert.assertNotNull(f);
        Assert.assertEquals(true, f.exists());
    }
}