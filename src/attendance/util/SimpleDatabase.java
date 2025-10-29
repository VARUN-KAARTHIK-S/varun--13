package attendance.util;

import attendance.model.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleDatabase {
    private static SimpleDatabase instance;
    private Map<Integer, Faculty> faculties = new HashMap<>();
    private Map<Integer, Student> students = new HashMap<>();
    private List<AttendanceRecord> attendanceRecords = new ArrayList<>();
    private AtomicInteger facultyIdCounter = new AtomicInteger(1);
    private AtomicInteger studentIdCounter = new AtomicInteger(1);
    private AtomicInteger recordIdCounter = new AtomicInteger(1);
    
    private SimpleDatabase() {
        initializeSampleData();
    }
    
    public static SimpleDatabase getInstance() {
        if (instance == null) {
            instance = new SimpleDatabase();
        }
        return instance;
    }
    
    private void initializeSampleData() {
        // Add sample faculty
        Faculty f1 = new Faculty(1, "Dr. John Smith", "Computer Science", "john@college.edu");
        f1.addClass("CS-A");
        f1.addClass("CS-B");
        faculties.put(1, f1);
        
        Faculty f2 = new Faculty(2, "Prof. Sarah Johnson", "Mathematics", "sarah@college.edu");
        f2.addClass("MATH-A");
        faculties.put(2, f2);
        
        facultyIdCounter.set(3);
        
        // Add sample students
        students.put(1, new Student(1, "CS001", "Alice Brown", "CS-A", "alice@student.edu", "1234567890"));
        students.put(2, new Student(2, "CS002", "Bob Davis", "CS-A", "bob@student.edu", "1234567891"));
        students.put(3, new Student(3, "CS003", "Charlie Evans", "CS-B", "charlie@student.edu", "1234567892"));
        students.put(4, new Student(4, "MATH001", "Diana Foster", "MATH-A", "diana@student.edu", "1234567893"));
        
        studentIdCounter.set(5);
    }
    
    // Faculty operations
    public void addFaculty(Faculty faculty) {
        faculty.setFacultyId(facultyIdCounter.getAndIncrement());
        faculties.put(faculty.getFacultyId(), faculty);
    }
    
    public List<Faculty> getAllFaculty() {
        return new ArrayList<>(faculties.values());
    }
    
    // Student operations
    public void addStudent(Student student) {
        student.setStudentId(studentIdCounter.getAndIncrement());
        students.put(student.getStudentId(), student);
    }
    
    public List<Student> getStudentsByClass(String className) {
        return students.values().stream()
            .filter(s -> s.getClassName().equals(className))
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    public void deleteStudent(int studentId) {
        students.remove(studentId);
        attendanceRecords.removeIf(r -> r.getStudentId() == studentId);
    }
    
    // Attendance operations
    public void markAttendance(AttendanceRecord record) {
        record.setRecordId(recordIdCounter.getAndIncrement());
        attendanceRecords.add(record);
    }
    
    public List<AttendanceRecord> getAttendanceByDateRange(String className, LocalDate startDate, LocalDate endDate) {
        return attendanceRecords.stream()
            .filter(r -> r.getClassName().equals(className))
            .filter(r -> !r.getDate().isBefore(startDate) && !r.getDate().isAfter(endDate))
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    public Student getStudentById(int studentId) {
        return students.get(studentId);
    }
}