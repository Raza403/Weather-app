import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONArray;

public class WeatherService {

    // API key for OpenWeatherMap; replace with your actual API key
    private static final String API_KEY = "Your API Key Here"; 

    // URL for fetching current weather data for a given location
    private static final String CURRENT_API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s";
    
    // URL for fetching forecast data for a given location
    private static final String FORECAST_API_URL = "http://api.openweathermap.org/data/2.5/forecast?q=%s&appid=%s";

    /**
     * Fetches the current weather data for a specified location.
     *
     * @param location the city name for which weather data is requested
     * @return a JSONObject containing the current weather data
     */
    public static JSONObject getWeatherData(String location) {
        // Format the URL with the location and API key and fetch the response
        return getApiResponse(String.format(CURRENT_API_URL, location, API_KEY));
    }

    /**
     * Fetches the forecast data for a specified location.
     *
     * @param location the city name for which forecast data is requested
     * @return a JSONArray containing the list of forecast data, or null if unavailable
     */
    public static JSONArray getForecastData(String location) {
        // Format the URL with the location and API key and fetch the response
        JSONObject response = getApiResponse(String.format(FORECAST_API_URL, location, API_KEY));
        
        // Check if the response contains the "list" key, which holds forecast data
        if (response != null && response.has("list")) {
            return response.getJSONArray("list");
        }
        
        return null;
    }

    /**
     * Helper method that makes an API request to the specified URL and returns the JSON response.
     *
     * @param urlString the complete URL as a string for the API request
     * @return a JSONObject containing the API response, or null if an error occurs
     */
    private static JSONObject getApiResponse(String urlString) {
        try {
            // Create a URL object from the urlString
            URL url = new URL(urlString);
            
            // Open a connection and set the request method to GET
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Read the input stream from the connection
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            
            // Append each line of the response to the StringBuilder
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            
            // Close the reader
            reader.close();

            // Convert the response to a JSONObject and return it
            return new JSONObject(response.toString());
        } catch (Exception e) {
            // Print error message if an exception occurs and return null
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}
