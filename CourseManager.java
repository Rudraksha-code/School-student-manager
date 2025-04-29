import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class CourseManager {
    private ArrayList<Course> courses;

    CourseManager(ArrayList<Course> courses) {
        this.courses = courses;
    }

    ArrayList<Course> getCourses() {
        return courses;
    }

    void initializeCourses(ArrayList<Teacher> teachers) throws IOException {
        // Read course names from Courses.txt
        List<String> courseNames = Utils.readLinesFromFile("Courses.txt");
        
        courses = new ArrayList<>();
        
        int semester = 1; // Start with semester 1
        int period = 1;   // Start with period 1
        
        // Get a list of teacher names for direct assignment
        ArrayList<String> teacherNames = new ArrayList<>();
        for (Teacher teacher : teachers) {
            teacherNames.add(teacher.getName());
        }
        
        for (int i = 0; i < courseNames.size(); i++) {
            String courseName = courseNames.get(i);
            
            // Assign teacher using modulo to cycle through the available teachers
            String assignedTeacher = "Unassigned";
            if (!teacherNames.isEmpty()) {
                assignedTeacher = teacherNames.get(i % teacherNames.size());
            }
            
            courses.add(new Course(courseName, semester, period, assignedTeacher));
            
            period++; // Increment period for the next course
            if (period > 4) { // Reset period after 4 and move to the next semester
                period = 1;
                semester++;
                if (semester > 2) { // Only two semesters allowed
                    semester = 1; // Reset semester to 1 if it exceeds 2
                }
            }
        }
    }

    int getCourseIndex(String courseName) {
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getName().equals(courseName)) {
                return i;
            }
        }
        return -1;
    }
}