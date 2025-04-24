import java.io.IOException;
import java.util.*;

class Main {
    static ArrayList<Student> students = new ArrayList<>();

    static TeacherManager teacherManager = new TeacherManager();
    static CourseManager courseManager;
    static StudentManager studentRecords;
    static StudentManager studentManager;

    public static void main(String[] args) throws IOException {
        teacherManager.initializeTeachers("Teachers.txt");
        initializeCourses();
        
        students = Student.loadStudents("StudentProfile.txt", courseManager);
        studentRecords = new StudentManager(students, courseManager);
        studentManager = new StudentManager(students, courseManager);

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
                case 1 -> addStudent(scanner);
                case 2 -> removeStudent(scanner);
                case 3 -> studentRecords.changeStudentDetails(scanner);
                case 4 -> studentRecords.showStudentDetails(scanner);
                case 5 -> courseManager.showCourseDetailsWithTeacherOption(scanner, students, teacherManager.getTeachers());
                case 6 -> {
                    studentManager.saveStudents("StudentProfile.txt");
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

    static void addStudent(Scanner scanner) {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
    
        System.out.print("Enter ID: ");
        String id = scanner.nextLine();
        
        // Extract student IDs into a separate list
        ArrayList<String> studentIds = new ArrayList<>();
        for (Student student : students) {
            studentIds.add(student.getId());
        }

        // Check if the ID already exists
        if (studentIds.contains(id)) {
            System.out.println("Student ID already exists.");
            return;
        }

        System.out.print("Enter Date of Birth (YYYY-MM-DD): ");
        String dob = scanner.nextLine();

        // Initialize marks and attendance with -1 for all courses (indicating not enrolled)
        ArrayList<Integer> marks = new ArrayList<>(Collections.nCopies(courseManager.getCourses().size(), -1));
        ArrayList<Integer> attendance = new ArrayList<>(Collections.nCopies(courseManager.getCourses().size(), -1));

        // Display available courses
        System.out.println("Available courses:");
        for (int i = 0; i < courseManager.getCourses().size(); i++) {
            Course course = courseManager.getCourses().get(i);
            System.out.println((i + 1) + ". " + course.getName() + " (Semester: " + course.getSemester() + ", Period: " + course.getPeriod() + ")");
        }

        // Prompt the user to select courses
        System.out.print("Enter the numbers of the courses you want to enroll in (comma-separated): ");
        String[] selectedCourses = scanner.nextLine().split(",");
    
        // Call studentEnrollment with all collected data
        studentManager.addStudent(id, name, dob, marks, attendance, selectedCourses);
    }

    static void removeStudent(Scanner scanner) {
        System.out.print("Enter Student ID to remove: ");
        String id = scanner.nextLine();

        boolean removed = studentManager.removeStudent(id);

        if (removed) {
            System.out.println("Student removed successfully.");
        } 
        else {
            System.out.println("Student not found.");
        }
    }
}