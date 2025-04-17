import java.util.ArrayList;

class Course {
    private String name;
    private String teacher;
    private int semester;
    private int period;

    public Course(String name, int semester, int period, String teacher) {
        this.name = name;
        this.teacher = teacher; 
        this.semester = semester;
        this.period = period;
    }
    public static ArrayList<Course> initializeCourses(ArrayList<String> teacherNames, ArrayList<ArrayList<String>> teacherCourses) {
        ArrayList<Course> courses = new ArrayList<>();
        int semester = 1; // Start with semester 1
        int period = 1;   // Start with period 1

        for (int i = 0; i < teacherNames.size(); i++) {
            String teacherName = teacherNames.get(i);
            ArrayList<String> coursesForTeacher = teacherCourses.get(i);
            for (String courseName : coursesForTeacher) {
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
        }
        return courses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() { 
        return teacher;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
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