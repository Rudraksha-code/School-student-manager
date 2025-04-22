import java.util.ArrayList;
import java.util.Scanner;

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

    public void showCourseDetailsWithTeacherOption(Scanner scanner, ArrayList<Student> students, ArrayList<Teacher> teachers) {
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
    
        showCourseDetails(studentIds, studentNames, studentMarks, studentAttendance);
    
        System.out.print("\nWould you like to view the teacher's details for a specific course? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();
        if (response.equals("yes")) {
            System.out.print("Enter the name of the course: ");
            String courseName = scanner.nextLine().trim();
            Teacher.showTeacherDetails(teachers, courses, courseName);
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