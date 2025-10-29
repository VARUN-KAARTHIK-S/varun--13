package attendance.controller;

import attendance.model.*;
import attendance.util.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;

import java.net.URL;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainController implements Initializable {
    
    @FXML private TabPane mainTabPane;
    @FXML private Tab facultyTab, studentsTab, attendanceTab, reportsTab, settingsTab;
    
    // Faculty Tab
    @FXML private TextField facultyNameField, facultyDeptField, facultyEmailField;
    @FXML private ListView<Faculty> facultyListView;
    @FXML private TextField classNameField;
    @FXML private ListView<String> classListView;
    
    // Students Tab
    @FXML private ComboBox<String> classComboBox;
    @FXML private TextField rollNumberField, studentNameField, studentEmailField, studentPhoneField;
    @FXML private TableView<Student> studentsTable;
    @FXML private TableColumn<Student, String> rollCol, nameCol, emailCol, phoneCol;
    
    // Attendance Tab
    @FXML private ComboBox<String> attendanceClassCombo;
    @FXML private ComboBox<Integer> periodCombo;
    @FXML private TextField facultyNameAttendance;
    @FXML private VBox studentAttendanceBox;
    @FXML private Label attendanceStatsLabel;
    
    // Reports Tab
    @FXML private ComboBox<String> reportClassCombo;
    @FXML private DatePicker startDatePicker, endDatePicker;
    @FXML private TableView<AttendanceRecord> reportTable;
    @FXML private Label reportStatsLabel;
    
    // Settings Tab
    @FXML private Spinner<Integer> periodsSpinner;
    @FXML private TextField instituteNameField;
    
    private ObservableList<Faculty> facultyList = FXCollections.observableArrayList();
    private ObservableList<Student> studentsList = FXCollections.observableArrayList();
    private String currentFacultyName = "Admin";
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupFacultyTab();
        setupStudentsTab();
        setupAttendanceTab();
        setupReportsTab();
        setupSettingsTab();
        loadData();
    }
    
    private void setupFacultyTab() {
        facultyListView.setItems(facultyList);
        facultyListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                classListView.setItems(FXCollections.observableArrayList(newVal.getClasses()));
            }
        });
    }
    
    private void setupStudentsTab() {
        rollCol.setCellValueFactory(new PropertyValueFactory<>("rollNumber"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        studentsTable.setItems(studentsList);
        
        classComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadStudentsByClass(newVal);
            }
        });
    }
    
    private void setupAttendanceTab() {
        for (int i = 1; i <= Settings.getInstance().getPeriodsPerDay(); i++) {
            periodCombo.getItems().add(i);
        }
        periodCombo.setValue(1);
        facultyNameAttendance.setText(currentFacultyName);
        
        attendanceClassCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadAttendanceView(newVal);
            }
        });
    }
    
    private void setupReportsTab() {
        startDatePicker.setValue(LocalDate.now().minusDays(7));
        endDatePicker.setValue(LocalDate.now());
    }
    
    private void setupSettingsTab() {
        periodsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, Settings.getInstance().getPeriodsPerDay()));
        instituteNameField.setText(Settings.getInstance().getInstituteName());
    }
    
    private void loadData() {
        facultyList.clear();
        facultyList.addAll(DatabaseManager.getAllFaculty());
        
        // Update class combo boxes
        List<String> allClasses = facultyList.stream()
            .flatMap(f -> f.getClasses().stream())
            .distinct()
            .collect(Collectors.toList());
        
        classComboBox.setItems(FXCollections.observableArrayList(allClasses));
        attendanceClassCombo.setItems(FXCollections.observableArrayList(allClasses));
        reportClassCombo.setItems(FXCollections.observableArrayList(allClasses));
    }
    
    @FXML
    private void addFaculty() {
        String name = facultyNameField.getText().trim();
        String dept = facultyDeptField.getText().trim();
        String email = facultyEmailField.getText().trim();
        
        if (name.isEmpty() || dept.isEmpty()) {
            showAlert("Input Error", "Name and Department are required");
            return;
        }
        
        Faculty faculty = new Faculty(0, name, dept, email);
        DatabaseManager.addFaculty(faculty);
        facultyList.add(faculty);
        clearFacultyFields();
        showAlert("Success", "Faculty added successfully");
    }
    
    @FXML
    private void addClassToFaculty() {
        Faculty selectedFaculty = facultyListView.getSelectionModel().getSelectedItem();
        String className = classNameField.getText().trim();
        
        if (selectedFaculty == null || className.isEmpty()) {
            showAlert("Input Error", "Select faculty and enter class name");
            return;
        }
        
        selectedFaculty.addClass(className);
        classListView.setItems(FXCollections.observableArrayList(selectedFaculty.getClasses()));
        classNameField.clear();
        loadData(); // Refresh combo boxes
    }
    
    @FXML
    private void addStudent() {
        String rollNumber = rollNumberField.getText().trim();
        String name = studentNameField.getText().trim();
        String email = studentEmailField.getText().trim();
        String phone = studentPhoneField.getText().trim();
        String className = classComboBox.getValue();
        
        if (rollNumber.isEmpty() || name.isEmpty() || className == null) {
            showAlert("Input Error", "Roll Number, Name, and Class are required");
            return;
        }
        
        Student student = new Student(0, rollNumber, name, className, email, phone);
        DatabaseManager.addStudent(student);
        loadStudentsByClass(className);
        clearStudentFields();
        showAlert("Success", "Student added successfully");
    }
    
    @FXML
    private void removeStudent() {
        Student selected = studentsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error", "Please select a student to remove");
            return;
        }
        
        DatabaseManager.deleteStudent(selected.getStudentId());
        studentsList.remove(selected);
        showAlert("Success", "Student removed successfully");
    }
    
    private void loadStudentsByClass(String className) {
        studentsList.clear();
        studentsList.addAll(DatabaseManager.getStudentsByClass(className));
    }
    
    private void loadAttendanceView(String className) {
        List<Student> students = DatabaseManager.getStudentsByClass(className);
        studentAttendanceBox.getChildren().clear();
        
        for (Student student : students) {
            HBox studentRow = createStudentAttendanceRow(student);
            studentAttendanceBox.getChildren().add(studentRow);
        }
    }
    
    private HBox createStudentAttendanceRow(Student student) {
        HBox row = new HBox(10);
        row.setPadding(new Insets(5));
        
        Label nameLabel = new Label(student.getRollNumber() + " - " + student.getName());
        nameLabel.setPrefWidth(200);
        
        ToggleGroup group = new ToggleGroup();
        RadioButton presentBtn = new RadioButton("Present");
        RadioButton absentBtn = new RadioButton("Absent");
        RadioButton onDutyBtn = new RadioButton("On Duty");
        
        presentBtn.setToggleGroup(group);
        absentBtn.setToggleGroup(group);
        onDutyBtn.setToggleGroup(group);
        presentBtn.setSelected(true);
        
        row.getChildren().addAll(nameLabel, presentBtn, absentBtn, onDutyBtn);
        row.setUserData(student);
        
        return row;
    }
    
    @FXML
    private void markAttendance() {
        String className = attendanceClassCombo.getValue();
        Integer period = periodCombo.getValue();
        
        if (className == null || period == null) {
            showAlert("Input Error", "Please select class and period");
            return;
        }
        
        int presentCount = 0, absentCount = 0, onDutyCount = 0;
        
        for (javafx.scene.Node node : studentAttendanceBox.getChildren()) {
            if (node instanceof HBox) {
                HBox row = (HBox) node;
                Student student = (Student) row.getUserData();
                
                AttendanceRecord.AttendanceStatus status = AttendanceRecord.AttendanceStatus.PRESENT;
                for (javafx.scene.Node child : row.getChildren()) {
                    if (child instanceof RadioButton && ((RadioButton) child).isSelected()) {
                        String text = ((RadioButton) child).getText();
                        if (text.equals("Absent")) status = AttendanceRecord.AttendanceStatus.ABSENT;
                        else if (text.equals("On Duty")) status = AttendanceRecord.AttendanceStatus.ON_DUTY;
                        break;
                    }
                }
                
                AttendanceRecord record = new AttendanceRecord(
                    student.getStudentId(), className, LocalDate.now(),
                    status, currentFacultyName, period
                );
                DatabaseManager.markAttendance(record);
                
                switch (status) {
                    case PRESENT: presentCount++; break;
                    case ABSENT: absentCount++; break;
                    case ON_DUTY: onDutyCount++; break;
                }
            }
        }
        
        attendanceStatsLabel.setText(String.format(
            "Attendance marked at %s - Present: %d, Absent: %d, On Duty: %d",
            LocalTime.now().toString().substring(0, 5), presentCount, absentCount, onDutyCount
        ));
        
        showAlert("Success", "Attendance marked successfully");
    }
    
    @FXML
    private void generateReport() {
        String className = reportClassCombo.getValue();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        
        if (className == null || startDate == null || endDate == null) {
            showAlert("Input Error", "Please select class and date range");
            return;
        }
        
        List<AttendanceRecord> records = DatabaseManager.getAttendanceByDateRange(className, startDate, endDate);
        
        long presentCount = records.stream().filter(r -> r.getStatus() == AttendanceRecord.AttendanceStatus.PRESENT).count();
        long absentCount = records.stream().filter(r -> r.getStatus() == AttendanceRecord.AttendanceStatus.ABSENT).count();
        long onDutyCount = records.stream().filter(r -> r.getStatus() == AttendanceRecord.AttendanceStatus.ON_DUTY).count();
        
        reportStatsLabel.setText(String.format(
            "Report for %s (%s to %s): Present: %d, Absent: %d, On Duty: %d",
            className, startDate, endDate, presentCount, absentCount, onDutyCount
        ));
    }
    
    @FXML
    private void saveSettings() {
        Settings.getInstance().setPeriodsPerDay(periodsSpinner.getValue());
        Settings.getInstance().setInstituteName(instituteNameField.getText());
        
        // Update period combo box
        periodCombo.getItems().clear();
        for (int i = 1; i <= Settings.getInstance().getPeriodsPerDay(); i++) {
            periodCombo.getItems().add(i);
        }
        periodCombo.setValue(1);
        
        showAlert("Success", "Settings saved successfully");
    }
    
    private void clearFacultyFields() {
        facultyNameField.clear();
        facultyDeptField.clear();
        facultyEmailField.clear();
    }
    
    private void clearStudentFields() {
        rollNumberField.clear();
        studentNameField.clear();
        studentEmailField.clear();
        studentPhoneField.clear();
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}