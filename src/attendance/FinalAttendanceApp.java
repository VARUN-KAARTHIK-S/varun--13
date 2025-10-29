package attendance;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class FinalAttendanceApp extends Application {
    
    private Map<String, List<String>> classes = new HashMap<>();
    private Map<String, Map<String, String>> attendance = new HashMap<>();
    private List<String> facultyList = new ArrayList<>();
    private String currentClass = "";
    private int periodsPerDay = 6;
    
    // UI Components that need to be updated
    private ComboBox<String> studentClassCombo;
    private ComboBox<String> attendanceClassCombo;
    private ComboBox<String> reportClassCombo;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) {
        stage.setTitle("🎓 Attendance Management System");
        
        TabPane tabPane = new TabPane();
        
        Tab facultyTab = new Tab("Faculty Management");
        facultyTab.setContent(createFacultyPane());
        
        Tab studentsTab = new Tab("Student Management");
        studentsTab.setContent(createStudentsPane());
        
        Tab attendanceTab = new Tab("Mark Attendance");
        attendanceTab.setContent(createAttendancePane());
        
        Tab reportsTab = new Tab("Reports");
        reportsTab.setContent(createReportsPane());
        
        Tab settingsTab = new Tab("Settings");
        settingsTab.setContent(createSettingsPane());
        
        tabPane.getTabs().addAll(facultyTab, studentsTab, attendanceTab, reportsTab, settingsTab);
        
        Scene scene = new Scene(tabPane, 1000, 700);
        stage.setScene(scene);
        stage.show();
    }
    
    private VBox createFacultyPane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        
        Label title = new Label("Faculty Management");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        TextField facultyName = new TextField();
        facultyName.setPromptText("Faculty Name");
        
        TextField department = new TextField();
        department.setPromptText("Department");
        
        TextField email = new TextField();
        email.setPromptText("Email");
        
        ListView<String> facultyListView = new ListView<>();
        facultyListView.setPrefHeight(150);
        
        TextField className = new TextField();
        className.setPromptText("Class Name (e.g., CS-A, MATH-101)");
        
        Button addFaculty = new Button("Add Faculty");
        addFaculty.setOnAction(e -> {
            if (!facultyName.getText().isEmpty() && !department.getText().isEmpty()) {
                String faculty = facultyName.getText() + " (" + department.getText() + ")";
                if (!email.getText().isEmpty()) {
                    faculty += " - " + email.getText();
                }
                facultyList.add(faculty);
                facultyListView.getItems().add(faculty);
                showAlert("Success", "Faculty " + facultyName.getText() + " added successfully!");
                facultyName.clear();
                department.clear();
                email.clear();
            } else {
                showAlert("Error", "Please enter faculty name and department");
            }
        });
        
        Button addClass = new Button("Add Class");
        addClass.setOnAction(e -> {
            if (!className.getText().isEmpty()) {
                classes.put(className.getText(), new ArrayList<>());
                updateClassComboBoxes();
                showAlert("Success", "Class " + className.getText() + " added successfully!");
                className.clear();
            } else {
                showAlert("Error", "Please enter class name");
            }
        });
        
        vbox.getChildren().addAll(
            title,
            new Label("Add Faculty:"), facultyName, department, email, addFaculty,
            new Label("Faculty List:"), facultyListView,
            new Separator(),
            new Label("Add Class:"), className, addClass
        );
        return vbox;
    }
    
    private VBox createStudentsPane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        
        Label title = new Label("Student Management");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        studentClassCombo = new ComboBox<>();
        studentClassCombo.setPromptText("Select Class");
        
        TextField rollNumber = new TextField();
        rollNumber.setPromptText("Roll Number");
        
        TextField studentName = new TextField();
        studentName.setPromptText("Student Name");
        
        TextField studentEmail = new TextField();
        studentEmail.setPromptText("Email");
        
        TextField phone = new TextField();
        phone.setPromptText("Phone");
        
        ListView<String> studentsList = new ListView<>();
        studentsList.setPrefHeight(200);
        
        studentClassCombo.setOnAction(e -> {
            String selectedClass = studentClassCombo.getValue();
            if (selectedClass != null) {
                studentsList.getItems().clear();
                studentsList.getItems().addAll(classes.get(selectedClass));
            }
        });
        
        Button addStudent = new Button("Add Student");
        addStudent.setOnAction(e -> {
            String selectedClass = studentClassCombo.getValue();
            if (selectedClass != null && !rollNumber.getText().isEmpty() && !studentName.getText().isEmpty()) {
                String student = rollNumber.getText() + " - " + studentName.getText();
                if (!studentEmail.getText().isEmpty()) {
                    student += " (" + studentEmail.getText() + ")";
                }
                if (!phone.getText().isEmpty()) {
                    student += " [" + phone.getText() + "]";
                }
                
                classes.get(selectedClass).add(student);
                studentsList.getItems().add(student);
                showAlert("Success", "Student added to " + selectedClass);
                rollNumber.clear();
                studentName.clear();
                studentEmail.clear();
                phone.clear();
            } else {
                showAlert("Error", "Please select class and enter roll number and name");
            }
        });
        
        Button removeStudent = new Button("Remove Selected");
        removeStudent.setOnAction(e -> {
            String selected = studentsList.getSelectionModel().getSelectedItem();
            String selectedClass = studentClassCombo.getValue();
            if (selected != null && selectedClass != null) {
                classes.get(selectedClass).remove(selected);
                studentsList.getItems().remove(selected);
                showAlert("Success", "Student removed successfully");
            } else {
                showAlert("Error", "Please select a student to remove");
            }
        });
        
        vbox.getChildren().addAll(
            title, studentClassCombo, rollNumber, studentName, studentEmail, phone,
            addStudent, removeStudent, new Label("Students in Class:"), studentsList
        );
        return vbox;
    }
    
    private VBox createAttendancePane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        
        Label title = new Label("Mark Attendance");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        attendanceClassCombo = new ComboBox<>();
        attendanceClassCombo.setPromptText("Select Class");
        
        ComboBox<Integer> periodCombo = new ComboBox<>();
        for (int i = 1; i <= periodsPerDay; i++) {
            periodCombo.getItems().add(i);
        }
        periodCombo.setPromptText("Period");
        
        TextField facultyField = new TextField("Admin");
        facultyField.setPromptText("Faculty Name");
        
        VBox attendanceBox = new VBox(5);
        Label statsLabel = new Label();
        
        attendanceClassCombo.setOnAction(e -> {
            String selectedClass = attendanceClassCombo.getValue();
            if (selectedClass != null) {
                currentClass = selectedClass;
                attendanceBox.getChildren().clear();
                
                List<String> students = classes.get(selectedClass);
                if (students != null) {
                    for (String student : students) {
                        HBox row = new HBox(10);
                        Label nameLabel = new Label(student);
                        nameLabel.setPrefWidth(300);
                        
                        ToggleGroup group = new ToggleGroup();
                        RadioButton present = new RadioButton("Present");
                        RadioButton absent = new RadioButton("Absent");
                        RadioButton onDuty = new RadioButton("On Duty");
                        
                        present.setToggleGroup(group);
                        absent.setToggleGroup(group);
                        onDuty.setToggleGroup(group);
                        present.setSelected(true);
                        
                        row.getChildren().addAll(nameLabel, present, absent, onDuty);
                        attendanceBox.getChildren().add(row);
                    }
                }
            }
        });
        
        Button markAttendance = new Button("Mark Attendance");
        markAttendance.setOnAction(e -> {
            if (currentClass.isEmpty() || periodCombo.getValue() == null) {
                showAlert("Error", "Please select class and period");
                return;
            }
            
            int present = 0, absent = 0, onDuty = 0;
            String date = LocalDate.now().toString();
            String time = LocalTime.now().toString().substring(0, 5);
            
            for (javafx.scene.Node node : attendanceBox.getChildren()) {
                if (node instanceof HBox) {
                    HBox row = (HBox) node;
                    for (javafx.scene.Node child : row.getChildren()) {
                        if (child instanceof RadioButton && ((RadioButton) child).isSelected()) {
                            String status = ((RadioButton) child).getText();
                            if (status.equals("Present")) present++;
                            else if (status.equals("Absent")) absent++;
                            else if (status.equals("On Duty")) onDuty++;
                        }
                    }
                }
            }
            
            String key = currentClass + "-" + date + "-P" + periodCombo.getValue();
            Map<String, String> record = new HashMap<>();
            record.put("present", String.valueOf(present));
            record.put("absent", String.valueOf(absent));
            record.put("onduty", String.valueOf(onDuty));
            record.put("faculty", facultyField.getText());
            record.put("time", time);
            attendance.put(key, record);
            
            statsLabel.setText(String.format(
                "Attendance marked at %s by %s - Present: %d, Absent: %d, On Duty: %d", 
                time, facultyField.getText(), present, absent, onDuty
            ));
            
            showAlert("Success", "Attendance marked successfully!");
        });
        
        vbox.getChildren().addAll(
            title, attendanceClassCombo, periodCombo, facultyField,
            markAttendance, statsLabel, new ScrollPane(attendanceBox)
        );
        return vbox;
    }
    
    private VBox createReportsPane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        
        Label title = new Label("Attendance Reports");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        reportClassCombo = new ComboBox<>();
        reportClassCombo.setPromptText("Select Class");
        
        DatePicker startDate = new DatePicker(LocalDate.now().minusDays(7));
        DatePicker endDate = new DatePicker(LocalDate.now());
        
        TextArea reportArea = new TextArea();
        reportArea.setPrefRowCount(15);
        reportArea.setEditable(false);
        
        Button generateReport = new Button("Generate Report");
        generateReport.setOnAction(e -> {
            String selectedClass = reportClassCombo.getValue();
            if (selectedClass != null) {
                StringBuilder report = new StringBuilder();
                report.append("📊 ATTENDANCE REPORT\n");
                report.append("===================\n");
                report.append("Class: ").append(selectedClass).append("\n");
                report.append("From: ").append(startDate.getValue()).append(" To: ").append(endDate.getValue()).append("\n\n");
                
                int totalPresent = 0, totalAbsent = 0, totalOnDuty = 0;
                
                for (Map.Entry<String, Map<String, String>> entry : attendance.entrySet()) {
                    if (entry.getKey().startsWith(selectedClass)) {
                        Map<String, String> data = entry.getValue();
                        report.append("📅 ").append(entry.getKey()).append("\n");
                        report.append("   👨‍🏫 Faculty: ").append(data.get("faculty")).append(" at ").append(data.get("time")).append("\n");
                        report.append("   ✅ Present: ").append(data.get("present"));
                        report.append("   ❌ Absent: ").append(data.get("absent"));
                        report.append("   📋 On Duty: ").append(data.get("onduty")).append("\n\n");
                        
                        totalPresent += Integer.parseInt(data.get("present"));
                        totalAbsent += Integer.parseInt(data.get("absent"));
                        totalOnDuty += Integer.parseInt(data.get("onduty"));
                    }
                }
                
                report.append("📈 SUMMARY:\n");
                report.append("===========\n");
                report.append("Total Present: ").append(totalPresent).append("\n");
                report.append("Total Absent: ").append(totalAbsent).append("\n");
                report.append("Total On Duty: ").append(totalOnDuty).append("\n");
                
                if (totalPresent + totalAbsent + totalOnDuty == 0) {
                    report.append("\nNo attendance records found for the selected period.");
                }
                
                reportArea.setText(report.toString());
            } else {
                showAlert("Error", "Please select a class");
            }
        });
        
        vbox.getChildren().addAll(title, reportClassCombo, 
                                 new Label("Start Date:"), startDate,
                                 new Label("End Date:"), endDate,
                                 generateReport, reportArea);
        return vbox;
    }
    
    private VBox createSettingsPane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        
        Label title = new Label("System Settings");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        Spinner<Integer> periodsSpinner = new Spinner<>(1, 10, periodsPerDay);
        TextField instituteName = new TextField("Attendance Management System");
        
        Button saveSettings = new Button("Save Settings");
        saveSettings.setOnAction(e -> {
            periodsPerDay = periodsSpinner.getValue();
            showAlert("Success", "Settings saved! Periods per day: " + periodsPerDay);
        });
        
        vbox.getChildren().addAll(
            title,
            new Label("Periods per Day:"), periodsSpinner,
            new Label("Institute Name:"), instituteName,
            saveSettings
        );
        return vbox;
    }
    
    private void updateClassComboBoxes() {
        if (studentClassCombo != null) {
            studentClassCombo.getItems().clear();
            studentClassCombo.getItems().addAll(classes.keySet());
        }
        if (attendanceClassCombo != null) {
            attendanceClassCombo.getItems().clear();
            attendanceClassCombo.getItems().addAll(classes.keySet());
        }
        if (reportClassCombo != null) {
            reportClassCombo.getItems().clear();
            reportClassCombo.getItems().addAll(classes.keySet());
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}