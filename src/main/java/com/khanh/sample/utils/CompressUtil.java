package com.khanh.sample.utils;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.util.zip.GZIPOutputStream;

class CompressUtil {

    static void createTarFile(String fileName, String[] compressingFileNames) {

        TarArchiveOutputStream tarOs = null;
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            GZIPOutputStream gos = new GZIPOutputStream(new BufferedOutputStream(fos));
            tarOs = new TarArchiveOutputStream(gos);

            for (String compressingFile : compressingFileNames) {
                File file = new File(compressingFile);
                addFilesToTarGZ(tarOs, file, ".");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                tarOs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void addFilesToTarGZ(TarArchiveOutputStream tos, File file, String dir) throws IOException {
        // New TarArchiveEntry
        tos.putArchiveEntry(new TarArchiveEntry(file, dir + File.separator + file.getName()));
        if (file.isFile()) {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            // Write content of the file
            IOUtils.copy(bis, tos);
            tos.closeArchiveEntry();
            fis.close();
        } else if (file.isDirectory()) {
            // no need to copy any content since it is
            // a directory, just close the outputstream
            tos.closeArchiveEntry();
            for (File cFile : file.listFiles()) {
                // recursively call the method for all the subfolders
                addFilesToTarGZ(tos, cFile, file.getName());

            }
        }

    }
}
