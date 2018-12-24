package com.khanh.sample.utils;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.util.zip.GZIPInputStream;
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
        tos.putArchiveEntry(new TarArchiveEntry(file, dir + File.separator + file.toString()));
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
            File[] files = file.listFiles();
            if (files != null) {
                for (File cFile : files) {
                    // recursively call the method for all the subfolders
                    addFilesToTarGZ(tos, cFile, file.getName());
                }
            }
        }
    }

    static void extractTarFile(String fileName, String destination) throws IOException {
        File tarGzFile = new File(fileName);
        String tarFilePath = getTarFileName(tarGzFile);
        File tarFile = new File(tarFilePath);
        tarFile = deCompressGZipFile(tarGzFile, tarFile);

        File destFile = new File(destination);
        if (!destFile.exists()) {
            destFile.mkdir();
        }
        // Calling method to untar file
        unTarFile(tarFile, destFile);
        tarFile.delete();
    }

    static void unTarFile(File tarFile, File destFile) throws IOException {
        FileInputStream fis = new FileInputStream(tarFile);
        TarArchiveInputStream tis = new TarArchiveInputStream(fis);
        TarArchiveEntry tarEntry = null;

        // tarIn is a TarArchiveInputStream
        while ((tarEntry = tis.getNextTarEntry()) != null) {
            File outputFile = new File(destFile + File.separator + tarEntry.getName());

            if (tarEntry.isDirectory()) {
                if (!outputFile.exists()) {
                    outputFile.mkdirs();
                }
            } else {
                outputFile.getParentFile().mkdirs();
                FileOutputStream fos = new FileOutputStream(outputFile);
                IOUtils.copy(tis, fos);
                fos.close();
            }
        }
        tis.close();
    }

    static File deCompressGZipFile(File gZippedFile, File tarFile) throws IOException {
        FileInputStream fis = new FileInputStream(gZippedFile);
        GZIPInputStream gZIPInputStream = new GZIPInputStream(fis);

        FileOutputStream fos = new FileOutputStream(tarFile);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = gZIPInputStream.read(buffer)) > 0) {
            fos.write(buffer, 0, len);
        }

        fos.close();
        gZIPInputStream.close();
        return tarFile;

    }

    private static String getTarFileName(File inputFile) {
        String path = inputFile.getAbsolutePath();
        return path.substring(0, path.lastIndexOf('.'));
    }
}
