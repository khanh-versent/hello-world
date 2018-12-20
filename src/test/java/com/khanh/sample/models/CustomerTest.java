package com.khanh.sample.models;

import org.junit.Assert;
import org.junit.Test;

public class CustomerTest {
    @Test
    public void testCustomerConstructor() {

        Customer customer = new Customer("Customer1", 10);

        Assert.assertEquals("Customer1", customer.getName());
        Assert.assertEquals(10, customer.getAge());
        Assert.assertEquals(0, customer.getCredit(), 0.000001);
    }
}
