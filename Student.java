import java.util.ArrayList;

class Student extends Person {
    private ArrayList<Integer> marks;
    private ArrayList<Integer> attendance;

    Student(String id, String name, String dob, ArrayList<Integer> marks, ArrayList<Integer> attendance) {
        // used super to call the constructor of the parent class (Person)
        super(id, name, dob);

        // Initialize Student-specific fields
        this.marks = marks;
        this.attendance = attendance;
    }

    ArrayList<Integer> getMarks() {
        return marks;
    }

    ArrayList<Integer> getAttendance() {
        return attendance;
    }

    void setMarks(ArrayList<Integer> marks) {
        this.marks = marks;
    }

    void setAttendance(ArrayList<Integer> attendance) {
        this.attendance = attendance;
    }
}