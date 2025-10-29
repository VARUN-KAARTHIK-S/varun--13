@echo off
echo Starting Attendance Management System...

set JAVAFX_PATH=javafx-sdk-17.0.17\lib
set MYSQL_JAR=mysql-connector-j-9.4.0.jar

echo Compiling Java files...
javac --module-path %JAVAFX_PATH% --add-modules javafx.controls,javafx.fxml -cp %MYSQL_JAR% -d out src\attendance\*.java src\attendance\controller\*.java src\attendance\model\*.java src\attendance\util\*.java

if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Running application...
java --module-path %JAVAFX_PATH% --add-modules javafx.controls,javafx.fxml -cp "out;%MYSQL_JAR%" attendance.AttendanceApp

pause