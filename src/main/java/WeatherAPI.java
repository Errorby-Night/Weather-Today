import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherAPI {

    private final String apiKey;
    private static final String API_ENDPOINT = "http://api.openweathermap.org/data/2.5/forecast?q=%s&appid=%s&units=metric&cnt=25";
    private final String city;

    public WeatherAPI(String apiKey, String city) {
        this.apiKey = apiKey;
        this.city = city;
    }

    public JSONObject getWeatherData() throws IOException {
        String urlString = String.format(API_ENDPOINT, city, apiKey);
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
        InputStream is = conn.getInputStream();
        Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.toString());
        String response = scanner.hasNext() ? scanner.useDelimiter("\\A").next() : "";
        scanner.close();
        JSONObject json = new JSONObject(response);
        return json;
    }
}
