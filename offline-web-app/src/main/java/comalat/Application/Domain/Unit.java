package comalat.Application.Domain;

import comalat.Application.Domain.Enum.Skill;
import comalat.Constants;
import comalat.Services.FileServices.PDFManager;
import java.io.File;
import java.io.FileFilter;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author SyleSakis
 */
@XmlRootElement
class Unit implements FolderInfoHandler {

    @XmlElement(name = "Unit")
    private String unitName;
    private String unitContents;
    private List<Skill> exercisedSkills;
    private File file;

    public Unit() {
    }

    public Unit(String unitName, String unitContents, List<Skill> exercisedSkills, File file) {
        this.unitName = unitName;
        this.unitContents = unitContents;
        this.exercisedSkills = exercisedSkills;
        this.file = file;
    }

    @XmlElement(name = "Skills")
    public List<String> getExercisedSkillsValue() {
        return Skill.convertSkillstoValues(this.exercisedSkills);
    }

    @XmlTransient
    public List<Skill> getExercisedSkills() {
        return exercisedSkills;
    }

    public void setExercisedSkills(List<Skill> exercisedSkills) {
        this.exercisedSkills = exercisedSkills;
    }

    @XmlElement(name = "Contents")
    public String getUnitContents() {
        return unitContents;
    }

    public void setUnitContents(String unitContents) {
        this.unitContents = unitContents;
    }

    public void setUnitContents(String[] unitsContents) {
        try {
            String tmp = unitName.replace("unit", "");
            int column = Integer.parseInt(tmp);
            column = (column-1) % 5;

            this.unitContents = unitsContents[column];
        } catch (NumberFormatException ex) {
            return;
        }
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    @XmlElement(name = "File")
    public String getFileName() {
        return file.getName();
    }

    @XmlTransient
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    @XmlElement(name = "size")
    public long getSize() {
        return file.length();
    }

    @Override
    //sourcePath = */Comalat-Folders/comalat-pdf-files/{langName}/{levelType}/{courses-x-y}/{unitz}
    public Unit readFromFolder(String sourcePath) {
        File directory = new File(sourcePath);
        this.unitName = directory.getName();
        for (File pdfFile : directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return (pathname.getPath().endsWith(Constants.PDF_FORMAT)
                        && pathname.isFile());
            }
        })) {
            this.file = pdfFile;
            this.setExercisedSkills(PDFManager.getSkills(file));
        }
        return this;
    }
}
