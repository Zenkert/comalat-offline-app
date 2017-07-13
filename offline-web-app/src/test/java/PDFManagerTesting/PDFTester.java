package PDFManagerTesting;

import comalat.Application.Domain.Enum.Skill;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
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
public class PDFTester {
    
    private static final String skillsFormat = "(.*)-P-(.*)";

    public PDFTester() {
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

    //@Test
    public void RunPrinting() {
        String filename = "C:\\Users\\SyleSakis\\Dropbox\\"
                + "Comalat-Folders\\comalat-pdf-files\\"
                + "English\\Elementary\\courses-1-5\\unit1\\UNIT 1 FINAL VERSION.pdf";

        
        extractTextFromPDF(new File(filename));
    }
    
    
    public static String[] extractTextFromPDF (final File file) {

        PDDocument document = null;
        String[] token = null;

        try {
            document = PDDocument.load(file);
            document.getClass();
            if (!document.isEncrypted()) {
                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);
                PDFTextStripper Tstripper = new PDFTextStripper();
                //String st = Tstripper.getText(document);
                //System.out.println("Text:" + st);
                token = Tstripper.getText(document).split(Tstripper.getLineSeparator());
                printToken(token);
            }
        } catch (Exception ex) {
            // do nothing
            ;
        }finally{
            try {
                document.close();
            } catch (IOException ex) {
                // do nothing
                ;
            } 
        }
        return token;
    }
    
    public List<Skill> getSkills(File file){
        String[] token = extractTextFromPDF(file);
        List<String> skillsList = getFormatedToken(token);
        return null;
    }

    private static List<String> getFormatedToken(String[] token) {
        List<String> formatedToken = new ArrayList<>();
        
        for (int i = 0; i < token.length; i++) {
            if (token[i].matches(skillsFormat)){
                formatedToken.add(token[i]);
            }
        }
        return formatedToken;
    }
    
    private static void printToken(String[] token) {
        for (int i = 0; i < token.length; i++) {
            if (token[i].matches(skillsFormat)){
                token[i] = token[i].replaceAll("(.*)-P-", "");
                //System.out.println("Token[i]: "+token[i]+"\nToken Char: "+token[i].charAt(0));
                //System.out.println(token[i]);
            }
        }
    }
}
