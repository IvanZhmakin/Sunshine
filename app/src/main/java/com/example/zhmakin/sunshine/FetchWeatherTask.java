package com.example.zhmakin.sunshine;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Zhmakin on 23.02.2015.
 */
public class FetchWeatherTask extends AsyncTask<Object, Void, String[]>
{
    ListAdapter _adapter;

    public FetchWeatherTask(ListAdapter adapter) {
        _adapter = adapter;
    }
    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p/>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected String[] doInBackground(Object... params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;

        try
        {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are available at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                forecastJsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null)
            {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0)
            {
                // Stream was empty.  No point in parsing.
                return null;
            }
            forecastJsonStr = buffer.toString();

            return ParseJsonData(forecastJsonStr);

        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
        return null;
    }
    protected String[] ParseJsonData(String jsonString)
    {
        String[] result=null;
        try {
            JSONObject root = new JSONObject(jsonString);
            JSONObject city = root.getJSONObject("city");

            Log.e("ParseJsonData", "city: "+city.getString("name"));

            JSONArray forecasts = root.getJSONArray("list");
            result = new String[forecasts.length()];
            for (int i = 0; i < forecasts.length(); i++)
            {
                JSONObject forecast = forecasts.getJSONObject(i);

                String dtStr = forecast.getString("dt");
                //Date dt = new Date(dtStr);
                // Because the API returns a unix timestamp (measured in seconds),
                // it must be converted to milliseconds in order to be converted to valid date.
                SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
                String dtStr2 =  shortenedDateFormat.format(forecast.getLong("dt"));

                String minTemp = forecast.getJSONObject("temp").getString("min");
                String maxTemp = forecast.getJSONObject("temp").getString("max");

                String weatherDesc = forecast.getJSONArray("weather").getJSONObject(0).getString("main");
                String dayWeather = "day: "+dtStr2+" Temp: "+minTemp+"/"+maxTemp+" desc: "+weatherDesc;

                result[i]=dayWeather;
                //Log.v("ParseJsonData", dayWeather);
            }


        } catch (JSONException e) {
            Log.e("ParseJsonData", "Error", e);
        }

        return result;

    }
    protected void onPostExecute(String[] results)
    {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>)_adapter;
        adapter.clear();
        for (String s: results)
        {
            Log.i("result", s);
            adapter.add(s);
        }
    }
}
