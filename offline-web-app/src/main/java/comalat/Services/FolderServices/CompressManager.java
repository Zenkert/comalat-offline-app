package comalat.Services.FolderServices;

import comalat.Application.Exception.DataNotFoundException;
import comalat.Constants;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author SyleSakis
 */
public class CompressManager {

    public static void Compression(String source, String destination, String zipfilename) {
        if (Files.notExists(Paths.get(destination))) {
            try {
                Files.createDirectory(Paths.get(destination));
            } catch (IOException ex) {
                Logger.getLogger(CompressManager.class
                        .getName()).log(Level.SEVERE, null, ex);
                // failed to create directory
            }
        }

        ZipOutputStream zip = null;
        try {
            if (Files.exists(Paths.get(source))) {
                zip = new ZipOutputStream(new FileOutputStream(Paths.get(destination, zipfilename).toString()));
                addFolderTOZip("", source, zip);
                zip.flush();
                zip.close();
            } else {
                //throw new NotfoundException
                throw new DataNotFoundException("Source folder does not exist!");
            }
        } catch (IOException ex) {
            Logger.getLogger(CompressManager.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void addFileTOZip(String path, String source_file, ZipOutputStream zip) {
        File file = new File(source_file);

        if (file.isDirectory()) {
            path = Paths.get(path, file.getName()).toString();
            //System.out.println("IS DIRECTORY ~~~~~~~ PATH: " + path);
            addFolderTOZip(path, source_file, zip);
        } else {
            // System.out.println("IS FILE ~~~~~~~ FILE PATH: " + path + "\\" + file.getName());
            byte[] buffer = new byte[Constants.BUFFER_SIZE];
            int length;

            FileInputStream input = null;
            try {
                input = new FileInputStream(file);
                zip.putNextEntry(new ZipEntry(Paths.get(path, file.getName()).toString()));
                while ((length = input.read(buffer)) > 0) {
                    zip.write(buffer, 0, length);

                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(CompressManager.class
                        .getName()).log(Level.SEVERE, null, ex);
                // file not found exception

            } catch (IOException ex) {
                Logger.getLogger(CompressManager.class
                        .getName()).log(Level.SEVERE, null, ex);
                // io exception
            } finally {
                try {
                    input.close();

                } catch (IOException ex) {
                    Logger.getLogger(CompressManager.class
                            .getName()).log(Level.SEVERE, null, ex);

                }
            }
        }
    }

    private static void addFolderTOZip(String path, String folder, ZipOutputStream zip) {
        File directory = new File(folder);

        if (path.equals("")) {
            path = directory.getName();
        }

        for (String filename : directory.list()) {
            addFileTOZip(path, Paths.get(folder, filename).toString(), zip);
        }
    }

    public static void Decompression(String source, String destination, String zipfilename) {
        File zipFile = new File(Paths.get(source, zipfilename).toString());
        if (!zipFile.exists()) {
            System.out.println("Zip file does not exist! throw Exception!");
            // throw new Exception
        }

        File destFolder = new File(destination);
        if (!destFolder.exists()) {
            destFolder.mkdir();
        }

        try {
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {
                String filename = ze.getName();
                String filepath = Paths.get(destination, filename).toString();
                //System.out.println("Path-----> " + filepath + "\nFile name for unzip---> " + filename);
                if (!ze.isDirectory()) {
                    File tmp = new File(filepath).getParentFile();
                    if(!tmp.exists()){
                        tmp.mkdirs();
                    }
                    extractFile(zis, filepath);
                } else {
                    File dir = new File(filepath);
                    dir.mkdir();
                }
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            zis.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(CompressManager.class.getName()).log(Level.SEVERE, null, ex);
            // not found exception
        } catch (IOException ex) {
            Logger.getLogger(CompressManager.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            // Delete zip file after unzip
            FolderManager.delete(zipFile.getPath());
        }
    }

    private static void extractFile(ZipInputStream zis, String filepath) {

        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filepath));
            byte[] buffer = new byte[Constants.BUFFER_SIZE];
            int read = 0;
            while ((read = zis.read(buffer)) != -1) {
                bos.write(buffer, 0, read);
            }
            bos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CompressManager.class.getName()).log(Level.SEVERE, null, ex);
            // notfound exception
        } catch (IOException ex) {
            Logger.getLogger(CompressManager.class.getName()).log(Level.SEVERE, null, ex);
            // io exception
        }
    }

}
