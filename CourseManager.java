import java.util.ArrayList;
import java.util.Scanner;

class CourseManager {
    private ArrayList<Course> courses;

    CourseManager(ArrayList<Course> courses) {
        this.courses = courses;
    }

    ArrayList<Course> getCourses() {
        return courses;
    }

    void showCourseDetailsWithTeacherOption(Scanner scanner, ArrayList<Student> students, ArrayList<Teacher> teachers) {
        ArrayList<String> studentIds = new ArrayList<>();
        ArrayList<String> studentNames = new ArrayList<>();
        ArrayList<ArrayList<Integer>> studentMarks = new ArrayList<>();
        ArrayList<ArrayList<Integer>> studentAttendance = new ArrayList<>();
    
        for (Student student : students) {
            studentIds.add(student.getId());
            studentNames.add(student.getName());
            studentMarks.add(student.getMarks());
            studentAttendance.add(student.getAttendance());
        }
    
        Main.showCourseDetails(scanner, studentIds, studentNames, studentMarks, studentAttendance);
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