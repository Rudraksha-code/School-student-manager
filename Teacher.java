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
    public static void showTeacherDetails(ArrayList<Teacher> teachers, String courseName) {
        boolean teacherFound = false;

        for (Teacher teacher : teachers) {
            if (teacher.getCourses().contains(courseName)) {
                if (!teacherFound) {
                    System.out.println("Teacher Details:");
                    teacherFound = true;
                }
                System.out.println("  ID: " + teacher.getId());
                System.out.println("  Name: " + teacher.getName());
                System.out.println("  DOB: " + teacher.getDob());
                System.out.println("  Courses: " + String.join(", ", teacher.getCourses())); // Display all courses
                System.out.println(); // Add a blank line for better readability
            }
        }

        if (!teacherFound) {
            System.out.println("No teacher found for this course.");
        }
    }
}
