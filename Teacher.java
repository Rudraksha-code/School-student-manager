import java.util.ArrayList;

class Teacher extends Person {
    private ArrayList<String> courses;

    Teacher(String id, String name, String dob, ArrayList<String> courses) {
        super(id, name, dob);
        this.courses = courses;
    }

    ArrayList<String> getCourses() {
        return courses;
    }

    void setCourses(ArrayList<String> courses) {
        this.courses = courses;
    }
}