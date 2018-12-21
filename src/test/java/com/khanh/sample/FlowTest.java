package com.khanh.sample;

import org.junit.Test;

import java.io.File;

public class FlowTest {
    private static final String F365_CSV_PATH = "data" + File.separator + "FOLDER1";
    private static final String NUGGET_PATH = "data" + File.separator + "FOLDER2";
    private static final String FORWARDED_NUGGET_PATH = "data" + File.separator + "FOLDER3";
    private static final String ARCHIVED_PATH = "data" + File.separator + "FOLDER4";
    private static final String F46_CSV_PATH = "data" + File.separator + "FOLDER5";

    @Test
    public void testEndToEndFlow() {
        SIM sim = new SIM(F365_CSV_PATH);
        Validation validation = new Validation(F365_CSV_PATH, NUGGET_PATH, FORWARDED_NUGGET_PATH, ARCHIVED_PATH, F46_CSV_PATH);
        BRS brs = new BRS(F365_CSV_PATH, NUGGET_PATH);
        DMP dmp = new DMP(NUGGET_PATH, FORWARDED_NUGGET_PATH, ARCHIVED_PATH, F46_CSV_PATH);
        BNP bnp = new BNP(FORWARDED_NUGGET_PATH, F46_CSV_PATH);

        sim.Execute();
        validation.verifyF365CSVFile();

        brs.Execute();
        validation.verifyNuggetFile(NUGGET_PATH);

        dmp.ExecuteNugget();
        validation.verifyNuggetFile(FORWARDED_NUGGET_PATH);
        validation.verifyNuggetFile(ARCHIVED_PATH);

        bnp.Execute();
        validation.verifyF46CSVFile(F46_CSV_PATH);

        dmp.ExecuteF46CSV();
        validation.verifyF46CSVFile(ARCHIVED_PATH);
    }
}
