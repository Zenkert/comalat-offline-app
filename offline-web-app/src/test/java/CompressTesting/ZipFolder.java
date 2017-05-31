package CompressTesting;

import comalat.Application.Services.CompressFiles;
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
public class ZipFolder {

    public ZipFolder() {
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
    public void CreateLangZip() throws Exception {

        Date d = new Date();
        String output = "C:\\Users\\SyleSakis\\Dropbox\\comalat-pdf-files";

        String path = "C:\\Users\\SyleSakis\\Dropbox\\comalat-pdf-files"
                + "\\English";

        output += "\\English_";
        CompressFiles.compress_folder(path, output + d.getTime() + ".zip");
    }

    @Test
    public void CreateElementaryZip() throws Exception {
        Date d = new Date();
        String output = "C:\\Users\\SyleSakis\\Dropbox\\comalat-pdf-files";

        String path1 = "C:\\Users\\SyleSakis\\Dropbox\\comalat-pdf-files"
                + "\\English\\ELEMENTARY\\UNITS-1-5";

        String output1 = output + "\\ELEMENTARY_1_5_";
        CompressFiles.compress_folder(path1, output1 + d.getTime() + ".zip");

        String path2 = "C:\\Users\\SyleSakis\\Dropbox\\comalat-pdf-files"
                + "\\English\\ELEMENTARY\\UNITS-6-10";

        String output2 = output + "\\ELEMENTARY_6_10_";
        CompressFiles.compress_folder(path2, output2 + d.getTime() + ".zip");
    }
    
    @Test
    public void CreateIntermediateZip() throws Exception {
        Date d = new Date();
        String output = "C:\\Users\\SyleSakis\\Dropbox\\comalat-pdf-files";

        String path1 = "C:\\Users\\SyleSakis\\Dropbox\\comalat-pdf-files"
                + "\\English\\INTERMEDIATE\\UNITS-1-5";

        String output1 = output + "\\INTERMEDIATE_1_5_";
        CompressFiles.compress_folder(path1, output1 + d.getTime() + ".zip");

        String path2 = "C:\\Users\\SyleSakis\\Dropbox\\comalat-pdf-files"
                + "\\English\\INTERMEDIATE\\UNITS-6-10";

        String output2 = output + "\\INTERMEDIATE_6_10_";
        CompressFiles.compress_folder(path2, output2 + d.getTime() + ".zip");
    }
}
