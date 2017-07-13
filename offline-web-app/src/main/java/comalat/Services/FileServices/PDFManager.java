package comalat.Services.FileServices;

import comalat.Application.Domain.Enum.Skill;

import java.io.File;
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

/**
 *
 * @author SyleSakis
 */
public class PDFManager {
    
    private static final String skillsFormat = "(.*)-P-(.*)";
    
    private static String[] extractTextFromPDF (File file) {

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
                //printToken(token);
            }
        } catch (Exception ex) {
            // do nothing
            return null;
        }finally{
            try {
                document.close();
            } catch (IOException ex) {
                // do nothing
                return null;
            } 
        }
        return token;
    }
    
    public static List<Skill> getSkills(File file){
        List<Skill> skillList = new ArrayList<>();
        
        String[] token = extractTextFromPDF(file);
        if(token == null){
            return null;
        }
        for (int i = 0; i < token.length; i++) {
            if (token[i].matches(skillsFormat)){
                token[i] = token[i].replaceAll("(.*)-P-", "");
                Skill tmpSkill = Skill.convertShort(token[i].charAt(0));
                if(!skillList.contains(tmpSkill)){
                    skillList.add(tmpSkill);
                }
            }
        }
        return skillList;
    }
    
    private static void printToken(String[] token) {
        for (int i = 0; i < token.length; i++) {
            if (token[i].matches(skillsFormat)){
                token[i].replaceAll("(.*)-P-", "");
                System.out.println(token[i]);
            }
        }
    }
    
}
