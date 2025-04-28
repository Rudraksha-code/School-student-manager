import java.util.ArrayList;

class CourseManager {
    private ArrayList<Course> courses;

    CourseManager(ArrayList<Course> courses) {
        this.courses = courses;
    }

    ArrayList<Course> getCourses() {
        return courses;
    }

    void initializeCourses(ArrayList<Teacher> teachers) {
        ArrayList<String> teacherNames = new ArrayList<>();
        ArrayList<ArrayList<String>> teacherCourses = new ArrayList<>();
    
        for (Teacher teacher : teachers) {
            teacherNames.add(teacher.getName());
            teacherCourses.add(teacher.getCourses());
        }
    
        this.courses = Course.initializeEachCourse(teacherNames, teacherCourses);
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