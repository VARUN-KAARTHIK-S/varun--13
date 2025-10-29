@echo off
echo Starting Attendance Management System...

javac --module-path javafx-sdk-17.0.17\lib --add-modules javafx.controls,javafx.fxml -cp mysql-connector-j-9.4.0.jar -d out src\attendance\*.java src\attendance\controller\*.java src\attendance\model\*.java src\attendance\util\*.java

java --module-path javafx-sdk-17.0.17\lib --add-modules javafx.controls,javafx.fxml -cp "out;mysql-connector-j-9.4.0.jar" attendance.AttendanceApp

pause