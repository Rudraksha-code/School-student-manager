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
    
    void saveTeachers() throws IOException {
        List<String> teacherLines = new ArrayList<>();
        for (Teacher teacher : teachers) {
            StringBuilder sb = new StringBuilder();
            sb.append("ID: ").append(teacher.getId()).append(", ");
            sb.append("Name: ").append(teacher.getName()).append(", ");
            sb.append("DOB: ").append(teacher.getDob()).append(", ");
            sb.append("Courses: ").append(String.join(", ", teacher.getCourses()));
            teacherLines.add(sb.toString());
        }
        Utils.writeLinesToFile("Teachers.txt", teacherLines);
    }

    // Static method to show teacher details for a specific course
    static Teacher showTeacherDetails(ArrayList<Teacher> teachers, ArrayList<Course> courses, String courseName) {
        // First find the teacher assigned to teach this course in the system
        String assignedTeacherName = null;
        for (Course course : courses) {
            if (course.getName().equals(courseName)) {
                assignedTeacherName = course.getTeacher();
                break;
            }
        }
        
        if (assignedTeacherName != null) {
            // Find the teacher by name
            for (Teacher teacher : teachers) {
                if (teacher.getName().equals(assignedTeacherName)) {
                    return teacher;
                }
            }
        }
        // If no teacher is found, return null
        return null;
    }
}