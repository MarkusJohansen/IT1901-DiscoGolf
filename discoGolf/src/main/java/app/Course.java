package app;

import java.util.ArrayList;
import java.util.HashMap;

public class Course {

    private int numberOfHoles;
    private String courseName;
    private HashMap<Integer, Integer> parForHoles;

    public Course(String courseName, ArrayList<Integer> parForHoles) {
        this.numberOfHoles = parForHoles.size();
        this.courseName = courseName;
        this.parForHoles = new HashMap<>();
        for (int i = 0; i < numberOfHoles; i++) {
            this.parForHoles.put(i + 1, parForHoles.get(i));
        }
    }

    public String getCourseName() {
        return courseName;
    }

    public HashMap<Integer, Integer> getPar() {
        return parForHoles;
    }

    public void setParForHole(int hole, int par) {
        if (hole < 1 || par < 3) {
            throw new IllegalArgumentException("Not a valid hole or par number");
        }
        parForHoles.put(hole, par);
    }

    public int getParForHole(int hole) {
        return parForHoles.get(hole);
    }
    
    public int getNumberOfHoles() {
        return numberOfHoles;
    }

    @Override
    public String toString() {
        return getCourseName() + ": " + getNumberOfHoles() + " holes";
    }
}
