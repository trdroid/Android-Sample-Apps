package com.gruprog.weatherapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.gruprog.weatherapp.util.WeatherAppUtility;

import org.json.JSONException;

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
    private static final String TAG = MainFragment.class.getSimpleName();

    private ArrayAdapter<String> weatherDataAdapter;

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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String weatherData = weatherDataAdapter.getItem(position);
                final Intent intent = new Intent(getActivity(), WeatherDetailsActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, weatherData);
                startActivity(intent);
            }
        });

        Button refreshButton = (Button) rootView.findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetWeatherDataTask().execute("M6R2H6");
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

    }

    private URL getURL(String cityCode) {
        /*
            URL format is

            http://api.openweathermap.org/data/2.5/forecast/daily?q=<postal code>&mode=<response format>&units=<temp unit>&cnt=<num of day>&APPID=<insert your app key>

            http://api.openweathermap.org/data/2.5/forecast/daily?q=M6R2H6&mode=json&units=metric&cnt=7&APPID=<api key>
         */

        final String baseUrl = BuildConfig.OPEN_WEATHER_MAP_URL;

        final String cityPathParameterKey = "q";
        final String cityPathParameterValue = cityCode;

        final String responseFormatPathParameterKey = "mode";
        final String responseFormatPathParameterValue = "json";

        final String temperatureUnitsPathParameterKey = "units";
        final String temperatureUnitsPathParameterValue = "metric";

        final String numberOfDaysPathParameterKey = "cnt";
        final int numberOfDaysPathParameterValue = 7;

        final String apiPathParameterKey = "APPID";
        final String apiPathParameterValue = BuildConfig.OPEN_WEATHER_MAP_API_KEY;

        Uri uri = Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter(cityPathParameterKey, cityPathParameterValue)
                .appendQueryParameter(responseFormatPathParameterKey, responseFormatPathParameterValue)
                .appendQueryParameter(temperatureUnitsPathParameterKey, temperatureUnitsPathParameterValue)
                .appendQueryParameter(numberOfDaysPathParameterKey, Integer.toString(numberOfDaysPathParameterValue))
                .appendQueryParameter(apiPathParameterKey, apiPathParameterValue)
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

        Log.d(TAG, "Url:" + url);

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

    private class GetWeatherDataTask extends AsyncTask<String, Void, String[]> {
        private final String TAG = GetWeatherDataTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(String... params) {
            if(params.length == 0) {
                return null;
            }

            final String cityCode = params[0];

            String weatherDataFromOpenWeatherApi = getWeatherData(getURL(cityCode));
            Log.d(TAG, weatherDataFromOpenWeatherApi);

            try {
                return WeatherAppUtility.getWeatherForecastDataFromJson(weatherDataFromOpenWeatherApi, 7);
            }catch(JSONException e) {
                Log.e(TAG, "Error Occurred", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String[] weatherForecastArray) {
            if(weatherForecastArray == null) {
                return;
            }

            weatherDataAdapter.clear();

            for(String daysForcastedWeather : weatherForecastArray) {
                weatherDataAdapter.add(daysForcastedWeather);
            }
        }
    }
}