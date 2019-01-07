package com.khanh.sample.utils;

import com.khanh.sample.models.TradeDetails;
import com.khanh.sample.models.TradeMetadata;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class NuggetUtil {

    public static Map.Entry<TradeDetails, TradeMetadata> readNugget(File nuggetFile) {

        TradeDetails details = null;
        TradeMetadata metadata = null;

        try {
            List<String> filePaths = CompressUtil.extractTarFile(nuggetFile);
            for (String extractedFilePath : filePaths) {
                if (extractedFilePath.contains("details"))
                    details = XmlUtil.readFromFile(extractedFilePath, TradeDetails.class);
                else if (extractedFilePath.contains("metadata")) {
                    metadata = XmlUtil.readFromFile(extractedFilePath, TradeMetadata.class);
                }
            }

            if (details != null && metadata != null) {
                return Map.entry(details, metadata);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
