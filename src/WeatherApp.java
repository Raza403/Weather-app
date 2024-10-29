import javafx.application.Application;
import javafx.stage.Stage;

public class WeatherApp extends Application {

    /**
     * The main entry point for all JavaFX applications.
     * This method is called after the application is launched.
     *
     * @param primaryStage the primary stage for this application,
     *                     onto which the GUI will be set up
     */
    @Override
    public void start(Stage primaryStage) {
        // Initialize the GUI class with the primary stage
        GUI gui = new GUI(primaryStage);
        
        // Set the title of the primary stage window
        primaryStage.setTitle("Weather Information App");
        
        // Display the primary stage to the user
        primaryStage.show();
    }

    /**
     * Launches the WeatherApp application.
     * 
     * @param args the command line arguments passed to the application
     */
    public static void launchApp(String[] args) {
        // Launches the JavaFX application
        launch(args);
    }
}
