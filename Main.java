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
        
        courseManager = new CourseManager(new ArrayList<>());
        courseManager.initializeCourses(teacherManager.getTeachers());
        
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
                case 3 -> changeStudentDetails(scanner);
                case 4 -> showStudentDetails(scanner);
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

    static void enrollmentSummary(ArrayList<String> enrolledCourses, ArrayList<String> conflictCourses) {
        // Display summary of enrollment
        System.out.println("\nEnrollment Summary:");
        if (!enrolledCourses.isEmpty()) {
            System.out.println("Successfully enrolled in:");
            for (String course : enrolledCourses) {
                System.out.println("  - " + course);
            }
        }
        if (!conflictCourses.isEmpty()) {
            System.out.println("Could not enroll due to time conflicts:");
            for (String course : conflictCourses) {
                System.out.println("  - " + course);
            }
        }
        System.out.println("Student added successfully.");
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
    
    static void changeStudentDetails(Scanner scanner) {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine();
        
        Student student = studentRecords.findStudentById(id);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        // Get enrolled courses
        ArrayList<Integer> enrolledCourses = new ArrayList<>();
        System.out.println("Enrolled Courses:");
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

        studentRecords.updateStudentCourseDetails(student, courseIndex, marks, attendance);
        System.out.println("Student details updated successfully.");
    }
    
    static void showStudentDetails(Scanner scanner) {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine();
        
        Student student = studentRecords.findStudentById(id);
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
    
    static void showCourseDetails(Scanner scanner, ArrayList<String> studentIds, ArrayList<String> studentNames, ArrayList<ArrayList<Integer>> studentMarks, ArrayList<ArrayList<Integer>> studentAttendance) {
        // Display course details
        for (Course course : courseManager.getCourses()) {
            System.out.println("Course: " + course.getName());
            System.out.println("  Teacher: " + course.getTeacher());
            System.out.println("  Semester: " + course.getSemester());
            System.out.println("  Period: " + course.getPeriod());
            System.out.println("  Students:");
            
            int courseIndex = courseManager.getCourseIndex(course.getName());
            if (courseIndex != -1) {
                for (int i = 0; i < studentIds.size(); i++) {
                    if (studentMarks.get(i).get(courseIndex) != -1 && studentAttendance.get(i).get(courseIndex) != -1) {
                        System.out.println("    - " + studentNames.get(i) + " (ID: " + studentIds.get(i) + ")");
                    }
                }
            }
            System.out.println();
        }
        viewTeacherDetail(scanner);
    }

    static void viewTeacherDetail(Scanner scanner) {
        System.out.print("\nWould you like to view the teacher's details for a specific course? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();
        if (response.equals("yes")) {
            System.out.print("Enter the name of the course: ");
            String courseName = scanner.nextLine().trim();
            
            Teacher teacher = Teacher.showTeacherDetails(teacherManager.getTeachers(), courseManager.getCourses(), courseName);

            if (teacher != null) {
                System.out.println("Teacher Details:");
                System.out.println("  ID: " + teacher.getId());
                System.out.println("  Name: " + teacher.getName());
                System.out.println("  DOB: " + teacher.getDob());
                System.out.println("  Courses: " + String.join(", ", teacher.getCourses()));
            } else {
                System.out.println("No teacher found for this course.");
            }
        }
    }
}