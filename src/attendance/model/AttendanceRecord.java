package attendance.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class AttendanceRecord {
    private int recordId;
    private int studentId;
    private String className;
    private LocalDate date;
    private LocalTime time;
    private AttendanceStatus status;
    private String markedBy;
    private int period;
    
    public enum AttendanceStatus {
        PRESENT("P"), ABSENT("A"), ON_DUTY("OD");
        
        private String code;
        
        AttendanceStatus(String code) {
            this.code = code;
        }
        
        public String getCode() { return code; }
        
        @Override
        public String toString() { return code; }
    }
    
    public AttendanceRecord() {}
    
    public AttendanceRecord(int studentId, String className, LocalDate date, 
                          AttendanceStatus status, String markedBy, int period) {
        this.studentId = studentId;
        this.className = className;
        this.date = date;
        this.time = LocalTime.now();
        this.status = status;
        this.markedBy = markedBy;
        this.period = period;
    }
    
    // Getters and Setters
    public int getRecordId() { return recordId; }
    public void setRecordId(int recordId) { this.recordId = recordId; }
    
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }
    
    public AttendanceStatus getStatus() { return status; }
    public void setStatus(AttendanceStatus status) { this.status = status; }
    
    public String getMarkedBy() { return markedBy; }
    public void setMarkedBy(String markedBy) { this.markedBy = markedBy; }
    
    public int getPeriod() { return period; }
    public void setPeriod(int period) { this.period = period; }
}