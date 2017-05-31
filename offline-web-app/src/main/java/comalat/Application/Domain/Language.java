package comalat.Application.Domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author SyleSakis
 */
public class Language implements SizeHandler{

    private List<Level> levels;

    public Language() {
        levels = new ArrayList<>();
    }

    public Language(List<Level> level) {
        this.levels = level;
    }

    public List<Level> getLevel() {
        return levels;
    }

    public void setLevel(List<Level> level) {
        this.levels = level;
    }

    public void addLevel(Level lvl) {
        levels.add(lvl);
    }

    @Override
    public long getSize() {
        long size = 0;
        for (Level lvl : levels) {
            size += lvl.getSize();
        }
        return size;
    }
}
