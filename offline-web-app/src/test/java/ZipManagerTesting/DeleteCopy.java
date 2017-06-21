package ZipManagerTesting;

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
public class DeleteCopy {
    
    public DeleteCopy() {
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
    public void copy(){
        String sourceFolder = Paths.get(Constants.DESTINATION_UPLOAD_FOLDER,"English").toString();
        String destFolder = Paths.get(Constants.SOURCE_FOLDER, "English").toString();
        
        FolderManager.copy(sourceFolder, destFolder);
    }
    
    //@Test
    public void delete(){
        String source = Paths.get(Constants.SOURCE_FOLDER, "English").toString();
        FolderManager.delete(source);
    }
}
