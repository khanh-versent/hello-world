package com.khanh.sample;

import com.khanh.sample.models.Trade;
import com.khanh.sample.models.TradeDetails;
import com.khanh.sample.models.TradeMetadata;
import com.khanh.sample.utils.CompressUtil;
import com.khanh.sample.utils.FileUtil;
import com.khanh.sample.utils.XmlUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DMP {
    private String nuggetPath;
    private String forwardedNuggetPath;
    private String archivedNuggetPath;
    private String csvPath;

    private Map<String, Map.Entry<TradeDetails, TradeMetadata>> nuggetData;
    Map<String, List<Trade>> csvData;

    Date lastNuggetCheck;
    Date lastCSVCheck;

    public DMP(String nuggetPath, String forwardedNuggetPath,
               String archivedNuggetPath, String csvPath) {
        this.nuggetPath = nuggetPath;
        this.forwardedNuggetPath = forwardedNuggetPath;
        this.archivedNuggetPath = archivedNuggetPath;
        this.csvPath = csvPath;

        lastNuggetCheck = new Date();
        lastCSVCheck = new Date();

        setNuggetData(new HashMap<>());
    }

    public void executeNugget() {
        processNewNuggetFile();

        forwardNuggets();
    }

    public void forwardNuggets() {
        try {
            for (String nugget : getNuggetData().keySet()) {
                // TODO process data collected from those files

                forwardNuggetFile(nugget, this.forwardedNuggetPath);
                archivedNuggetFile(nugget, this.archivedNuggetPath);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void executeF46CSV() {
        checkNewF46CSVFile();

        for (String csv : csvData.keySet()) {
            // TODO process those files

            archivedF46CSVFile(csv);
        }
    }

    public void processNewNuggetFile() {
        List<File> files = getNewFilesList(this.nuggetPath, this.lastNuggetCheck);
        lastNuggetCheck = new Date();

        for (File nuggetFile : files) {
            try {
                TradeDetails details = null;
                TradeMetadata metadata = null;

                List<String> filePaths = CompressUtil.extractTarFile(nuggetFile, nuggetFile.getParentFile());
                for (String extractedFilePath : filePaths) {
                    if (extractedFilePath.contains("details"))
                        details = XmlUtil.readFromFile(extractedFilePath, TradeDetails.class);
                    else if (extractedFilePath.contains("metadata")) {
                        metadata = XmlUtil.readFromFile(extractedFilePath, TradeMetadata.class);
                    }
                }

                if(details != null && metadata != null) {
                    this.nuggetData.put(nuggetFile.getName(), Map.entry(details, metadata));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void forwardNuggetFile(String source, String destination) throws IOException {
        FileUtil.copyFile(new File(this.nuggetPath + File.separator + source), new File(destination));
    }

    private void archivedNuggetFile(String source, String destination) throws IOException {
        FileUtil.copyFile(new File(this.nuggetPath + File.separator + source), new File(destination));
    }


    public void checkNewF46CSVFile() {

        lastCSVCheck = new Date();
    }

    public boolean archivedF46CSVFile(String filePath) {
        return true;
    }

    private List<File> getNewFilesList(String path, Date lastCheck) {
        try {
            return FileUtil.getNewFiles(path, lastCheck.getTime());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Map<String, Map.Entry<TradeDetails, TradeMetadata>> getNuggetData() {
        return nuggetData;
    }

    public void setNuggetData(Map<String, Map.Entry<TradeDetails, TradeMetadata>> nuggetData) {
        this.nuggetData = nuggetData;
    }
}
