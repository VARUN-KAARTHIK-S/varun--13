package attendance;

import attendance.controller.MainController;
import attendance.util.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AttendanceApp extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize database
            DatabaseManager.initializeDatabase();
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
            Scene scene = new Scene(loader.load(), 1200, 800);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            
            primaryStage.setTitle("Attendance Management System");
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            
            // Set application icon
            try {
                primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logo.png")));
            } catch (Exception e) {
                System.out.println("Logo not found, using default icon");
            }
            
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}