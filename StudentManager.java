import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

class StudentManager {
    private ArrayList<Student> students;
    private CourseManager courseManager;

    StudentManager(ArrayList<Student> students, CourseManager courseManager) {
        this.students = students;
        this.courseManager = courseManager;
    }

    void addStudent(String id, String name, String dob, ArrayList<Integer> marks, ArrayList<Integer> attendance, String[] selectedCourses) {
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

    void changeStudentDetails(Scanner scanner) {
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

    void showStudentDetails(Scanner scanner) {
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
