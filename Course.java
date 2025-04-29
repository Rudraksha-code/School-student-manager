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