import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GUI {
    // GUI components and variables
    private Stage stage; // The primary stage for the application
    private VBox mainLayout; // Layout for the main UI elements
    private TextField locationInput; // Input field for location (city name)
    private Label weatherLabel, temperatureLabel, humidityLabel, windLabel, forecastLabel; // Labels for displaying weather information
    private ImageView weatherIcon; // Image view for displaying weather icons
    private ComboBox<String> unitSelector; // Dropdown for selecting temperature units
    private ListView<String> historyList; // List view to display search history
    private List<String> searchHistory; // List to store search history
    private String unit = "Celsius"; // Default temperature unit
    private String windUnit = "m/s"; // Default wind speed unit

    // Constructor to initialize the GUI
    public GUI(Stage stage) {
        this.stage = stage; // Assign the primary stage
        searchHistory = new ArrayList<>(); // Initialize search history list
        setupLayout(); // Setup the layout of the GUI
    }

    // Method to set up the layout of the GUI
    private void setupLayout() {
        mainLayout = new VBox(10); // Create a vertical box layout with spacing
        mainLayout.setPadding(new Insets(15)); // Set padding for the layout

        // Initialize the location input field
        locationInput = new TextField();
        locationInput.setPromptText("Enter city name..."); // Set prompt text for the input field

        // Create a button to fetch weather data
        Button searchButton = new Button("Get Weather");
        searchButton.setOnAction(e -> fetchWeather()); // Set action to fetch weather when clicked

        // Initialize unit selector combo box
        unitSelector = new ComboBox<>();
        unitSelector.getItems().addAll("Celsius", "Fahrenheit"); // Add temperature options
        unitSelector.setValue("Celsius"); // Set default value
        unitSelector.setOnAction(e -> unit = unitSelector.getValue()); // Update unit based on selection

        // Initialize labels for displaying weather information
        weatherLabel = new Label();
        temperatureLabel = new Label();
        humidityLabel = new Label();
        windLabel = new Label();
        forecastLabel = new Label("3-Hour Forecast:"); // Label for the forecast section
        historyList = new ListView<>(); // List view for displaying search history

        // Initialize the weather icon image view
        weatherIcon = new ImageView();

        // Add all components to the main layout
        mainLayout.getChildren().addAll(locationInput, unitSelector, searchButton, 
            weatherLabel, temperatureLabel, humidityLabel, windLabel, 
            forecastLabel, weatherIcon, historyList);

        // Create a scene with the main layout and set dimensions
        Scene scene = new Scene(mainLayout, 350, 600);
        updateBackgroundColor(); // Update the background color based on time of day
        stage.setScene(scene); // Set the scene for the stage
    }

    // Method to fetch weather data from the API
    private void fetchWeather() {
        String location = locationInput.getText().trim(); // Get and trim the input location
        if (location.isEmpty()) { // Check if the input is empty
            showAlert("Please enter a location."); // Show alert if empty
            return; // Exit the method
        }

        // Fetch weather and forecast data using the WeatherService
        JSONObject data = WeatherService.getWeatherData(location);
        JSONArray forecastData = WeatherService.getForecastData(location);

        // Check if data is valid and contains necessary information
        if (data != null && data.has("main")) {
            // Extract relevant weather information from the JSON response
            JSONObject main = data.getJSONObject("main");
            double temp = main.getDouble("temp"); // Current temperature
            double feelsLike = main.getDouble("feels_like"); // Feels-like temperature
            int humidity = main.getInt("humidity"); // Humidity percentage
            double windSpeed = data.getJSONObject("wind").getDouble("speed"); // Wind speed
            String weatherCondition = data.getJSONArray("weather").getJSONObject(0).getString("main"); // Main weather condition
            String weatherDescription = data.getJSONArray("weather").getJSONObject(0).getString("description"); // Weather description
            String iconCode = data.getJSONArray("weather").getJSONObject(0).getString("icon"); // Weather icon code

            // Convert temperature to the selected unit (Celsius or Fahrenheit)
            if (unit.equals("Fahrenheit")) {
                temp = (temp - 273.15) * 9 / 5 + 32; // Convert to Fahrenheit
                feelsLike = (feelsLike - 273.15) * 9 / 5 + 32; // Convert feels-like temperature
            } else {
                temp = temp - 273.15; // Convert to Celsius
                feelsLike = feelsLike - 273.15; // Convert feels-like temperature
            }

            // Display weather information in the respective labels
            weatherLabel.setText("Condition: " + weatherCondition + " (" + weatherDescription + ")");
            temperatureLabel.setText(String.format("Temperature: %.2f %s (Feels like: %.2f %s)", 
                temp, unit.equals("Celsius") ? "°C" : "°F", feelsLike, unit.equals("Celsius") ? "°C" : "°F"));
            humidityLabel.setText("Humidity: " + humidity + "%");
            windLabel.setText("Wind Speed: " + windSpeed + (windUnit.equals("m/s") ? " m/s" : " kph"));

            // Set the weather icon using a CDN
            String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
            weatherIcon.setImage(new Image(iconUrl));

            // Prepare to display the forecast
            forecastLabel.setText("Forecast:");
            StringBuilder forecastText = new StringBuilder();
            for (int i = 0; i < Math.min(forecastData.length(), 5); i++) {
                JSONObject forecastItem = forecastData.getJSONObject(i);
                double forecastTemp = forecastItem.getJSONObject("main").getDouble("temp") - 273.15; // Convert forecast temperature
                String forecastCondition = forecastItem.getJSONArray("weather").getJSONObject(0).getString("main"); // Forecast condition
                String forecastTime = forecastItem.getString("dt_txt"); // Time of the forecast

                // Append forecast information to the string
                forecastText.append(forecastTime).append(": ").append(forecastCondition)
                    .append(" - ").append(String.format("%.2f", forecastTemp)).append("°C\n");
            }
            forecastLabel.setText(forecastText.toString()); // Display the forecast text

            // Add the search entry to the history list
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); // Get current timestamp
            searchHistory.add(timestamp + " - " + location); // Add to search history
            historyList.getItems().clear(); // Clear the current list view
            historyList.getItems().addAll(searchHistory); // Update list view with the new history

            // Update background color based on time of day
            updateBackgroundColor();

        } else {
            // Show an alert if no weather data is available
            showAlert("Weather data not available. Please check your location input.");
        }
    }

    // Method to update the background color based on the time of day
    private void updateBackgroundColor() {
        int hour = LocalDateTime.now().getHour(); // Get current hour
        if (hour >= 6 && hour < 18) {
            // Set light blue background for daytime
            mainLayout.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        } else if (hour >= 18 && hour < 20) {
            // Set orange background for evening
            mainLayout.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            // Set dark blue background for nighttime
            mainLayout.setBackground(new Background(new BackgroundFill(Color.DARKBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    // Method to show an alert dialog with a specified message
    private void showAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR); // Create an error alert
            alert.setContentText(message); // Set the alert message
            alert.showAndWait(); // Show the alert and wait for it to close
        });
    }
}
