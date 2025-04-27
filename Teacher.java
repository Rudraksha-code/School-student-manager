import java.util.ArrayList;

class Teacher extends Person {
    private ArrayList<String> courses;

    Teacher(String id, String name, String dob, ArrayList<String> courses) {
        super(id, name, dob);
        this.courses = courses;
    }

    ArrayList<String> getCourses() {
        return courses;
    }

    void setCourses(ArrayList<String> courses) {
        this.courses = courses;
    }

    // Static method to show teacher details for a specific course
    static Teacher showTeacherDetails(ArrayList<Teacher> teachers, ArrayList<Course> courses, String courseName) {
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
                    return teacher;
                }
            }
        }
        // If no teacher is found, return null
        return null;
    }
}