import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class TeacherManager {
    private ArrayList<Teacher> teachers;

    TeacherManager() {
        this.teachers = new ArrayList<>();
    }

    ArrayList<Teacher> getTeachers() {
        return teachers;
    }

    void initializeTeachers(String filePath) throws IOException {
        List<String> teacherLines = Utils.readLinesFromFile(filePath);
        for (String line : teacherLines) {
            String[] parts = line.split(", ", 4); // Split into 4 parts: ID, Name, DOB, and Courses
            String id = parts[0].split(": ")[1];
            String name = parts[1].split(": ")[1];
            String dob = parts[2].split(": ")[1];
            String[] coursesArray = parts[3].split(": ")[1].split(", "); // Correctly split courses by ", "
            ArrayList<String> courses = new ArrayList<>(Arrays.asList(coursesArray));

            teachers.add(new Teacher(id, name, dob, courses));
        }
    }
}