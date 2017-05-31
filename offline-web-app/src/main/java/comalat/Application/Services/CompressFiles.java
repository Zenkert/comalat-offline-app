package comalat.Application.Services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author SyleSakis
 */
public class CompressFiles {

    public static void compress_folder(String source, String destination) throws Exception {
        ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(destination));
        addFolderTOZip("", source, zip);
        zip.flush();
        zip.close();
    }

    private static void addFileTOZip(String path, String source_file, ZipOutputStream zip) throws Exception {
        File file = new File(source_file);

        if (file.isDirectory()) {
            path += "\\" + file.getName();
            System.out.println("IS DIRECTORY ~~~~~~~ PATH: " + path);
            addFolderTOZip(path, source_file, zip);
        } else {
            System.out.println("IS FILE ~~~~~~~ FILE PATH: " + path + "\\" + file.getName());
            byte[] buffer = new byte[4096];
            int length;
            FileInputStream input = new FileInputStream(file);
            zip.putNextEntry(new ZipEntry(path + "\\" + file.getName()));

            while ((length = input.read(buffer)) > 0) {
                zip.write(buffer, 0, length);
            }
        }
    }

    private static void addFolderTOZip(String path, String folder, ZipOutputStream zip) throws Exception {
        File directory = new File(folder);

        if (path.equals("")) {
            path = directory.getName();
        }

        for (String filename : directory.list()) {
            addFileTOZip(path, folder + "\\" + filename, zip);
        }
    }
}
