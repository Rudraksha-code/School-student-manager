import java.io.IOException;
import java.util.*;

public class Main {
    static ArrayList<Student> students = new ArrayList<>();

    static TeacherManager teacherManager = new TeacherManager();
    static CourseManager courseManager;
    static StudentRecords studentRecords;
    static StudentEnrollment studentEnrollment;

    public static void main(String[] args) throws IOException {
        teacherManager.initializeTeachers("School-student-manager/Teachers.txt");

        initializeCourses();
        
        students = Student.loadStudents("School-student-manager/StudentProfile.txt", courseManager);
        studentRecords = new StudentRecords(students, courseManager);
        studentEnrollment = new StudentEnrollment(students, courseManager);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nStudent Management System");
            System.out.println("1. Add Student");
            System.out.println("2. Remove Student");
            System.out.println("3. Change Student Details");
            System.out.println("4. Show Student Details");
            System.out.println("5. Show Course Details");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 6.");
                scanner.nextLine(); // Consume invalid input
                continue;
            }

            switch (choice) {
                case 1 -> studentEnrollment.addStudent(scanner);
                case 2 -> studentEnrollment.removeStudent(scanner);
                case 3 -> studentRecords.changeStudentDetails(scanner);
                case 4 -> studentRecords.showStudentDetails(scanner);
                case 5 -> courseManager.showCourseDetailsWithTeacherOption(scanner, students, teacherManager.getTeachers());
                case 6 -> {
                    studentEnrollment.saveStudents("School-student-manager/StudentProfile.txt");
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid option. Please choose a number between 1 and 6.");
            }
        }
    }

    static void initializeCourses() {
        ArrayList<String> teacherNames = new ArrayList<>();
        ArrayList<ArrayList<String>> teacherCourses = new ArrayList<>();

        for (Teacher teacher : teacherManager.getTeachers()) {
            teacherNames.add(teacher.getName());
            teacherCourses.add(teacher.getCourses());
        }

        ArrayList<Course> courses = Course.initializeCourses(teacherNames, teacherCourses);
        courseManager = new CourseManager(courses);
    }
}