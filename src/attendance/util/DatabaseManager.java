package attendance.util;

import attendance.model.*;
import java.time.LocalDate;
import java.util.List;

public class DatabaseManager {
    private static SimpleDatabase db = SimpleDatabase.getInstance();
    
    public static void initializeDatabase() {
        // Database is initialized in SimpleDatabase constructor
    }
    
    // Faculty operations
    public static void addFaculty(Faculty faculty) {
        db.addFaculty(faculty);
    }
    
    public static List<Faculty> getAllFaculty() {
        return db.getAllFaculty();
    }
    
    // Student operations
    public static void addStudent(Student student) {
        db.addStudent(student);
    }
    
    public static List<Student> getStudentsByClass(String className) {
        return db.getStudentsByClass(className);
    }
    
    public static void deleteStudent(int studentId) {
        db.deleteStudent(studentId);
    }
    
    // Attendance operations
    public static void markAttendance(AttendanceRecord record) {
        db.markAttendance(record);
    }
    
    public static List<AttendanceRecord> getAttendanceByDateRange(String className, LocalDate startDate, LocalDate endDate) {
        return db.getAttendanceByDateRange(className, startDate, endDate);
    }
}