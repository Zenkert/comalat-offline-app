package comalat;

import java.nio.file.Paths;

/**
 *
 * @author SyleSakis
 */
public class Constants {
    
    private static final String MAIN_FOLDER = "Dropbox";
    public static final String USER_DIR = System.getProperty("user.home");

    // Source folder
    public static final String SOURCE_FOLDER = Paths.get(USER_DIR, MAIN_FOLDER, "Comalat-Folders", "comalat-pdf-files").toString();
    // Destination folder for download
    public static final String DESTINATION_DOWNLOAD_FOLDER = Paths.get(USER_DIR, MAIN_FOLDER, "Comalat-Folders", "comalat-Download-ZIPS").toString();
    // Destination folder for upload
    public static final String DESTINATION_UPLOAD_FOLDER = Paths.get(USER_DIR, MAIN_FOLDER, "Comalat-Folders", "comalat-Upload-ZIPS").toString();
    
    public static final int BUFFER_SIZE = 4096;
    
    public static final String ZIP_FORMAT = ".zip";
    public static final String PDF_FORMAT = ".pdf";
    
}
