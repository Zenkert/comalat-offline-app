package ZipManagerTesting;

import comalat.Constants;
import comalat.Services.FolderServices.CompressManager;
import comalat.Services.FolderServices.FolderManager;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author SyleSakis
 */
public class Compression {

    public Compression() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void CreateLangZip() {
        // Zip all the languages
        String zipname = "Langs-" + Date.from(Instant.now()).getTime() + Constants.ZIP_FORMAT;
        CompressManager.Compression(Constants.SOURCE_FOLDER, Constants.DESTINATION_DOWNLOAD_FOLDER, zipname);

        //Zip specific language {English}
        zipname = "English-" + Date.from(Instant.now()).getTime() + Constants.ZIP_FORMAT;
        String pathfolder = FolderManager.getPath(Constants.SOURCE_FOLDER, "english");
        CompressManager.Compression(pathfolder, Constants.DESTINATION_DOWNLOAD_FOLDER, zipname);
    }

    //@Test
    public void CreateElementaryZip() {
        // Zip elementary of language {English}
        String zipname = "EngElem-" + Date.from(Instant.now()).getTime() + Constants.ZIP_FORMAT;
        String source = Paths.get(Constants.SOURCE_FOLDER, "English").toString();
        String pathfolder = FolderManager.getPath(source, Constants.ELEMENTARY);
        CompressManager.Compression(pathfolder, Constants.DESTINATION_DOWNLOAD_FOLDER, zipname);

        // Zip elementary units1-5 of language {English}
        zipname = "EngElemU1_5-" + Date.from(Instant.now()).getTime() + Constants.ZIP_FORMAT;
        source = Paths.get(Constants.SOURCE_FOLDER, "English", Constants.ELEMENTARY).toString();
        pathfolder = FolderManager.getPath(source, "UNITS-1-5");
        CompressManager.Compression(pathfolder, Constants.DESTINATION_DOWNLOAD_FOLDER, zipname);

        // Zip elementary units6-10 of language {English}
        zipname = "EngElemU6_10-" + Date.from(Instant.now()).getTime() + Constants.ZIP_FORMAT;
        source = Paths.get(Constants.SOURCE_FOLDER, "English", Constants.ELEMENTARY).toString();
        pathfolder = FolderManager.getPath(source, "UNITS-6-10");
        CompressManager.Compression(pathfolder, Constants.DESTINATION_DOWNLOAD_FOLDER, zipname);
    }

    //@Test
    public void CreateIntermediateZip() throws Exception {
        // Zip intemediate of language {English}
        String zipname = "EngInter-" + Date.from(Instant.now()).getTime() + Constants.ZIP_FORMAT;
        String source = Paths.get(Constants.SOURCE_FOLDER, "English").toString();
        String pathfolder = FolderManager.getPath(source, Constants.INTERMEDIATE);
        CompressManager.Compression(pathfolder, Constants.DESTINATION_DOWNLOAD_FOLDER, zipname);

        // Zip intermediate units1-5 of language {English}
        zipname = "EngInterU1_5-" + Date.from(Instant.now()).getTime() + Constants.ZIP_FORMAT;
        source = Paths.get(Constants.SOURCE_FOLDER, "English", Constants.INTERMEDIATE).toString();
        pathfolder = FolderManager.getPath(source, "UNITS-1-5");
        CompressManager.Compression(pathfolder, Constants.DESTINATION_DOWNLOAD_FOLDER, zipname);

        // Zip elementary units6-10 of language {English}
        zipname = "EngInterU6_10-" + Date.from(Instant.now()).getTime() + Constants.ZIP_FORMAT;
        source = Paths.get(Constants.SOURCE_FOLDER, "English", Constants.INTERMEDIATE).toString();
        pathfolder = FolderManager.getPath(source, "UNITS-6-10");
        CompressManager.Compression(pathfolder, Constants.DESTINATION_DOWNLOAD_FOLDER, zipname);
    }
}
