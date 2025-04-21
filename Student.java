import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Student extends Person {
    private ArrayList<Integer> marks;
    private ArrayList<Integer> attendance;

    public Student(String id, String name, String dob, ArrayList<Integer> marks, ArrayList<Integer> attendance) {
        // used super to call the constructor of the parent class (Person)
        super(id, name, dob);

        // Initialize Student-specific fields
        this.marks = marks;
        this.attendance = attendance;
    }

    public ArrayList<Integer> getMarks() {
        return marks;
    }

    public ArrayList<Integer> getAttendance() {
        return attendance;
    }

    public void setMarks(ArrayList<Integer> marks) {
        this.marks = marks;
    }

    public void setAttendance(ArrayList<Integer> attendance) {
        this.attendance = attendance;
    }

    // A method to load students from the StudentProfile file
    public static ArrayList<Student> loadStudents(String filePath, CourseManager courseManager) throws IOException {
        ArrayList<Student> students = new ArrayList<>();
        List<String> studentLines = Utils.readLinesFromFile(filePath);

        for (String line : studentLines) {
            String[] parts = line.split(", ", 5);
            if (parts.length < 5) {
                System.out.println("Invalid student data: " + line);
                continue;
            }

            String id = parts[0].split(": ")[1];
            String name = parts[1].split(": ")[1];
            String dob = parts[2].split(": ")[1];

            ArrayList<Integer> marks = Utils.parseDetails(parts[3].split(": ")[1], courseManager);
            ArrayList<Integer> attendance = Utils.parseDetails(parts[4].split(": ")[1], courseManager);

            students.add(new Student(id, name, dob, marks, attendance));
        }

        return students;
    }
}