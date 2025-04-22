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
    
    static ArrayList<Course> initializeCourses(ArrayList<String> teacherNames, ArrayList<ArrayList<String>> teacherCourses) {
        ArrayList<Course> courses = new ArrayList<>();
        ArrayList<String> allCourseNames = new ArrayList<>();
        
        // First, collect all unique course names from all teachers
        for (ArrayList<String> coursesForTeacher : teacherCourses) {
            for (String courseName : coursesForTeacher) {
                if (!allCourseNames.contains(courseName)) {
                    allCourseNames.add(courseName);
                }
            }
        }
        
        // Now assign each course to a teacher based on index
        int semester = 1; // Start with semester 1
        int period = 1;   // Start with period 1
        
        for (int i = 0; i < Math.min(allCourseNames.size(), 9); i++) { // Limit to 9 courses
            String courseName = allCourseNames.get(i);
            String teacherName = (i < teacherNames.size()) ? teacherNames.get(i) : "Unassigned";
            
            courses.add(new Course(courseName, semester, period, teacherName));
            
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