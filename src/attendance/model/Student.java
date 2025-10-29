package attendance.model;

public class Student {
    private int studentId;
    private String rollNumber;
    private String name;
    private String className;
    private String email;
    private String phone;
    
    public Student() {}
    
    public Student(int studentId, String rollNumber, String name, String className, String email, String phone) {
        this.studentId = studentId;
        this.rollNumber = rollNumber;
        this.name = name;
        this.className = className;
        this.email = email;
        this.phone = phone;
    }
    
    // Getters and Setters
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    
    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    @Override
    public String toString() {
        return rollNumber + " - " + name;
    }
}