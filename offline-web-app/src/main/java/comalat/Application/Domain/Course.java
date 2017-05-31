package comalat.Application.Domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author SyleSakis
 */
class Course implements SizeHandler{

    private List<Unit> units;

    public Course() {
        units = new ArrayList<>();
    }

    public Course(List<Unit> units) {
        this.units = units;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    public void addUnit(Unit u) {
        units.add(u);
    }

    @Override
    public long getSize() {
        long size = 0;
        for (Unit u : units) {
            size += u.getSize();
        }
        return size;
    }

}
