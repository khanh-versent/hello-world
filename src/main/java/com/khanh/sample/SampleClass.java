package com.khanh.sample;

import java.util.ArrayList;
import java.util.List;

public class SampleClass {

    public static void main(String[] args) {

    }

    public static List GetCustomers() {
        List result =  new ArrayList();
        result.add(new Customer("Khanh", 29));
        result.add(new Customer("Khanh2", 39));
        return result;
    }

    public static void WriteToCSV(List data) {
        try {
            String[] columns =  { "Name", "Age" };
            CSVUtil.Write(data, columns, "Customer.csv");
        }
        catch (Exception ex) {

        }
    }
}
