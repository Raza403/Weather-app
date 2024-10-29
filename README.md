# **Weather Information App**

## **Overview**

The Weather Information App is a JavaFX application that allows users to retrieve and display current weather data and a 3-hour forecast for a specified location. The app uses the OpenWeatherMap API to fetch weather information and presents it in a user-friendly interface.

## **Features**

- Enter a city name to get the current weather information.
- View the temperature, humidity, wind speed, and weather conditions.
- Get a 3-hour forecast for the selected location.
- Choose between Celsius and Fahrenheit for temperature display.
- Maintain a history of searched locations.
- Dynamic background color changes based on the time of day.

## **Requirements**

- Java Development Kit (JDK) 11 or higher
- JavaFX SDK
- Internet connection to fetch weather data

## **Setup Instructions**

### **1\. Clone the Repository**

Clone this repository to your local machine using:

bash

Copy code

git clone &lt;repository-url&gt;

cd WeatherInformationApp

### **2\. Install JavaFX**

Download the JavaFX SDK from the [JavaFX website](https://openjfx.io/). Extract the SDK to a suitable location on your machine.

### **3\. Configure the Project**

Make sure to include the JavaFX libraries in your project. You can add the following dependencies to your build configuration (if using Maven or Gradle) or set up the JavaFX libraries manually in your IDE.

### **4\. Set Up API Key**

Replace the placeholder in WeatherService with your actual OpenWeatherMap API key:

arduino

Copy code

private static final String API_KEY = "your_api_key"; // Replace with your actual API key

### **5\. Compile and Run the Application**

Compile and run the application using the following command (adjust the paths as necessary):

vbnet

Copy code

java --module-path path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -cp "out:lib/\*" WeatherApp

## **How to Use the App**

1. **Launch the Application**: Run the main method in the Main class to start the application.
2. **Enter Location**: In the input field, type the name of the city you want to get weather information for.
3. **Select Temperature Unit**: Use the dropdown to choose between Celsius and Fahrenheit.
4. **Get Weather**: Click the "Get Weather" button to retrieve the weather data.
5. **View Results**: The app will display the current weather conditions, temperature, humidity, wind speed, and a 3-hour forecast.
6. **Check History**: The search history will update with each new query.

## **Implementation Details**

The application is structured as follows:

- **WeatherService Class**: Handles API calls to OpenWeatherMap for fetching current weather and forecast data.
- **WeatherApp Class**: Initializes the JavaFX application and displays the GUI.
- **GUI Class**: Manages the graphical user interface, handles user input, and displays weather information and history.

### **Key Components**

- **JSON Processing**: The application uses the org.json library to parse JSON responses from the API.
- **JavaFX**: The GUI is built using JavaFX, providing a responsive and modern user interface.

## **License**

This project is licensed under the Creative Commons License. See the LICENSE file for more details.

## **Acknowledgments**

- [OpenWeatherMap](https://openweathermap.org/) for providing weather data.
- [JavaFX](https://openjfx.io/) for the GUI framework.