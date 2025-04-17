import java.util.ArrayList;

public class CourseManager {
    private ArrayList<Course> courses;

    public CourseManager(ArrayList<Course> courses) {
        this.courses = courses;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void showCourseDetails(ArrayList<String> studentIds, ArrayList<String> studentNames, ArrayList<ArrayList<Integer>> studentMarks, ArrayList<ArrayList<Integer>> studentAttendance) {
        for (Course course : courses) {
            System.out.println("Course: " + course.getName());
            System.out.println("  Teacher: " + course.getTeacher());
            System.out.println("  Semester: " + course.getSemester());
            System.out.println("  Period: " + course.getPeriod());
            System.out.println("  Students:");
            for (int i = 0; i < studentIds.size(); i++) {
                int courseIndex = getCourseIndex(course.getName());
                if (courseIndex != -1 && studentMarks.get(i).get(courseIndex) != -1 && studentAttendance.get(i).get(courseIndex) != -1) {
                    System.out.println("    - " + studentNames.get(i) + " (ID: " + studentIds.get(i) + ")");
                }
            }
            System.out.println();
        }
    }

    public int getCourseIndex(String courseName) {
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getName().equals(courseName)) {
                return i;
            }
        }
        return -1;
    }
}