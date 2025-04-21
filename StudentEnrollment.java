import java.util.*;

public class StudentEnrollment {
    private ArrayList<Student> students;
    private CourseManager courseManager;

    public StudentEnrollment(ArrayList<Student> students, CourseManager courseManager) {
        this.students = students;
        this.courseManager = courseManager;
    }

    public void addStudent(Scanner scanner) {
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

    public void removeStudent(Scanner scanner) {
        System.out.print("Enter Student ID to remove: ");
        String id = scanner.nextLine();
        students.removeIf(student -> student.getId().equals(id));
        System.out.println("Student removed successfully.");
    }
}