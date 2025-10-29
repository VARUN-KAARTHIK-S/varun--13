package attendance.model;

import java.util.ArrayList;
import java.util.List;

public class Faculty {
    private int facultyId;
    private String name;
    private String department;
    private String email;
    private List<String> classes;
    
    public Faculty() {
        this.classes = new ArrayList<>();
    }
    
    public Faculty(int facultyId, String name, String department, String email) {
        this.facultyId = facultyId;
        this.name = name;
        this.department = department;
        this.email = email;
        this.classes = new ArrayList<>();
    }
    
    // Getters and Setters
    public int getFacultyId() { return facultyId; }
    public void setFacultyId(int facultyId) { this.facultyId = facultyId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public List<String> getClasses() { return classes; }
    public void setClasses(List<String> classes) { this.classes = classes; }
    
    public void addClass(String className) {
        if (!classes.contains(className)) {
            classes.add(className);
        }
    }
    
    public void removeClass(String className) {
        classes.remove(className);
    }
    
    @Override
    public String toString() {
        return name + " (" + department + ")";
    }
}