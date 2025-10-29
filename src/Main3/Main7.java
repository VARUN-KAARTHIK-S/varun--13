package Main3;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main7 extends Application {

    private static final int TOTAL_PERIODS = 7;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Attendance Management System");

        VBox root = new VBox(15);
        root.setPadding(new Insets(15));
        root.getStyleClass().addAll("root-pane", "main-border"); // root + border

        Label title = new Label("🎓 Attendance Management System");
        title.getStyleClass().add("title-label");

        Button btnMark = new Button("Mark Attendance");
        Button btnShow = new Button("Show Attendance");
        Button btnAdd = new Button("Add Student");
        Button btnRemove = new Button("Remove Student");
        Button btnViewStudents = new Button("View Students");
        Button btnExit = new Button("Exit");

        root.getChildren().addAll(title, btnMark, btnShow, btnAdd, btnRemove, btnViewStudents, btnExit);

        btnMark.setOnAction(e -> markAttendance());
        btnShow.setOnAction(e -> showAttendance());
        btnAdd.setOnAction(e -> addStudent());
        btnRemove.setOnAction(e -> removeStudent());
        btnViewStudents.setOnAction(e -> viewStudents());
        btnExit.setOnAction(e -> stage.close());

        Scene scene = new Scene(root, 400, 350);

        // Apply CSS file
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    // Get today's date
    private String getTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new java.util.Date());
    }

    // Add student
    private void addStudent() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Student");
        dialog.setHeaderText("Enter Student Name");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(name -> {
            try (Connection conn = DBUtil.getConnection()) {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO students(name) VALUES(?)");
                ps.setString(1, name);
                ps.executeUpdate();
                showAlert("Success", "Student added successfully!");
            } catch (SQLException ex) {
                showAlert("Error", ex.getMessage());
            }
        });
    }

    // Remove student
    private void removeStudent() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Remove Student");
        dialog.setHeaderText("Enter Roll Number to Remove");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(roll -> {
            try (Connection conn = DBUtil.getConnection()) {
                PreparedStatement ps = conn.prepareStatement("DELETE FROM students WHERE roll=?");
                ps.setInt(1, Integer.parseInt(roll));
                int rows = ps.executeUpdate();
                if (rows > 0) showAlert("Success", "Student removed!");
                else showAlert("Info", "No student found!");
            } catch (SQLException ex) {
                showAlert("Error", ex.getMessage());
            }
        });
    }

    // View students
    private void viewStudents() {
        StringBuilder sb = new StringBuilder();
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM students ORDER BY roll")) {

            while (rs.next()) {
                sb.append(rs.getInt("roll")).append(" - ").append(rs.getString("name")).append("\n");
            }
        } catch (SQLException ex) {
            showAlert("Error", ex.getMessage());
            return;
        }

        if (sb.length() == 0) sb.append("No students found!");
        showAlert("Students List", sb.toString());
    }

    // Mark attendance for selected period
    private void markAttendance() {
        TextInputDialog pdialog = new TextInputDialog("1");
        pdialog.setTitle("Select Period");
        pdialog.setHeaderText("Enter period number (1-" + TOTAL_PERIODS + ")");
        Optional<String> pResult = pdialog.showAndWait();

        if (pResult.isEmpty()) return;
        int period = Integer.parseInt(pResult.get());
        if (period < 1 || period > TOTAL_PERIODS) {
            showAlert("Error", "Invalid period number!");
            return;
        }

        String date = getTodayDate();

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM students ORDER BY roll")) {

            while (rs.next()) {
                int roll = rs.getInt("roll");
                String name = rs.getString("name");

                ChoiceDialog<String> cDialog = new ChoiceDialog<>("P", "P", "A", "OD");
                cDialog.setTitle("Mark Attendance");
                cDialog.setHeaderText("Student: " + name + " (Roll: " + roll + ")");
                cDialog.setContentText("Choose status:");
                Optional<String> choice = cDialog.showAndWait();

                if (choice.isPresent()) {
                    saveAttendance(date, roll, period, choice.get());
                }
            }
            showAlert("Success", "Attendance saved for period " + period);

        } catch (SQLException ex) {
            showAlert("Error", ex.getMessage());
        }
    }

    // Save attendance to DB
    private void saveAttendance(String date, int roll, int period, String status) throws SQLException {
        String select = "SELECT * FROM attendance WHERE date=? AND roll=? AND period=?";
        String insert = "INSERT INTO attendance(date, roll, period, status) VALUES(?,?,?,?)";
        String update = "UPDATE attendance SET status=? WHERE date=? AND roll=? AND period=?";

        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(select);
            ps.setString(1, date);
            ps.setInt(2, roll);
            ps.setInt(3, period);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                PreparedStatement up = conn.prepareStatement(update);
                up.setString(1, status);
                up.setString(2, date);
                up.setInt(3, roll);
                up.setInt(4, period);
                up.executeUpdate();
            } else {
                PreparedStatement ins = conn.prepareStatement(insert);
                ins.setString(1, date);
                ins.setInt(2, roll);
                ins.setInt(3, period);
                ins.setString(4, status);
                ins.executeUpdate();
            }
        }
    }

    // Show attendance summary
    private void showAttendance() {
        String date = getTodayDate();
        StringBuilder sb = new StringBuilder("Attendance Summary for " + date + ":\n\n");

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT roll, name FROM students ORDER BY roll")) {

            while (rs.next()) {
                int roll = rs.getInt("roll");
                String name = rs.getString("name");

                PreparedStatement ps = conn.prepareStatement("SELECT status FROM attendance WHERE date=? AND roll=?");
                ps.setString(1, date);
                ps.setInt(2, roll);
                ResultSet attRs = ps.executeQuery();

                int p = 0, a = 0, od = 0;
                while (attRs.next()) {
                    String s = attRs.getString("status");
                    if (s.equals("P")) p++;
                    else if (s.equals("A")) a++;
                    else if (s.equals("OD")) od++;
                }

                sb.append(String.format("Roll %d - %s | P:%d  A:%d  OD:%d%n", roll, name, p, a, od));
            }

            showAlert("Attendance Report", sb.toString());

        } catch (SQLException ex) {
            showAlert("Error", ex.getMessage());
        }
    }

    // Helper alert
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
