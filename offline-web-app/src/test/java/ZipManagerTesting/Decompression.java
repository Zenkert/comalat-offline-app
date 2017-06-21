package ZipManagerTesting;

import comalat.Constants;
import comalat.Services.FolderServices.CompressManager;
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
public class Decompression {
    
    public Decompression() {
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
    public void Decompression(){
        String zipfilename = "English.zip";
        CompressManager.Decompression(Constants.DESTINATION_UPLOAD_FOLDER, Constants.DESTINATION_UPLOAD_FOLDER, zipfilename);
    }
}
