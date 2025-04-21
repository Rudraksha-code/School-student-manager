import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

class StudentRecords {
    private ArrayList<Student> students;
    private CourseManager courseManager;

    public StudentRecords(ArrayList<Student> students, CourseManager courseManager) {
        this.students = students;
        this.courseManager = courseManager;
    }

    public void changeStudentDetails(Scanner scanner) {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine();
        Student student = students.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.println("Enrolled Courses:");
        ArrayList<Integer> enrolledCourses = new ArrayList<>();
        for (int i = 0; i < courseManager.getCourses().size(); i++) {
            if (student.getMarks().get(i) != -1 && student.getAttendance().get(i) != -1) {
                System.out.println((enrolledCourses.size() + 1) + ". " + courseManager.getCourses().get(i).getName());
                enrolledCourses.add(i);
            }
        }

        if (enrolledCourses.isEmpty()) {
            System.out.println("The student is not enrolled in any courses.");
            return;
        }

        System.out.print("Enter the number of the course to update: ");
        int courseChoice;
        try {
            courseChoice = scanner.nextInt();
            scanner.nextLine();
            if (courseChoice < 1 || courseChoice > enrolledCourses.size()) {
                System.out.println("Invalid course number.");
                return;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.nextLine();
            return;
        }

        int courseIndex = enrolledCourses.get(courseChoice - 1);

        System.out.print("Enter new marks: ");
        int marks = scanner.nextInt();
        System.out.print("Enter new attendance: ");
        int attendance = scanner.nextInt();
        scanner.nextLine();

        student.getMarks().set(courseIndex, marks);
        student.getAttendance().set(courseIndex, attendance);

        System.out.println("Student details updated successfully.");
    }

    public void showStudentDetails(Scanner scanner) {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine();
        Student student = students.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.println("ID: " + student.getId());
        System.out.println("Name: " + student.getName());
        System.out.println("DOB: " + student.getDob());
        System.out.println("Enrolled Courses:");
        for (int i = 0; i < courseManager.getCourses().size(); i++) {
            if (student.getMarks().get(i) != -1 && student.getAttendance().get(i) != -1) {
                System.out.println("Course: " + courseManager.getCourses().get(i).getName());
                System.out.println("    Marks: " + student.getMarks().get(i));
                System.out.println("    Attendance: " + student.getAttendance().get(i));
            }
        }
    }
}
