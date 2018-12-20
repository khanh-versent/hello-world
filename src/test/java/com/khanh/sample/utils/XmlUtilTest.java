package com.khanh.sample.utils;

import com.khanh.sample.models.Customer;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class XmlUtilTest {
    private String fileName = "Customer.xml";

    @Test
    public void testXmlUtilWriteToFile() throws IOException {
        File f = new File(fileName);
        if(f.exists()) {
            f.delete();
        }

        //XmlUtil.WriteToFile(fileName, details);

        f = new File(fileName);
        Assert.assertNotNull(f);
        Assert.assertEquals(true, f.exists());
    }
}