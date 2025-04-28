import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class StudentManager {
    private ArrayList<Student> students;
    private CourseManager courseManager;

    StudentManager(ArrayList<Student> students, CourseManager courseManager) {
        this.students = students;
        this.courseManager = courseManager;
    }

    void addStudent(String id, String name, String dob, ArrayList<Integer> marks, ArrayList<Integer> attendance, String[] selectedCourses) {
        ArrayList<String> enrolledCourses = new ArrayList<>();

        // Update marks and attendance for selected courses
        for (String courseIndexStr : selectedCourses) {
            try {
                int courseIndex = Integer.parseInt(courseIndexStr.trim()) - 1;
                if (courseIndex >= 0 && courseIndex < courseManager.getCourses().size()) {
                    Course course = courseManager.getCourses().get(courseIndex);
                    marks.set(courseIndex, 0); // Initialize marks to 0
                    attendance.set(courseIndex, 0); // Initialize attendance to 0
                    enrolledCourses.add(course.getName() + " (Semester " + course.getSemester() + ", Period " + course.getPeriod() + ")");
                } else {
                    System.out.println("Invalid course number: " + (courseIndex + 1));
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: " + courseIndexStr);
            }
        }

        // Add student details to the list
        students.add(new Student(id, name, dob, marks, attendance));
        
        Main.enrollmentSummary(enrolledCourses);
    }

    boolean removeStudent(String id) {
        boolean removed = students.removeIf(student -> student.getId().equals(id));;
        return removed;
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

    Student findStudentById(String id) {
        return students.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
    }
    
    void updateStudentCourseDetails(Student student, int courseIndex, int marks, int attendance) {
        student.getMarks().set(courseIndex, marks);
        student.getAttendance().set(courseIndex, attendance);
    }
}