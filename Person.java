class Person {
    private String id;
    private String name;
    private String dob;

    Person(String id, String name, String dob) {
        this.id = id;
        this.name = name;
        this.dob = dob;
    }

    String getId() {
        return id;
    }

    String getName() {
        return name;
    }

    String getDob() {
        return dob;
    }

    void setName(String name) {
        this.name = name;
    }

    void setDob(String dob) {
        this.dob = dob;
    }
}