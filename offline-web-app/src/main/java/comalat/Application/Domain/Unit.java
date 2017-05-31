package comalat.Application.Domain;

import comalat.Application.Domain.Enum.Skill;
import java.io.File;
import java.util.List;

/**
 *
 * @author SyleSakis
 */
class Unit implements SizeHandler{

    private String unitContents;
    private List<Skill> exercisedSkills;
    private List<File> files;

    public Unit() {
    }

    public Unit(String unitContents, List<Skill> exercisedSkills, List<File> files) {
        this.unitContents = unitContents;
        this.exercisedSkills = exercisedSkills;
        this.files = files;
    }

    public List<Skill> getExercisedSkills() {
        return exercisedSkills;
    }

    public void setExercisedSkills(List<Skill> exercisedSkills) {
        this.exercisedSkills = exercisedSkills;
    }

    public String getUnitContents() {
        return unitContents;
    }

    public void setUnitContents(String unitContents) {
        this.unitContents = unitContents;
    }

    public List<File> getFile() {
        return files;
    }

    public void setFile(List<File> file) {
        this.files = file;
    }

    public void addFile(File file) {
        files.add(file);
    }

    @Override
    public long getSize() {
        long size = 0;
        for (File file : files) {
            size += file.length();
        }
        return size;
    }
}
