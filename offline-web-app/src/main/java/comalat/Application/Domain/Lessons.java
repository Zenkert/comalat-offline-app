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
public class Lessons implements FolderInfoHandler{
    @XmlElement(name = "Languages")
    private List<Language> languages;

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
        for(File folder : directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        })){
            Language lang = new Language();
            lang = lang.readFromFolder(folder.getPath());
            languages.add(lang);
        }
        this.getSize();
        return this;
    }
}
