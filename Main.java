import java.io.IOException;
import java.util.*;

public class Main {
    static ArrayList<Student> students = new ArrayList<>();
    static ArrayList<Teacher> teachers = new ArrayList<>();
    static CourseManager courseManager;
    static StudentRecords studentRecords;

    public static void main(String[] args) throws IOException {
        initializeTeachers();
        initializeCourses();
        loadStudents();
        studentRecords = new StudentRecords(students, courseManager);

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
                case 5 -> showCourseDetails(scanner);
                case 6 -> {
                    saveStudents();
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

        for (Teacher teacher : teachers) {
            teacherNames.add(teacher.getName());
            teacherCourses.add(teacher.getCourses());
        }

        ArrayList<Course> courses = Course.initializeCourses(teacherNames, teacherCourses);
        courseManager = new CourseManager(courses);
    }

    static void initializeTeachers() throws IOException {
        List<String> teacherLines = Utils.readLinesFromFile("School-student-manager/Teachers.txt");
        for (String line : teacherLines) {
            String[] parts = line.split(", ");
            String id = parts[0].split(": ")[1];
            String name = parts[1].split(": ")[1];
            String dob = parts[2].split(": ")[1];
            String[] coursesArray = parts[3].split(": ")[1].split(", ");
            ArrayList<String> courses = new ArrayList<>(Arrays.asList(coursesArray));

            teachers.add(new Teacher(id, name, dob, courses));
        }
    }

    static void loadStudents() throws IOException {
        List<String> studentLines = Utils.readLinesFromFile("School-student-manager/StudentProfile.txt");
        for (String line : studentLines) {
            String[] parts = line.split(", ", 5);
            if (parts.length < 5) {
                System.out.println("Invalid student data: " + line);
                continue;
            }

            String id = parts[0].split(": ")[1];
            String name = parts[1].split(": ")[1];
            String dob = parts[2].split(": ")[1];

            ArrayList<Integer> marks = parseDetails(parts[3].split(": ")[1]);
            ArrayList<Integer> attendance = parseDetails(parts[4].split(": ")[1]);

            students.add(new Student(id, name, dob, marks, attendance));
        }
    }

    static ArrayList<Integer> parseDetails(String details) {
        ArrayList<Integer> values = new ArrayList<>(Collections.nCopies(courseManager.getCourses().size(), 0));
        details = details.replace("{", "").replace("}", "").trim();
        String[] entries = details.split(", ");
        for (String entry : entries) {
            String[] keyValue = entry.split("=");
            if (keyValue.length == 2) {
                String courseName = keyValue[0].trim();
                int value = Integer.parseInt(keyValue[1].trim());
                int index = courseManager.getCourseIndex(courseName);
                if (index != -1) {
                    values.set(index, value);
                }
            }
        }
        return values;
    }

    static void saveStudents() throws IOException {
        List<String> studentLines = new ArrayList<>();

        for (Student student : students) {
            StringBuilder sb = new StringBuilder();
            sb.append("ID: ").append(student.getId()).append(", ");
            sb.append("Name: ").append(student.getName()).append(", ");
            sb.append("DOB: ").append(student.getDob()).append(", ");
            sb.append("Marks: {");
            
            for (int j = 0; j < courseManager.getCourses().size(); j++) {
                sb.append(courseManager.getCourses().get(j).getName()).append("=").append(student.getMarks().get(j));
                if (j < courseManager.getCourses().size() - 1) sb.append(", ");
            }
            sb.append("}, Attendance: {");
            for (int j = 0; j < courseManager.getCourses().size(); j++) {
                sb.append(courseManager.getCourses().get(j).getName()).append("=").append(student.getAttendance().get(j));
                if (j < courseManager.getCourses().size() - 1) sb.append(", ");
            }
            sb.append("}");
            studentLines.add(sb.toString());
        }
        Utils.writeLinesToFile("School-student-manager/StudentProfile.txt", studentLines);
    }

    static void showCourseDetails(Scanner scanner) {
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

        courseManager.showCourseDetails(studentIds, studentNames, studentMarks, studentAttendance);

        System.out.print("\nWould you like to view the teacher's details for a specific course? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();
        if (response.equals("yes")) {
            System.out.print("Enter the name of the course: ");
            String courseName = scanner.nextLine().trim();
            showTeacherDetails(courseName);
        }
    }

    static void addStudent(Scanner scanner) {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter ID: ");
        String id = scanner.nextLine();
        if (students.stream().anyMatch(student -> student.getId().equals(id))) {
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

        // Track selected periods and semesters to prevent conflicts
        HashSet<String> selectedPeriodSemesterPairs = new HashSet<>();
        ArrayList<String> enrolledCourses = new ArrayList<>();
        ArrayList<String> conflictCourses = new ArrayList<>();

        // Update marks and attendance for selected courses
        for (String courseIndexStr : selectedCourses) {
            try {
                int courseIndex = Integer.parseInt(courseIndexStr.trim()) - 1;
                if (courseIndex >= 0 && courseIndex < courseManager.getCourses().size()) {
                    Course course = courseManager.getCourses().get(courseIndex);
                    String periodSemesterPair = course.getPeriod() + "-" + course.getSemester();

                    if (selectedPeriodSemesterPairs.contains(periodSemesterPair)) {
                        conflictCourses.add(course.getName() + " (Semester " + course.getSemester() + ", Period " + course.getPeriod() + ")");
                    } else {
                        marks.set(courseIndex, 0); // Initialize marks to 0
                        attendance.set(courseIndex, 0); // Initialize attendance to 0
                        selectedPeriodSemesterPairs.add(periodSemesterPair); // Mark this period-semester pair as occupied
                        enrolledCourses.add(course.getName() + " (Semester " + course.getSemester() + ", Period " + course.getPeriod() + ")");
                    }
                } else {
                    System.out.println("Invalid course number: " + (courseIndex + 1));
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: " + courseIndexStr);
            }
        }

        // Add student details to the list
        students.add(new Student(id, name, dob, marks, attendance));

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
        students.removeIf(student -> student.getId().equals(id));
        System.out.println("Student removed successfully.");
    }

    static void showTeacherDetails(String courseName) {
        for (Teacher teacher : teachers) {
            if (teacher.getCourses().contains(courseName)) {
                System.out.println("Teacher Details:");
                System.out.println("  ID: " + teacher.getId());
                System.out.println("  Name: " + teacher.getName());
                System.out.println("  DOB: " + teacher.getDob());
                System.out.println("  Courses: " + String.join(", ", teacher.getCourses()));
                return;
            }
        }
        System.out.println("No teacher found for this course.");
    }
}