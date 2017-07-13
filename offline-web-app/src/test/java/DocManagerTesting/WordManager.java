package DocManagerTesting;

import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

    public static void readTable(File file, int column) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file.getAbsolutePath());
        } catch (FileNotFoundException ex) {
            return;
        }

        try {
            XWPFDocument document = new XWPFDocument(fis);
            readTableXWPF(document, column);
        } catch (OLE2NotOfficeXmlFileException ex) {
            try {
                InputStream newFis = new FileInputStream(file.getAbsolutePath());
                POIFSFileSystem fs = new POIFSFileSystem(newFis);
                HWPFDocument document = new HWPFDocument(fs);
                readTableHWPF(document, column);
            } catch (IOException ex1) {
                return;
            }

        } catch (IOException ex) {
            return;
        }
    }

    private static void readTableXWPF(XWPFDocument document, int x) {
        List<XWPFTable> tables = document.getTables();
        String tmp = "";
        String[] cont = new String[5];
        XWPFTable table = tables.get(0);

        for (int j = 1; j < 6; j++) {
            for (int i = 2; i < table.getNumberOfRows(); i++) {
                if (table.getRow(i).getCell(j) != null) {
                    tmp += table.getRow(i).getCell(0).getTextRecursively() + " : " + table.getRow(i).getCell(j).getTextRecursively() + "\n";

                }
            }
            cont[j - 1] = tmp;
            tmp = "";
        }

        for (int q = 0; q < 5; q++) {
            System.out.println(cont[q]);
        }
        //System.out.println(cont);
    }

    private static void readTableHWPF(HWPFDocument document, int x) {

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
                        tmp += row.getCell(j).getParagraph(p).text() + "\n";
                    }
                }
                cont[j - 1] = tmp;
                tmp = "";
            }
        }
        System.out.println(tmp);
    }
}
