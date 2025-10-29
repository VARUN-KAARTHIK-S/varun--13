@echo off
echo ========================================
echo  Attendance Management System Setup
echo ========================================

echo.
echo Step 1: Setting up database...
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS attendance_db; USE attendance_db; SOURCE database_setup.sql;"

if %errorlevel% neq 0 (
    echo Database setup failed. Please ensure MySQL is running and try again.
    echo.
    echo Manual setup:
    echo 1. Start MySQL service
    echo 2. Run: mysql -u root -p ^< database_setup.sql
    echo.
    pause
    exit /b 1
)

echo.
echo Step 2: Compiling application...
javac --module-path javafx-sdk-17.0.17\lib --add-modules javafx.controls,javafx.fxml -cp mysql-connector-j-9.4.0.jar -d out src\attendance\*.java src\attendance\controller\*.java src\attendance\model\*.java src\attendance\util\*.java

if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo.
echo Step 3: Starting Attendance Management System...
java --module-path javafx-sdk-17.0.17\lib --add-modules javafx.controls,javafx.fxml -cp "out;mysql-connector-j-9.4.0.jar" attendance.AttendanceApp

echo.
echo Application closed.
pause