import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private String apiKey;

    public void init() throws ServletException {
        apiKey = getServletContext().getInitParameter("apiKey");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String city = request.getParameter("city");
        WeatherAPI weatherAPI = new WeatherAPI(apiKey, city);
        try {
            JSONObject weatherData = weatherAPI.getWeatherData();
            JSONObject cityData = weatherData.getJSONObject("city");
            String cityName = cityData.getString("name");
            String countryCode = cityData.getString("country");

            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Result</title>");
            out.println("<link rel='stylesheet' href='src/style.css'>");
            out.println("<link href=\"https://fonts.googleapis.com/css?family=Raleway:200,100,400\" rel=\"stylesheet\" type=\"text/css\" />");
            out.println("</head>");
            out.println("<body>");
            out.println("<div id='nav-bar' class='nav-bar'>");
            out.println("<img src='src/logo-no-background.png' width='500px'>");
            out.println("<div id='links' class='links'>");
            out.println("<form action=\"weather\" method='get'>");
            out.println("<input type=\"text\" class=\"searchbar\" id=\"search\" placeholder=\"Search\" name=\"city\"><button type='submit'><img src='src/search.png' width='20px' height='15px' class='searchbutt'></button>");
            out.println("</form>");
            out.println("<p>About</p>");
            out.println("<p>Contact</p>");
            out.println("</div>");
            out.println("</div>");
            JSONArray weatherList = weatherData.getJSONArray("list");

            // Current weather data
            JSONObject currentData = weatherList.getJSONObject(0);
            String currentDate = currentData.getString("dt_txt");
            String currentIconCode = currentData.getJSONArray("weather").getJSONObject(0).getString("icon");
            String currentDescription = currentData.getJSONArray("weather").getJSONObject(0).getString("description");
            double currentTemperature = currentData.getJSONObject("main").getDouble("temp");
            double currentHumidity = currentData.getJSONObject("main").getDouble("humidity");
            double currentWindSpeed = currentData.getJSONObject("wind").getDouble("speed");

            out.println("<div class='main' id='main'>");
            out.println("<p><span id='location' class='location'>" + cityName + ", " + countryCode + "</span></p>");
            out.println(" <p><span id=\"temp\" class=\"temp\">" + currentTemperature + "&deg;C <img src='http://openweathermap.org/img/w/" + currentIconCode + ".png'></span></p>\n" + "<p>Humidity:<span id=\"hu\">" + currentHumidity + "%</span></p>\n" + "<p>Condition: <span id=\"con\">" + currentDescription + "</span></p>");
            out.println("</div>");
            out.println("<br><br><br><br>");
            out.println("<div class='alldays'>");
                for (int i = 1; i < 17; i++) {
                    JSONObject dayData = weatherList.getJSONObject(i);
                    String date = dayData.getString("dt_txt");
                    String iconCode = dayData.getJSONArray("weather").getJSONObject(0).getString("icon");
                    String description = dayData.getJSONArray("weather").getJSONObject(0).getString("description");
                    double temperature = dayData.getJSONObject("main").getDouble("temp");
                    double humidity = dayData.getJSONObject("main").getDouble("humidity");
                    double windSpeed = dayData.getJSONObject("wind").getDouble("speed");

                    out.println("<div id='day" + (i + 1) + "' class='days'>");
                    if (i == 0) {
                        out.println("<h2>Tomorrow</h2>");
                    } else {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date dt = format.parse(date);
                        String formattedDate = new SimpleDateFormat("HH:mm - MMM dd, yyyy").format(dt);
                        out.println("<h2>" + formattedDate + "</h2>");
                    }
                    out.println("<p>Temp: <span id='temp" + (i + 1) + "'>" + temperature + " &deg;C <img src='http://openweathermap.org/img/w/" + iconCode + ".png'></span></p>");
                    out.println("<p>Wind: <span id='win" + (i + 1) + "'>" + windSpeed + " mph</span></p>");
                    out.println("<p>Humidity: <span id='hum" + (i + 1) + "'>" + humidity + "%</span></p>");
                    out.println("<p>Description: <span id='des" + (i + 1) + "'>" + description + "</span></p>");
                    out.println("</div>");
                    if(i%4==0){out.println("</div><div class='alldays'>");}
                }

            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
