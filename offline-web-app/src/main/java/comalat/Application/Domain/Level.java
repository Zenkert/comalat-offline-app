package comalat.Application.Domain;

import comalat.Application.Domain.Enum.LevelType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author SyleSakis
 */
class Level implements SizeHandler{

    private LevelType level;
    private List<Course> courses;

    public Level() {
        courses = new ArrayList<>();
    }

    public Level(LevelType level, List<Course> courses) {
        this.level = level;
        this.courses = courses;
    }

    public LevelType getLevel() {
        return level;
    }

    public void setLevel(LevelType level) {
        this.level = level;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public void addCourse(Course c) {
        courses.add(c);
    }

    @Override
    public long getSize() {
        long size = 0;
        for (Course c : courses) {
            size += c.getSize();
        }
        return size;
    }
}
