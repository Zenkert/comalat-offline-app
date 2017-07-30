package comalat.Application.Domain;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author SyleSakis
 */
@XmlRootElement
public class Lessons implements FolderInfoHandler{
    @XmlElement(name = "Languages")
    private List<Language> languages;
    @XmlElement(name = "noUnits")
    private int noUnits = 0;
    @XmlTransient
    private long lastupdate;

    public Lessons() {
        languages = new ArrayList<>();
    }

    public Lessons(List<Language> languages) {
        this.languages = languages;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }    

    @Override
    @XmlElement(name = "size")
    public long getSize() {
        long size = 0;
        for (Language lang : languages) {
            size += lang.getSize();
        }
        return size;
    }
    
    @Override
    //sourcePath = */Comalat-Folders/comalat-pdf-files/
    public Lessons readFromFolder(String sourcePath){
        File directory = new File(sourcePath);
        if(!directory.exists()){
            return null;
        }
        lastupdate = directory.lastModified();
        for(File folder : directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        })){
            
            setLastUpdate(directory.lastModified());
            Language lang = new Language();
            lang = lang.readFromFolder(folder.getPath());
            noUnits += lang.getNoOfUnits();
            languages.add(lang);
        }
        this.getSize();
        return this;
    }

    @Override
    public int getNoOfUnits() {
        return noUnits;
    }
    
    @Override
    @XmlTransient
    public long getLastUpdate() {
        return lastupdate;
    }

    private void setLastUpdate(long update) {
        if(update > lastupdate){
            lastupdate = update;
        }
    }
}
