package com.gruprog.weatherapp;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WeatherDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new WeatherDetailsFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_weather_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class WeatherDetailsFragment extends Fragment {
        public WeatherDetailsFragment() {}

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            final Intent intent = getActivity().getIntent();
            final View rootView = inflater.inflate(R.layout.fragment_weather_details, container, false);

            if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
                String weatherData = intent.getStringExtra(Intent.EXTRA_TEXT);
                final TextView weatherDetailsTextView = (TextView) rootView.findViewById(R.id.weather_details_text);
                weatherDetailsTextView.setText(weatherData);
            }

            return rootView;
        }
    }
}
