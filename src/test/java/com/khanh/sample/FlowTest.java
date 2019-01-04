package com.khanh.sample;

import com.khanh.sample.models.Trade;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FlowTest {
    private static final String F365_CSV_PATH = "data" + File.separator + "FOLDER1";
    private static final String NUGGET_PATH = "data" + File.separator + "FOLDER2";
    private static final String FORWARDED_NUGGET_PATH = "data" + File.separator + "FOLDER3";
    private static final String ARCHIVED_PATH = "data" + File.separator + "FOLDER4";
    private static final String F46_CSV_PATH = "data" + File.separator + "FOLDER5";

    @Test
    public void testEndToEndFlow() {
        SIM sim = new SIM(F365_CSV_PATH);
        Validator validator = new Validator(F365_CSV_PATH, NUGGET_PATH, FORWARDED_NUGGET_PATH, ARCHIVED_PATH, F46_CSV_PATH);
        BRS brs = new BRS(F365_CSV_PATH, NUGGET_PATH);
        DMP dmp = new DMP(NUGGET_PATH, FORWARDED_NUGGET_PATH, ARCHIVED_PATH, F46_CSV_PATH);
        BNP bnp = new BNP(FORWARDED_NUGGET_PATH, F46_CSV_PATH);

        List<Trade> trades = new ArrayList<>();

        sim.setTrades(trades);
        sim.execute();
        validator.verifyF365CSVFile();

        /*brs.execute();
        validator.verifyNuggetFile(NUGGET_PATH);

        dmp.executeNugget();
        validator.verifyNuggetFile(FORWARDED_NUGGET_PATH);
        validator.verifyNuggetFile(ARCHIVED_PATH);

        bnp.execute();
        validator.verifyF46CSVFile(F46_CSV_PATH);

        dmp.executeF46CSV();
        validator.verifyF46CSVFile(ARCHIVED_PATH);*/
    }
}
