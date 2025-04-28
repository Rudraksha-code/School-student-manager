import java.util.ArrayList;

class Course {
    private String name;
    private String teacher;
    private int semester;
    private int period;

    Course(String name, int semester, int period, String teacher) {
        this.name = name;
        this.teacher = teacher; 
        this.semester = semester;
        this.period = period;
    }
    
    static ArrayList<Course> initializeEachCourse(ArrayList<String> courseNames, ArrayList<Teacher> teachers) {
        ArrayList<Course> courses = new ArrayList<>();
        
        int semester = 1; // Start with semester 1
        int period = 1;   // Start with period 1
        
        // Get a list of teacher names for direct assignment
        ArrayList<String> teacherNames = new ArrayList<>();
        for (Teacher teacher : teachers) {
            teacherNames.add(teacher.getName());
        }
        
        for (int i = 0; i < courseNames.size(); i++) {
            String courseName = courseNames.get(i);
            
            // Assign teacher using modulo to cycle through the available teachers
            String assignedTeacher = "Unassigned";
            if (!teacherNames.isEmpty()) {
                assignedTeacher = teacherNames.get(i % teacherNames.size());
            }
            
            courses.add(new Course(courseName, semester, period, assignedTeacher));
            
            period++; // Increment period for the next course
            if (period > 4) { // Reset period after 4 and move to the next semester
                period = 1;
                semester++;
                if (semester > 2) { // Only two semesters allowed
                    semester = 1; // Reset semester to 1 if it exceeds 2
                }
            }
        }
        return courses;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    String getTeacher() { 
        return teacher;
    }

    int getSemester() {
        return semester;
    }

    void setSemester(int semester) {
        this.semester = semester;
    }

    int getPeriod() {
        return period;
    }

    void setPeriod(int period) {
        this.period = period;
    }

    public String toString() {
        return "Course{" +
                "name='" + name + '\'' +
                ", semester=" + semester +
                ", period=" + period +
                '}';
    }
}