package com.mohammadsamandari.jsondemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... cityName) {
            String myUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + cityName[0] + "&appid=740f4053ef70a0b0281155f7e2c9e8ce";
            URL url;
            HttpURLConnection connection;
            String result = "";
            try {
                url = new URL(myUrl);
                connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                Log.i("Lord-ConnectionLength", String.valueOf(connection.getContentLength()));
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (IOException e) {
                e.printStackTrace();
                return "Connection Error";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //  Creating a Json from String.
            try {
                JSONObject jsonObject = new JSONObject(s);
                //  to get a part of a json:
                String weatherInfo = jsonObject.getString("weather");
                //  we are going to taking our weather info and converting it into json array.
                JSONArray jsonArray = new JSONArray(weatherInfo);
                //  Looping through array.
                for (int i = 0; i < jsonArray.length(); i++) {
                    //  Converting each part to a json object itself.
                    JSONObject jsonPart = jsonArray.getJSONObject(i);
                    //  getting part of the new json object
                    Log.i("Lord", jsonPart.getString("main"));
                    Log.i("Lord", jsonPart.getString("description"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute("London");
    }
}
