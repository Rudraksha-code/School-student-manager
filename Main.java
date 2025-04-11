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
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> addStudent(scanner);
                case 2 -> removeStudent(scanner);
                case 3 -> changeStudentDetails(scanner);
                case 4 -> showStudentDetails(scanner);
                case 5 -> showCourseDetails();
                case 6 -> {
                    saveStudents();
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
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

        courseSemesters.add(1);
        courseSemesters.add(1);
        courseSemesters.add(1);
        courseSemesters.add(1);
        courseSemesters.add(2);
        courseSemesters.add(2);
        courseSemesters.add(2);

        coursePeriods.add(1);
        coursePeriods.add(2);
        coursePeriods.add(3);
        coursePeriods.add(4);
        coursePeriods.add(2);
        coursePeriods.add(3);
        coursePeriods.add(4);

        // Load teacher names from Teachers.txt
        try (BufferedReader br = new BufferedReader(new FileReader("Teachers.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                teachers.add(line.trim());
            }
        } catch (IOException e) {
            System.out.println("Error loading teachers: " + e.getMessage());
        }

        // Ensure the number of teachers matches the number of courses
        while (teachers.size() < courseNames.size()) {
            teachers.add("Unknown"); // Default teacher name if missing
        }
    }

    static void loadStudents() {
        try (BufferedReader br = new BufferedReader(new FileReader("StudentProfile.txt"))) {
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
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("StudentProfile.txt"))) {
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
            System.out.println((i + 1) + ". " + courseNames.get(i));
        }

        // Prompt the user to select courses
        System.out.print("Enter the numbers of the courses you want to enroll in (comma-separated): ");
        String[] selectedCourses = scanner.nextLine().split(",");

        // Update marks and attendance for selected courses
        for (String courseIndexStr : selectedCourses) {
            try {
                int courseIndex = Integer.parseInt(courseIndexStr.trim()) - 1;
                if (courseIndex >= 0 && courseIndex < courseNames.size()) {
                    marks.set(courseIndex, 0); // Initialize marks to 0
                    attendance.set(courseIndex, 0); // Initialize attendance to 0
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

        System.out.println("Courses:");
        for (int i = 0; i < courseNames.size(); i++) {
            System.out.println((i + 1) + ". " + courseNames.get(i));
        }

        System.out.print("Enter course name to update: ");
        String course = scanner.nextLine();
        int courseIndex = courseNames.indexOf(course);
        if (courseIndex == -1) {
            System.out.println("Invalid course name.");
            return;
        }

        // Check if the student is enrolled in the course
        if (studentMarks.get(index).get(courseIndex) == -1 || studentAttendance.get(index).get(courseIndex) == -1) {
            System.out.println("The student is not enrolled in this course. Cannot update marks or attendance.");
            return;
        }

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
        System.out.println("Marks:");
        for (int i = 0; i < courseNames.size(); i++) {
            int mark = studentMarks.get(index).get(i);
            System.out.println(courseNames.get(i) + ": " + (mark == -1 ? "Not Enrolled" : mark));
        }
        System.out.println("Attendance:");
        for (int i = 0; i < courseNames.size(); i++) {
            int attendance = studentAttendance.get(index).get(i);
            System.out.println(courseNames.get(i) + ": " + (attendance == -1 ? "Not Enrolled" : attendance));
        }
    }

    static void showCourseDetails() {
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
    }
}