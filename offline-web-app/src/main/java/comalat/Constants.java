package comalat;

import java.nio.file.Paths;

/**
 *
 * @author SyleSakis
 */
public class Constants {
    
    private static final String USER_DIR = System.getProperty("user.home");

    // Source folder
    public static final String SOURCE_FOLDER = Paths.get(USER_DIR, "Dropbox\\comalat-pdf-files").toString();
    // Destination folder for download
    public static final String DESTINATION_DOWNLOAD_FOLDER = Paths.get(USER_DIR, "Dropbox\\comalat-Download-ZIPS").toString();
    // Destination folder for upload
    public static final String DESTINATION_UPLOAD_FOLDER = Paths.get(USER_DIR, "Dropbox\\comalat-Upload-ZIPS").toString();
    
    public static final int BUFFER_SIZE = 4096;
    
    // Elementary folder name
    public static final String ELEMENTARY = "ELEMENTARY";
    // Intermediate folder name
    public static final String INTERMEDIATE = "INTERMEDIATE";
    
    public static final String ZIP_FORMAT = ".zip";
    
    
}
