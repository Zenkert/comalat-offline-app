package comalat;

import java.nio.file.Paths;

/**
 *
 * @author SyleSakis
 */
public class Constants {
    
    public static final String USER_DIR = System.getProperty("user.home");
    private static final String MAIN_FOLDER = "Comalat Folder";
    private static final String RESOURCES = "comalat Languages";
    private static final String DOWNLOAD = "comalat Download";
    private static final String UPLOAD = "comalat UPLOAD";
    
    // Source folder
    public static final String SOURCE_FOLDER = Paths.get(USER_DIR, MAIN_FOLDER, RESOURCES).toString();
    // Destination folder for download
    public static final String DOWNLOAD_FOLDER = Paths.get(USER_DIR, MAIN_FOLDER, DOWNLOAD).toString();
    // Destination folder for upload
    public static final String UPLOAD_FOLDER = Paths.get(USER_DIR, MAIN_FOLDER, UPLOAD).toString();
    
    public static final int BUFFER_SIZE = 4096;
    
    public static final String ZIP_FORMAT = ".zip";
    public static final String PDF_FORMAT = ".pdf";
    
}
