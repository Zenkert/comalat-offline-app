package comalat.Services.FolderServices;

import comalat.Constants;
import comalat.Application.Exception.ServerProcedureException;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.FileVisitResult;
import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.TERMINATE;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SyleSakis
 */
public class FolderManager {
    
    /**
     * Create main folders.
     *
     * @throws ServerProcedureException if something goes wrong.
     *
     */
    public static void createMainFolders() {
        
        if(!Paths.get(Constants.SOURCE_FOLDER).toFile().exists()){
            Paths.get(Constants.SOURCE_FOLDER).toFile().mkdirs();
        }
        
        if(!Paths.get(Constants.DOWNLOAD_FOLDER).toFile().exists()){
            Paths.get(Constants.DOWNLOAD_FOLDER).toFile().mkdirs();
        }
        
        if(!Paths.get(Constants.UPLOAD_FOLDER).toFile().exists()){
            Paths.get(Constants.UPLOAD_FOLDER).toFile().mkdirs();
        }
        
    }
    
    /**
     * Delete a folder or file.
     *
     * @param source A String absolute path of folder/file.
     * @throws ServerProcedureException if something goes wrong.
     *
     */
    public static void delete(String source) {
        Path directory = Paths.get(source);
        try {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs)
                        throws IOException {
                    Files.delete(file);
                    return CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(final Path file, final IOException e) {
                    return handleException(e);
                }

                private FileVisitResult handleException(final IOException e) {
                    e.printStackTrace(); // replace with more robust error handling
                    return TERMINATE;
                }

                @Override
                public FileVisitResult postVisitDirectory(final Path dir, final IOException e)
                        throws IOException {
                    if (e != null) {
                        return handleException(e);
                    }
                    Files.delete(dir);
                    return CONTINUE;
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(FolderManager.class.getName()).log(Level.SEVERE, null, ex);
            delete(source);
        }
    }

    /**
     * Delete all contents of a folder.
     *
     * @param source A String absolute path of folder.
     * @throws ServerProcedureException if something goes wrong.
     *
     */
    public static boolean deleteAll(String source) {
        File directory = null;
        try {
            directory = new File(source);
        } catch (NullPointerException ex) {
            return false;
        }
        if (directory.listFiles().length > 0) {
            for (File folder : directory.listFiles()) {
                delete(folder.getAbsolutePath());
            }
            return true;
        }
        return false;
    }

    /**
     * Copy folder or file.
     *
     * @param source The path of folder/file.
     * @param destination The destination where the folder will be copy.
     * @SubMethods copyDirectory, copyFile.
     * @throws GeneralServerProcessException if something goes wrong.
     * @throws InformationException if destination folder/file exist.
     *
     */
    public static final void copy(String source, String destination) {
        File sourceFile = new File(source);
        File destFile = new File(destination);

        if (destFile.exists()) {
            // Information error all ready exist the file
        }

        if (!sourceFile.exists()) {
            // General Server Error
        }

        if (sourceFile.isDirectory()) {
            copyDirectory(source, destination);
        } else {
            copyFile(source, destination);
        }

        //delete source folder after copy-paste
    }

    private static final void copyDirectory(String source, String destination) {
        File target = new File(destination);
        File sourceFile = new File(source);
        if (!target.exists()) {
            target.mkdir();
        }

        for (String file : sourceFile.list()) {
            copy(Paths.get(source, file).toString(), Paths.get(destination, file).toString());
        }
    }

    private static final void copyFile(String source, String destination) {
        try (
                InputStream in = new FileInputStream(source);
                OutputStream out = new FileOutputStream(destination);) {
            byte[] buffer = new byte[Constants.BUFFER_SIZE];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        } catch (IOException ex) {
            Logger.getLogger(FolderManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Save file to server.
     *
     * @param in InputStream.
     * @param destination Where the file we be created.
     * @param filename File's name.
     * @throws ServerProcedureException if something goes wrong.
     *
     */
    public static void saveUploadedFile(InputStream in, String destination, String filename) {
        File dir = new File(destination);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File upload = new File(Paths.get(destination, filename).toString());

        try {
            Files.copy(in, upload.toPath());
        } catch (IOException ex) {
            throw new ServerProcedureException("Server procedure error. Please try later!");
        }
    }

    /**
     * Check if a PDF file exist and return the PDF file name.
     *
     * @param source The path of folder/file.
     * @return PDF file name.
     *
     */
    public static String getFileName(String source) {
        File directory = null;
        try {
            directory = new File(source);
        } catch (NullPointerException ex) {
            return null;
        }
        if (directory.exists()) {
            for (File file : directory.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    if (pathname.isFile() && pathname.getName().endsWith(Constants.PDF_FORMAT)) {
                        return true;
                    }
                    return false;
                }
            })) {
                return file.getName();
            }
        }
        return null;
    }

    /**
     * Check if a folder/file exist and return the path.
     *
     * @param source The path of folder/file.
     * @param target The name of folder/file that we are looking for.
     * @return The absolute path of folder/file.
     *
     */
    public static final String getPath(String source, String target) {
        File directory = null;
        try {
            directory = new File(source);
        } catch (NullPointerException ex) {
            return null;
        }
        if (directory.exists()) {
            for (String foldername : directory.list()) {
                if (foldername.equalsIgnoreCase(target)) {
                    return Paths.get(source, foldername).toString();
                }
            }
        }
        return null;
    }

    /**
     * Check if a folder/file exist and return true or false.
     *
     * @param source The path of folder/file.
     * @param target The name of folder/file that we are looking for.
     * @return true if exist, false otherwise.
     *
     */
    public static final boolean exist(String source, String target) {
        File directory = null;
        try {
            directory = new File(source);
        } catch (NullPointerException ex) {
            return false;
        }
        if (directory.exists()) {
            for (String filename : directory.list()) {
                if (filename.equalsIgnoreCase(target)) {
                    return true;
                }
            }
        }
        return false;
    }
}
