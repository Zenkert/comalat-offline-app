package FolderManagerTesting;

import comalat.Constants;
import comalat.Services.FolderServices.FolderManager;
import java.nio.file.Paths;
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
public class ManagerFolder {

    public ManagerFolder() {
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
    public void deleteMainFolder() {
        //Main folder
        FolderManager.delete(Constants.SOURCE_FOLDER);

    }
    
    //@Test
    public void deleteSubFolder(){
        //Sub folder English
        String path = Paths.get(Constants.SOURCE_FOLDER, "English2").toString();
        FolderManager.delete(path);
    }

    //@Test
    public void deleteFile() {
        //file1 from English1 elementray
        String path = Paths.get(Constants.SOURCE_FOLDER, "English1", Constants.ELEMENTARY, "UNITS-1-5", "file1.pdf").toString();
        FolderManager.delete(path);
    }
}
