package comalat.Services.FolderServices;

import comalat.Application.Exception.DataNotFoundException;
import comalat.Constants;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.TERMINATE;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SyleSakis
 */
public class FolderManager {

    /**
     * Delete a folder or file.
     *
     * @param source A String absolute path of folder/file.
     * @return true or false
     * @throws IOException if something go wrong.
     *
     */
    public static boolean delete(String source) {
        Path directory = Paths.get(source);
        Path path = null;
        try {
            path = Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
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
            //IO exception
        }

        return Files.notExists(path);
    }

    /**
     * Copy folder or file.
     *
     * @param source The path of folder/file.
     * @param destination The destination where the folder will be copy.
     * @SubMethods copyDirectory, copyFile.
     * @throws GeneralServerProcessException if something go wrong.
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
     * @param destination Where the file we be created.
     * @param in InputStream.
     * @param filename File's name.
     * @throws DataNotFoundException if folder/file does not exist in the source
     * folder.
     *
     */
    public static void saveUploadedFile(String destination, InputStream in, String filename) {

        OutputStream out = null;
        String pathfile = Paths.get(destination, filename).toString();

        try {
            out = new FileOutputStream(new File(pathfile));

            byte[] buffer = new byte[Constants.BUFFER_SIZE];
            int length;
            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
            out.flush();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FolderManager.class.getName()).log(Level.SEVERE, null, ex);
            // Server service Exception
        } catch (IOException ex) {
            Logger.getLogger(FolderManager.class.getName()).log(Level.SEVERE, null, ex);
            // Server service Exception
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(FolderManager.class.getName()).log(Level.SEVERE, null, ex);
                // Server service Exception
            }
        }
    }

    /**
     * Check if a folder/file exist and return the path.
     *
     * @param source The path of folder/file.
     * @param target The name of folder/file that we are looking for.
     * @return The absolute path of folder/file.
     * @throws DataNotFoundException if folder/file does not exist in the source
     *
     */
    public static final String getPath(String source, String target) {
        File directory = new File(source);
        if (directory.exists()) {
            for (String filename : directory.list()) {
                if (filename.toUpperCase().equals(target.toUpperCase())) {
                    //System.out.println("-----------PATH---------------- " + Paths.get(source, filename).toString());
                    //System.out.println("---FILENAME------- " + filename + " |");
                    return Paths.get(source, filename).toString();
                }
            }
        }
        throw new DataNotFoundException("Can not find folder/file " + "{" + target + "}");
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
        File directory = new File(source);
        if (directory.exists()) {
            for (String filename : directory.list()) {
                if (filename.toUpperCase().equals(target.toUpperCase())) {
                    return true;
                }
            }
        }
        return false;
    }
}
