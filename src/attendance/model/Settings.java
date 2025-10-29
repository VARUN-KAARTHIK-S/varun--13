package attendance.model;

public class Settings {
    private static Settings instance;
    private int periodsPerDay = 6;
    private String instituteName = "Attendance Management System";
    
    private Settings() {}
    
    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }
    
    public int getPeriodsPerDay() { return periodsPerDay; }
    public void setPeriodsPerDay(int periodsPerDay) { this.periodsPerDay = periodsPerDay; }
    
    public String getInstituteName() { return instituteName; }
    public void setInstituteName(String instituteName) { this.instituteName = instituteName; }
}