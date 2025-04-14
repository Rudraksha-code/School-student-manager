import java.io.*;
import java.util.*;

public class Main {
    static ArrayList<String> studentIds = new ArrayList<>();
    static ArrayList<String> studentNames = new ArrayList<>();
    static ArrayList<String> studentDobs = new ArrayList<>();
    static ArrayList<ArrayList<Integer>> studentMarks = new ArrayList<>();
    static ArrayList<ArrayList<Integer>> studentAttendance = new ArrayList<>();
    static ArrayList<String> courseNames = new ArrayList<>();
    static ArrayList<Integer> courseSemesters = new ArrayList<>();
    static ArrayList<Integer> coursePeriods = new ArrayList<>();
    static ArrayList<String> teachers = new ArrayList<>();

    static ArrayList<String> teacherIds = new ArrayList<>();
    static ArrayList<String> teacherNames = new ArrayList<>();
    static ArrayList<String> teacherDobs = new ArrayList<>();
    static ArrayList<ArrayList<String>> teacherCourses = new ArrayList<>();

    public static void main(String[] args) {
        initializeCourses();
        loadStudents();

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
                case 5 -> showCourseDetails();
                case 6 -> {
                    saveStudents();
                    System.out.println("Exiting...");
                    scanner.close(); // Close the scanner here
                    return;
                }
                default -> System.out.println("Invalid option. Please choose a number between 1 and 6.");
            }
        }
    }

    static void initializeCourses() {
        courseNames.add("Math");
        courseNames.add("Civics");
        courseNames.add("History");
        courseNames.add("English");
        courseNames.add("Physics");
        courseNames.add("Chemistry");
        courseNames.add("Biology");
        courseNames.add("French");
        courseNames.add("Computer Science");

        courseSemesters.add(1);
        courseSemesters.add(1);
        courseSemesters.add(1);
        courseSemesters.add(1);
        courseSemesters.add(2);
        courseSemesters.add(2);
        courseSemesters.add(2);
        courseSemesters.add(1);
        courseSemesters.add(2);

        coursePeriods.add(1);
        coursePeriods.add(2);
        coursePeriods.add(3);
        coursePeriods.add(4);
        coursePeriods.add(2);
        coursePeriods.add(3);
        coursePeriods.add(4);
        coursePeriods.add(1);
        coursePeriods.add(3);

        // Load teacher details from Teachers.txt
        try (BufferedReader br = new BufferedReader(new FileReader("School-student-manager/Teachers.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                String id = parts[0].split(": ")[1];
                String name = parts[1].split(": ")[1];
                String dob = parts[2].split(": ")[1];
                String[] courses = parts[3].split(": ")[1].split(", ");

                teacherIds.add(id);
                teacherNames.add(name);
                teacherDobs.add(dob);
                teacherCourses.add(new ArrayList<>(Arrays.asList(courses)));
            }
        } catch (IOException e) {
            System.out.println("Error loading teachers: " + e.getMessage());
        }

        // Map teachers to courses
        for (String course : courseNames) {
            boolean teacherFound = false;
            for (int i = 0; i < teacherCourses.size(); i++) {
                if (teacherCourses.get(i).contains(course)) {
                    teachers.add(teacherNames.get(i));
                    teacherFound = true;
                    break;
                }
            }
            if (!teacherFound) {
                teachers.add("Unknown"); // Default teacher name if no teacher is found
            }
        }
    }

    static void loadStudents() {
        try (BufferedReader br = new BufferedReader(new FileReader("School-student-manager/StudentProfile.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ", 5);
                if (parts.length < 5) {
                    System.out.println("Invalid student data: " + line);
                    continue;
                }

                studentIds.add(parts[0].split(": ")[1]);
                studentNames.add(parts[1].split(": ")[1]);
                studentDobs.add(parts[2].split(": ")[1]);

                ArrayList<Integer> marks = parseDetails(parts[3].split(": ")[1]);
                ArrayList<Integer> attendance = parseDetails(parts[4].split(": ")[1]);

                studentMarks.add(marks);
                studentAttendance.add(attendance);
            }
        } catch (IOException e) {
            System.out.println("Error loading students: " + e.getMessage());
        }
    }

    static ArrayList<Integer> parseDetails(String details) {
        ArrayList<Integer> values = new ArrayList<>(Collections.nCopies(courseNames.size(), 0));
        details = details.replace("{", "").replace("}", "").trim();
        String[] entries = details.split(", ");
        for (String entry : entries) {
            String[] keyValue = entry.split("=");
            if (keyValue.length == 2) {
                String courseName = keyValue[0].trim();
                int value = Integer.parseInt(keyValue[1].trim());
                int index = courseNames.indexOf(courseName);
                if (index != -1) {
                    values.set(index, value);
                }
            }
        }
        return values;
    }

    static void saveStudents() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("School-student-manager/StudentProfile.txt"))) {
            for (int i = 0; i < studentIds.size(); i++) {
                StringBuilder sb = new StringBuilder();
                sb.append("ID: ").append(studentIds.get(i)).append(", ");
                sb.append("Name: ").append(studentNames.get(i)).append(", ");
                sb.append("DOB: ").append(studentDobs.get(i)).append(", ");
                sb.append("Marks: {");
                for (int j = 0; j < courseNames.size(); j++) {
                    sb.append(courseNames.get(j)).append("=").append(studentMarks.get(i).get(j));
                    if (j < courseNames.size() - 1) sb.append(", ");
                }
                sb.append("}, Attendance: {");
                for (int j = 0; j < courseNames.size(); j++) {
                    sb.append(courseNames.get(j)).append("=").append(studentAttendance.get(i).get(j));
                    if (j < courseNames.size() - 1) sb.append(", ");
                }
                sb.append("}");
                bw.write(sb.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving students: " + e.getMessage());
        }
    }

    static void addStudent(Scanner scanner) {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter ID: ");
        String id = scanner.nextLine();
        if (studentIds.contains(id)) {
            System.out.println("Student ID already exists.");
            return;
        }
        System.out.print("Enter Date of Birth (YYYY-MM-DD): ");
        String dob = scanner.nextLine();

        // Initialize marks and attendance with -1 for all courses (indicating not enrolled)
        ArrayList<Integer> marks = new ArrayList<>(Collections.nCopies(courseNames.size(), -1));
        ArrayList<Integer> attendance = new ArrayList<>(Collections.nCopies(courseNames.size(), -1));

        // Display available courses
        System.out.println("Available courses:");
        for (int i = 0; i < courseNames.size(); i++) {
            System.out.println((i + 1) + ". " + courseNames.get(i) + " (Semester: " + courseSemesters.get(i) + ", Period: " + coursePeriods.get(i) + ")");
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
                if (courseIndex >= 0 && courseIndex < courseNames.size()) {
                    int period = coursePeriods.get(courseIndex);
                    int semester = courseSemesters.get(courseIndex);
                    String periodSemesterPair = period + "-" + semester;

                    if (selectedPeriodSemesterPairs.contains(periodSemesterPair)) {
                        conflictCourses.add(courseNames.get(courseIndex) + " (Semester " + semester + ", Period " + period + ")");
                    } else {
                        marks.set(courseIndex, 0); // Initialize marks to 0
                        attendance.set(courseIndex, 0); // Initialize attendance to 0
                        selectedPeriodSemesterPairs.add(periodSemesterPair); // Mark this period-semester pair as occupied
                        enrolledCourses.add(courseNames.get(courseIndex) + " (Semester " + semester + ", Period " + period + ")");
                    }
                } else {
                    System.out.println("Invalid course number: " + (courseIndex + 1));
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: " + courseIndexStr);
            }
        }

        // Add student details to the lists
        studentIds.add(id);
        studentNames.add(name);
        studentDobs.add(dob);
        studentMarks.add(marks);
        studentAttendance.add(attendance);

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
        int index = studentIds.indexOf(id);
        if (index != -1) {
            studentIds.remove(index);
            studentNames.remove(index);
            studentDobs.remove(index);
            studentMarks.remove(index);
            studentAttendance.remove(index);
            System.out.println("Student removed successfully.");
        } else {
            System.out.println("Student not found.");
        }
    }

    static void changeStudentDetails(Scanner scanner) {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine();
        int index = studentIds.indexOf(id);
        if (index == -1) {
            System.out.println("Student not found.");
            return;
        }

        // Display only the courses the student is enrolled in
        System.out.println("Enrolled Courses:");
        ArrayList<Integer> enrolledCourses = new ArrayList<>();
        for (int i = 0; i < courseNames.size(); i++) {
            if (studentMarks.get(index).get(i) != -1 && studentAttendance.get(index).get(i) != -1) {
                System.out.println((enrolledCourses.size() + 1) + ". " + courseNames.get(i));
                enrolledCourses.add(i); // Store the index of the enrolled course
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
            scanner.nextLine(); // Consume newline
            if (courseChoice < 1 || courseChoice > enrolledCourses.size()) {
                System.out.println("Invalid course number.");
                return;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.nextLine(); // Consume invalid input
            return;
        }

        int courseIndex = enrolledCourses.get(courseChoice - 1); // Get the actual course index

        System.out.print("Enter new marks: ");
        int marks = scanner.nextInt();
        System.out.print("Enter new attendance: ");
        int attendance = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        studentMarks.get(index).set(courseIndex, marks);
        studentAttendance.get(index).set(courseIndex, attendance);

        System.out.println("Student details updated successfully.");
    }

    static void showStudentDetails(Scanner scanner) {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine();
        int index = studentIds.indexOf(id);
        if (index == -1) {
            System.out.println("Student not found.");
            return;
        }

        System.out.println("ID: " + studentIds.get(index));
        System.out.println("Name: " + studentNames.get(index));
        System.out.println("DOB: " + studentDobs.get(index));
        System.out.println("Enrolled Courses:");
        for (int i = 0; i < courseNames.size(); i++) {
            // Only show courses the student is enrolled in
            if (studentMarks.get(index).get(i) != -1 && studentAttendance.get(index).get(i) != -1) {
                System.out.println("Course: " + courseNames.get(i));
                System.out.println("    Marks: " + studentMarks.get(index).get(i));
                System.out.println("    Attendance: " + studentAttendance.get(index).get(i));
            }
        }
    }

    static void showCourseDetails() {
        Scanner scanner = new Scanner(System.in);

        // Display all course details
        for (int i = 0; i < courseNames.size(); i++) {
            System.out.println("Course: " + courseNames.get(i));
            System.out.println("  Teacher: " + (i < teachers.size() ? teachers.get(i) : "Unknown"));
            System.out.println("  Semester: " + courseSemesters.get(i));
            System.out.println("  Period: " + coursePeriods.get(i));
            System.out.println("  Students:");
            for (int j = 0; j < studentIds.size(); j++) {
                // Check if the student is enrolled in the course (marks or attendance not -1)
                if (studentMarks.get(j).get(i) != -1 && studentAttendance.get(j).get(i) != -1) {
                    System.out.println("    - " + studentNames.get(j) + " (ID: " + studentIds.get(j) + ")");
                }
            }
            System.out.println();
        }

        // Ask the user which teacher's details they want to view
        System.out.print("Enter the name of the course to view the teacher's details (or type 'none' to skip): ");
        String courseName = scanner.nextLine().trim();

        if (!courseName.equalsIgnoreCase("none")) {
            showTeacherDetails(courseName);
        }
    }

    static void showTeacherDetails(String courseName) {
        for (int i = 0; i < teacherNames.size(); i++) {
            if (teacherCourses.get(i).contains(courseName)) {
                System.out.println("Teacher Details:");
                System.out.println("  ID: " + teacherIds.get(i));
                System.out.println("  Name: " + teacherNames.get(i));
                System.out.println("  DOB: " + teacherDobs.get(i));
                System.out.println("  Courses: " + String.join(", ", teacherCourses.get(i)));
                return;
            }
        }
        System.out.println("No teacher found for this course.");
    }
}