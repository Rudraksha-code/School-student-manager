import java.util.ArrayList;

class Teacher extends Person {
    private ArrayList<String> courses;

    public Teacher(String id, String name, String dob, ArrayList<String> courses) {
        super(id, name, dob);
        this.courses = courses;
    }

    public ArrayList<String> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<String> courses) {
        this.courses = courses;
    }

    // Static method to show teacher details for a specific course
    public static void showTeacherDetails(ArrayList<Teacher> teachers, ArrayList<Course> courses, String courseName) {
        // First find the teacher assigned to teach this course in the system
        String assignedTeacherName = null;
        for (Course course : courses) {
            if (course.getName().equals(courseName)) {
                assignedTeacherName = course.getTeacher();
                break;
            }
        }
        
        if (assignedTeacherName != null) {
            // Find the teacher by name
            for (Teacher teacher : teachers) {
                if (teacher.getName().equals(assignedTeacherName)) {
                    System.out.println("Teacher Details:");
                    System.out.println("  ID: " + teacher.getId());
                    System.out.println("  Name: " + teacher.getName());
                    System.out.println("  DOB: " + teacher.getDob());
                    System.out.println("  Courses: " + String.join(", ", teacher.getCourses()));
                    return;
                }
            }
        }
        
        System.out.println("No teacher found for this course.");
    }
}
