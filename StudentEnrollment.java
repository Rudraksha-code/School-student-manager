import java.util.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class StudentEnrollment {
    private ArrayList<Student> students;
    private CourseManager courseManager;

    StudentEnrollment(ArrayList<Student> students, CourseManager courseManager) {
        this.students = students;
        this.courseManager = courseManager;
    }

    void addStudent(Scanner scanner) {
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

        ArrayList<String> enrolledCourses = new ArrayList<>();
        ArrayList<String> conflictCourses = new ArrayList<>();

        // Update marks and attendance for selected courses
        for (String courseIndexStr : selectedCourses) {
            try {
                int courseIndex = Integer.parseInt(courseIndexStr.trim()) - 1;
                if (courseIndex >= 0 && courseIndex < courseManager.getCourses().size()) {
                    Course course = courseManager.getCourses().get(courseIndex);
                    int period = course.getPeriod();
                    int semester = course.getSemester();

                    // Check for conflicts
                    boolean conflict = false;
                    for (String enrolledCourse : enrolledCourses) {
                        if (enrolledCourse.contains("Semester " + semester) && enrolledCourse.contains("Period " + period)) {
                            conflict = true;
                            break;
                        }
                    }

                    if (conflict) {
                        conflictCourses.add(course.getName() + " (Semester " + semester + ", Period " + period + ")");
                    } else {
                        marks.set(courseIndex, 0); // Initialize marks to 0
                        attendance.set(courseIndex, 0); // Initialize attendance to 0
                        enrolledCourses.add(course.getName() + " (Semester " + semester + ", Period " + period + ")");
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

    void removeStudent(Scanner scanner) {
        System.out.print("Enter Student ID to remove: ");
        String id = scanner.nextLine();

        // Check if a student with the given ID exists
        boolean removed = students.removeIf(student -> student.getId().equals(id));

        if (removed) {
            System.out.println("Student removed successfully.");
        } 
        else {
            System.out.println("Student not found.");
        }
    }

    void saveStudents(String filePath) throws IOException {
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
        Utils.writeLinesToFile(filePath, studentLines);
    }
}