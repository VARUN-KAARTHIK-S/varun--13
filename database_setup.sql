-- Attendance Management System Database Setup
-- Run this script in MySQL to create the database and tables

CREATE DATABASE IF NOT EXISTS attendance_db;
USE attendance_db;

-- Faculty table
CREATE TABLE IF NOT EXISTS faculty (
    faculty_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Students table
CREATE TABLE IF NOT EXISTS students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    roll_number VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    class_name VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(15),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Attendance records table
CREATE TABLE IF NOT EXISTS attendance (
    record_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT,
    class_name VARCHAR(50),
    date DATE,
    time TIME,
    status ENUM('PRESENT', 'ABSENT', 'ON_DUTY') NOT NULL,
    marked_by VARCHAR(100),
    period INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE
);

-- Faculty classes mapping
CREATE TABLE IF NOT EXISTS faculty_classes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    faculty_id INT,
    class_name VARCHAR(50),
    FOREIGN KEY (faculty_id) REFERENCES faculty(faculty_id) ON DELETE CASCADE
);

-- Insert sample data
INSERT INTO faculty (name, department, email) VALUES 
('Dr. John Smith', 'Computer Science', 'john.smith@college.edu'),
('Prof. Sarah Johnson', 'Mathematics', 'sarah.johnson@college.edu'),
('Dr. Mike Wilson', 'Physics', 'mike.wilson@college.edu');

INSERT INTO faculty_classes (faculty_id, class_name) VALUES 
(1, 'CS-A'),
(1, 'CS-B'),
(2, 'MATH-A'),
(3, 'PHY-A');

INSERT INTO students (roll_number, name, class_name, email, phone) VALUES 
('CS001', 'Alice Brown', 'CS-A', 'alice@student.edu', '1234567890'),
('CS002', 'Bob Davis', 'CS-A', 'bob@student.edu', '1234567891'),
('CS003', 'Charlie Evans', 'CS-B', 'charlie@student.edu', '1234567892'),
('MATH001', 'Diana Foster', 'MATH-A', 'diana@student.edu', '1234567893'),
('PHY001', 'Eve Garcia', 'PHY-A', 'eve@student.edu', '1234567894');

-- Create indexes for better performance
CREATE INDEX idx_attendance_date ON attendance(date);
CREATE INDEX idx_attendance_class ON attendance(class_name);
CREATE INDEX idx_students_class ON students(class_name);