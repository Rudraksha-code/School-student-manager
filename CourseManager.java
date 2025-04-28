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
        ArrayList<String> courseList = new ArrayList<>(courseNames);
        
        this.courses = Course.initializeEachCourse(courseList, teachers);
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