package comalat.Services.FileServices;

import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public static String[] readTable(File file) {
        FileInputStream fis = null;
        InputStream fisdoc = null;
        String[] contents = new String[5];
        try {
            fis = new FileInputStream(file.getAbsolutePath());
        } catch (FileNotFoundException ex) {
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

    private static String[] readTableXWPF(XWPFDocument document) {
        List<XWPFTable> tables = document.getTables();
        String tmp = "";
        String[] cont = new String[5];
        XWPFTable table = tables.get(0);

        for (int j = 1; j < 6; j++) {
            for (int i = 2; i < table.getNumberOfRows(); i++) {
                if (table.getRow(i).getCell(j) != null) {
                    tmp += (table.getRow(i).getCell(0).getText() + " : "
                            + table.getRow(i).getCell(j).getText() + System.getProperty("line.separator")).replaceAll("\t", " ").trim();

                }
            }
            cont[j - 1] = tmp.trim();
            tmp = "";
        }

        /*
        for (int q = 0; q < 5; q++) {
            System.out.println(cont[q]);
        }
         */
        return cont;
    }

    private static String[] readTableHWPF(HWPFDocument document) {

        String tmp = "";
        String[] cont = new String[5];
        Range range = document.getRange();
        TableIterator itr = new TableIterator(range);
        while (itr.hasNext()) {
            Table table = itr.next();
            for (int j = 1; j < 6; j++) {
                for (int i = 2; i < table.numRows(); i++) {
                    TableRow row = table.getRow(i);
                    tmp += row.getCell(0).getParagraph(0).text() + " : ";
                    for (int p = 0; p < row.getCell(j).numParagraphs(); p++) {
                        tmp += row.getCell(j).getParagraph(p).text() + System.getProperty("line.separator");
                    }
                }
                cont[j - 1] = tmp;
                tmp = "";
            }
        }
        /*
        for (int q = 0; q < 5; q++) {
            System.out.println(cont[q]);
        }
         */
        return cont;
    }

}
