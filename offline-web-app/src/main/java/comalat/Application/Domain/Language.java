package comalat.Application.Domain;

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
public class Language implements FolderInfoHandler {

    @XmlElement(name = "Language")
    private String languageName;
    @XmlElement(name = "EducationLevels")
    private List<Level> levels;

    public Language() {
        levels = new ArrayList<>();
    }

    public Language(String languageName, List<Level> levels) {
        this.languageName = languageName;
        this.levels = levels;
    }

    public List<Level> getLevel() {
        return levels;
    }

    public void setLevels(List<Level> levels) {
        this.levels = levels;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public void addLevel(Level lvl) {
        levels.add(lvl);
    }

    @Override
    @XmlElement(name = "size")
    public long getSize() {
        long size = 0;
        for (Level lvl : levels) {
            size += lvl.getSize();
        }
        return size;
    }

    @Override
    //sourcePath = */Comalat-Folders/comalat-pdf-files/{langName}
    public Language readFromFolder(String sourcePath) {
        File directory = new File(sourcePath);
        this.languageName = directory.getName();
        for (File folder : directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        })) {
            Level lvl = new Level();
            lvl = lvl.readFromFolder(folder.getPath());
            levels.add(lvl);
        }

        return this;
    }
}
