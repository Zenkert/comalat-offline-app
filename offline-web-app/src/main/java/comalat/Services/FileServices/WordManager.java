package comalat.Services.FileServices;

import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;

/**
 *
 * @author SyleSakis
 */
public class WordManager {

    public static List<String[]> readTable(File file) {
        FileInputStream fis = null;
        InputStream fisdoc = null;
        List<String[]> contents = null;
        try {
            fis = new FileInputStream(file.getAbsolutePath());
        } catch (FileNotFoundException ex) {
            return null;
        } catch (NullPointerException ex) {
            return null;
        }

        try {
            XWPFDocument document = new XWPFDocument(fis);
            contents = readTableXWPF(document);
        } catch (OLE2NotOfficeXmlFileException ex) {
            try {
                fisdoc = new FileInputStream(file.getAbsolutePath());
                POIFSFileSystem fs = new POIFSFileSystem(fisdoc);
                HWPFDocument document = new HWPFDocument(fs);

                contents = readTableHWPF(document);
            } catch (IOException ex1) {
                return null;
            } finally {
                try {
                    fisdoc.close();
                } catch (IOException ex1) {
                    return null;
                }
            }

        } catch (IOException ex) {
            return null;
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                return null;
            }
        }

        return contents;
    }

    private static List<String[]> readTableXWPF(XWPFDocument document) {
        List<XWPFTable> tables = document.getTables();
        List<String[]> listcont = new ArrayList<>();
        XWPFTable table = tables.get(0);

        for (int j = 1; j < 6; j++) {
            String[] tmp = new String[4];
            for (int i = 2; i < table.getNumberOfRows(); i++) {
                if (table.getRow(i).getCell(j) != null) {
                    tmp[i - 2] = new String();
                    tmp[i - 2] = table.getRow(i).getCell(0).getText() + " : " + table.getRow(i).getCell(j).getTextRecursively().replaceAll("\t", " ");
                }
            }
            listcont.add(tmp);
        }

        /*
        for (String[] str : listcont) {
            System.out.println("-------------Unit:");
            for (String str2 : str) {
                System.out.println("-------------------ITEM: \n" + str2);
            }
        } 
         */
        return listcont;
    }

    private static List<String[]> readTableHWPF(HWPFDocument document) {
        List<String[]> listcont = new ArrayList<>();
        Range range = document.getRange();
        TableIterator itr = new TableIterator(range);
        while (itr.hasNext()) {
            Table table = itr.next();
            for (int j = 1; j < 6; j++) {
                String[] tmp2 = new String[4];
                for (int i = 2; i < table.numRows(); i++) {
                    tmp2[i - 2] = new String();
                    TableRow row = table.getRow(i);
                    tmp2[i - 2] = row.getCell(0).getParagraph(0).text().replaceAll("\\u0007", "") + " : ";
                    for (int p = 0; p < row.getCell(j).numParagraphs(); p++) {
                        tmp2[i - 2] += row.getCell(j).getParagraph(p).text().replaceAll("\t", " ").replaceAll("\r", " ").replaceAll("\\u0007", "");
                    }
                }
                listcont.add(tmp2);
            }
        }

        /*
        for (String[] str : listcont) {
            System.out.println("-------------Unit:");
            for (String str2 : str) {
                System.out.println("-------------------ITEM: \n" + str2);
            }
        } 
        */
        return listcont;
    }

}
