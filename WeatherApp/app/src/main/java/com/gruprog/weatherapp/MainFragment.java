package com.gruprog.weatherapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainFragment extends Fragment {
    private static final String TAG = MainFragment.class.getName();

    ArrayAdapter<String> weatherDataAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String[] weatherData = {
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thursday",
                "Friday",
                "Saturday",
                "Sunday"
        };

        List<String> weatherDataList = new ArrayList<>(Arrays.asList(weatherData));

        weatherDataAdapter = new ArrayAdapter<>(
                this.getActivity(),
                R.layout.day_weather_forecast_list_item_layout, /* Point to the resource ID of the layout that contains the view for each item in the list */
                R.id.day_weather_forecast_list_item_text_view, /* Point to the resource ID of the View in the above specified layout, that the adapter
                 instantiates per (and initializes with each) data element to display a list of those views/data items in the ListView. */
                weatherDataList);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.weather_forecast_list_view);
        listView.setAdapter(weatherDataAdapter);

        String weatherDataFromOpenWeatherApi = getWeatherData(getURL("M6R2H6"));

        return rootView;
    }

    private URL getURL(String cityCode) {
        /*
            URL format is

            http://api.openweathermap.org/data/2.5/forecast/daily?q=<postal code>&mode=<response format>&units=<temp unit>&cnt=<num of day>&APPID=<insert your app key>

            http://api.openweathermap.org/data/2.5/forecast/daily?q=M6R2H6&mode=json&units=metric&cnt=7&APPID=<api key>
         */

        final String baseUrl = BuildConfig.OPEN_WEATHER_MAP_URL;
        final String apiKey = "&APPID=" + BuildConfig.OPEN_WEATHER_MAP_API_KEY;
        final String cityPathParameterKey = "q";
        final String cityPathParameterValue = cityCode;

        final String responseFormatPathParameterKey = "mode";
        final String responseFormatPathParameterValue = "json";

        final String temperatureUnitsPathParameterKey = "units";
        final String temperatureUnitsPathParameterValue = "metric";

        final String numberOfDaysPathParameterKey = "cnt";
        final int numberOfDaysPathParameterValue = 7;

        Uri uri = Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter(cityPathParameterKey, cityPathParameterValue)
                .appendQueryParameter(responseFormatPathParameterKey, responseFormatPathParameterValue)
                .appendQueryParameter(temperatureUnitsPathParameterKey, temperatureUnitsPathParameterValue)
                .appendQueryParameter(numberOfDaysPathParameterKey, Integer.toString(numberOfDaysPathParameterValue))
                .build();

        URL url = null;

        try {
            //Construct the url to access openweathermap api
            url = new URL(uri.toString());
        } catch(MalformedURLException e) {
            Log.e(TAG, "Error occurred", e);
            System.exit(0);
        }

        return url;
    }

    private String getWeatherData(URL url) {
        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;

        StringBuffer responseBuffer = new StringBuffer();

        try {
            //Make a request by connecting to the openweathermap api
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Read the response
            InputStream inputStream = urlConnection.getInputStream();

            if(inputStream == null) {
                return null;
            }

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                /*
                    adding a new line clearly formats json string, which is helpful when printed to the console
                 */
                responseBuffer.append(line + "\n");
            }

            if(responseBuffer != null) {
                Log.d(TAG, responseBuffer.toString());
            }
        }catch (IOException e) {
            Log.e(TAG, "Error occurred", e);
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }

            if(bufferedReader != null) {
                try {
                    bufferedReader.close();
                }catch(IOException e) {
                    Log.e(TAG, "Error occurred while closing stream", e);
                }
            }
        }

        return (responseBuffer.length() == 0 ? null : responseBuffer.toString());
    }
}
