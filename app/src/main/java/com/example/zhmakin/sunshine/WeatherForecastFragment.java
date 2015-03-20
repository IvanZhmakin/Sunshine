package com.example.zhmakin.sunshine;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class WeatherForecastFragment extends Fragment {

    ArrayAdapter<String> adapter;
    Context context;

    public WeatherForecastFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        setHasOptionsMenu(true);
        // GetWeatherForecastInfo();
        context = container.getContext();

        ArrayList<String> list = new ArrayList<String>();
        list.add("Today - Sunny - 88/63");
        list.add("Tomorrow - Foggy - 81/60");
        list.add("Day3 - Sunny - 88/63");
        list.add("Day4 - Sunny - 88/63");
        list.add("Day5 - Sunny - 88/63");
        list.add("Day6 - Sunny - 88/63");
        list.add("Day7 - Sunny - 88/63");
        list.add("Day8 - Sunny - 88/63");
        list.add("Day9 - Sunny - 88/63");
        list.add("Day10 - Sunny - 88/63");
        list.add("Day11 - Sunny - 88/63");
        list.add("Day12 - Sunny - 88/63");
        list.add("Day13 - Sunny - 88/63");
        list.add("Day14 - Sunny - 88/63");

        adapter = new ArrayAdapter<String>(
                getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textView, list);

        ListView listView = (ListView)rootView.findViewById(R.id.listView_forecast);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = container.getContext();
                String text = adapter.getItem(position);

               // Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
                //toast.show();

                Intent detailIntent = new Intent(context, Detail.class);
                detailIntent.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(detailIntent);
            }
        });

        return rootView;

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent detailIntent = new Intent(context, SettingsActivity.class);
            startActivity(detailIntent);
        }
        else if (id == R.id.action_refresh) {
            new FetchWeatherTask(adapter).execute("94043");//"SaintPeterburg");
        }
        else if (id == R.id.action_show_map) {
            Uri uri = Uri.parse("geo:0,0?q=Kursk");
            showMap(uri);//"SaintPeterburg"); geo:0,0?q=Kursk
        }
        return super.onOptionsItemSelected(item);
    }

    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}
