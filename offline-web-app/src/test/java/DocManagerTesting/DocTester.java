package DocManagerTesting;

import java.io.File;
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
public class DocTester {
    
    public DocTester() {
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

    //@TEST
    public void DocTesting(){
        
        System.out.println("Starting...");
        String filename = "C:\\Users\\SyleSakis\\Dropbox\\"
                + "Comalat-Folders\\comalat-pdf-files\\"
                + "English\\Elementary\\courses-1-5\\CONTENTS UNITS 1-5.docx";
        File file = new File(filename);
        
        WordManager.readTable(file, 5);
        
    }
}
