# 🎓 Attendance Management System

A comprehensive JavaFX-based attendance management system for educational institutions.

## ✨ Features

### Faculty Management
- Add faculty with department details
- Assign classes to faculty members
- Email integration for faculty

### Student Management
- Add/remove students by class
- Student details with roll number, name, email, phone
- Class-wise student organization

### Attendance System
- Mark attendance (Present/Absent/On Duty)
- Period-wise attendance tracking
- Real-time statistics display
- Faculty tracking (who marked attendance)
- Time-stamped records

### Reports & Analytics
- Date range attendance reports
- Class-wise attendance summary
- Present/Absent/On Duty counts
- Detailed attendance history

### Settings
- Configurable periods per day
- Institute name customization
- System preferences

## 🚀 Setup Instructions

### Prerequisites
- Java 17 or higher
- JavaFX SDK 17+
- MySQL Server
- MySQL Connector/J

### Database Setup
1. Install MySQL Server
2. Run the `database_setup.sql` script:
   ```sql
   mysql -u root -p < database_setup.sql
   ```
3. Update database credentials in `DatabaseManager.java` if needed

### Running the Application
1. Ensure JavaFX is in your module path
2. Compile and run:
   ```bash
   javac --module-path "path/to/javafx/lib" --add-modules javafx.controls,javafx.fxml -d out src/attendance/*.java src/attendance/*/*.java
   java --module-path "path/to/javafx/lib" --add-modules javafx.controls,javafx.fxml -cp out;mysql-connector-j-9.4.0.jar attendance.AttendanceApp
   ```

## 📁 Project Structure
```
src/
├── attendance/
│   ├── AttendanceApp.java          # Main application
│   ├── controller/
│   │   └── MainController.java     # UI controller
│   ├── model/
│   │   ├── Faculty.java           # Faculty model
│   │   ├── Student.java           # Student model
│   │   ├── AttendanceRecord.java  # Attendance model
│   │   └── Settings.java          # Settings model
│   └── util/
│       └── DatabaseManager.java   # Database operations
resources/
├── view/
│   └── main.fxml                  # Main UI layout
├── css/
│   └── style.css                  # Application styling
└── images/
    └── logo.png                   # Application logo
```

## 🎯 Usage Guide

### 1. Faculty Management
- Go to "Faculty Management" tab
- Add faculty with name, department, and email
- Select faculty and add classes they teach

### 2. Student Management
- Go to "Student Management" tab
- Select a class from dropdown
- Add students with roll number, name, email, phone
- Remove students using the "Remove Selected" button

### 3. Mark Attendance
- Go to "Mark Attendance" tab
- Select class and period
- Mark each student as Present (P), Absent (A), or On Duty (OD)
- Click "Mark Attendance" to save
- View real-time statistics

### 4. Generate Reports
- Go to "Reports" tab
- Select class and date range
- Click "Generate Report" to view attendance summary
- See detailed breakdown of P/A/OD counts

### 5. System Settings
- Go to "Settings" tab
- Configure periods per day (1-10)
- Set institute name
- Save settings

## 🔧 Technical Details

- **Framework**: JavaFX 17
- **Database**: MySQL
- **Architecture**: MVC Pattern
- **Styling**: CSS with modern design
- **Data Persistence**: JDBC with MySQL

## 📊 Database Schema

### Tables
- `faculty` - Faculty information and departments
- `students` - Student details and class assignments
- `attendance` - Attendance records with timestamps
- `faculty_classes` - Faculty-class mappings

### Key Features
- Foreign key constraints for data integrity
- Automatic timestamps for audit trails
- Indexed columns for performance
- Sample data included

## 🎨 UI Features

- Modern, responsive design
- Tab-based navigation
- Real-time statistics
- Professional color scheme
- Intuitive user interface
- Form validation and error handling

## 📈 Future Enhancements

- Export reports to PDF/Excel
- Email notifications for attendance
- Biometric integration
- Mobile app companion
- Advanced analytics dashboard
- Backup and restore functionality