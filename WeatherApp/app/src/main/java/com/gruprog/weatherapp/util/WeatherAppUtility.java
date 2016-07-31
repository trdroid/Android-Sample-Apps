package com.gruprog.weatherapp.util;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class WeatherAppUtility {
    private static final String TAG = WeatherAppUtility.class.getName();

    public static String[] getWeatherForecastDataFromJson(String weatherDataFromOpenWeatherApi, int numberOfDays)
            throws JSONException {
        final String list = "list";
        final String weather = "weather";
        final String temperature = "temp";
        final String maximumTemperature = "max";
        final String minimumTemperature = "min";
        final String weatherQuickSummary = "main";

        JSONObject weatherDataJson = new JSONObject(weatherDataFromOpenWeatherApi);
        JSONArray weatherDataArray = weatherDataJson.getJSONArray(list);

        /*
            The OpenWeatherMap API sends the weather data from current day to the next requested number of days in order

            The forecast data returned for a city is based the city's local time.
         */

        String[] weatherForecastArray = new String[numberOfDays];

        GregorianCalendar gregorianCalendar = new GregorianCalendar();

        long currentTime = System.currentTimeMillis();

        Log.d(TAG, gregorianCalendar.toString());
        Log.d(TAG, gregorianCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US));

        for(int index = 0; index < weatherDataArray.length(); index++) {
            JSONObject dayWeatherForecast = weatherDataArray.getJSONObject(index);
            JSONObject weatherObject = dayWeatherForecast.getJSONArray(weather).getJSONObject(0);
            String desc = weatherObject.getString(weatherQuickSummary);
            JSONObject temperatureObject = dayWeatherForecast.getJSONObject(temperature);

            String dayOfWeek = gregorianCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US);

            double high = temperatureObject.getDouble(maximumTemperature);
            double low = temperatureObject.getDouble(minimumTemperature);

            weatherForecastArray[index] = dayOfWeek + ":" + desc + " High:" + high + " Low:" + low;
            Log.d(TAG, desc);

            currentTime = currentTime + 1000 * 60 * 60 * 24;
            gregorianCalendar.setTimeInMillis(currentTime);
        }

        return weatherForecastArray;
    }
}