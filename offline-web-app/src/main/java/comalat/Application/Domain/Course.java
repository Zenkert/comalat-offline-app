package comalat.Application.Domain;

import comalat.Services.FileServices.WordManager;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SyleSakis
 */
@XmlRootElement
class Course implements FolderInfoHandler {

    @XmlElement(name = "Course")
    private String courseName;
    private List<Unit> units;

    public Course() {
        units = new ArrayList<>();
    }

    public Course(String courseName, List<Unit> units) {
        this.courseName = courseName;
        this.units = units;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void addUnit(Unit u) {
        units.add(u);
    }

    @Override
    @XmlElement(name = "size")
    public long getSize() {
        long size = 0;
        for (Unit unit : units) {
            size += unit.getSize();
        }
        return size;
    }

    @Override
    //sourcePath = */Comalat-Folders/comalat-pdf-files/{langName}/{levelType}/{courses-x-y}
    public Course readFromFolder(String sourcePath) {
        File directory = new File(sourcePath);
        File contents = null;
        this.courseName = directory.getName();

        for (File folder : directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.isFile()) {
                    return pathname.getName().endsWith(".doc") || pathname.getName().endsWith(".docx");
                }
                return false;
            }
        })) {
            if (folder != null) {
                contents = new File(folder.getAbsolutePath());
            }
        }

        for (File folder : directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        })) {

            Unit unit = new Unit();
            unit = unit.readFromFolder(folder.getPath());
            unit.setUnitContents(WordManager.readTable(contents));
            units.add(unit);

        }
        return this;
    }

}
