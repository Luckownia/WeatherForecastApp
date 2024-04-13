package pl.example.weatherforecastapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    EditText textInputLayout;
    TextView textView,longTermWeather,weatherInfoCity,weatherInfoTemp;
    ConstraintLayout weatherInfo;
    private final String url = "http://api.openweathermap.org/geo/1.0/direct?";
    private final String appid = "6b19c6b85668b0aa881c3d9a392fcbf8";
    double lat, lon;
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        textInputLayout = findViewById(R.id.editText1);
        textView = findViewById(R.id.textView);
        weatherInfoCity = findViewById(R.id.weatherInfoCity);
        weatherInfoTemp = findViewById(R.id.weatherInfoTemp);
        weatherInfo = findViewById(R.id.weatherInfo);
        longTermWeather = findViewById(R.id.longTermForecast);
    }

    public void getWeather(View view) {
        String tempUrl = "";
        String city = textInputLayout.getText().toString().trim();
        //Klawiatura nie ma polskich znakow, brak możliwosci ą,ę dla Api jest to obojętne
        if (city.equals("")) {
            textView.setText("City field must be filled!!!");
            weatherInfo.setVisibility(View.INVISIBLE);
        } else {
            tempUrl = url + "q=" + city + "&appid=" + appid;// http://api.openweathermap.org/geo/1.0/direct?q=Gorlice&appid=6b19c6b85668b0aa881c3d9a392fcbf8
            StringRequest stringRequest = new StringRequest(Request.Method.GET, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("response", response);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        if (jsonArray.length() > 0) {
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            lat = jsonObject.getDouble("lat");
                            lon = jsonObject.getDouble("lon");
                            System.out.println(lat);
                            System.out.println(lon);

                            // Drugie żądanie sieciowe
                            String tempUrlLoc = "https://api.openweathermap.org/data/2.5/weather?";
                            tempUrlLoc += "lat=" + lat + "&lon=" + lon + "&appid=" + appid;
                            System.out.println(tempUrlLoc);// https://api.openweathermap.org/data/2.5/weather?lat=49.6663323&lon=21.16348426717198&appid=6b19c6b85668b0aa881c3d9a392fcbf8
                            StringRequest stringRequest2 = new StringRequest(Request.Method.GET, tempUrlLoc, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response2) {
                                    Log.d("response", response2);
                                    String output = "";
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response2);
                                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                                        String description = jsonObjectWeather.getString("description");
                                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                                        double temp = jsonObjectMain.getDouble("temp") - 273.15;
                                        double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                                        float pressure = jsonObjectMain.getInt("pressure");
                                        int humidity = jsonObjectMain.getInt("humidity");
                                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                                        float speedWind = jsonObjectWind.getInt("speed");
                                        JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                                        int cloudy = jsonObjectClouds.getInt("all");
                                        output = "description=" + description + "\n"
                                                + "temperature=" + temp + "\n "
                                                + "feels like=" + feelsLike + "\n "
                                                + "Pressure=" + pressure + "\n"
                                                + "Humidity=" + humidity + "\n"
                                                + "Speed wind=" + speedWind + "\n"
                                                + "Cloudy=" + cloudy + "\n";
                                        textView.setText(output);
                                        weatherInfo.setVisibility(View.VISIBLE);
                                        weatherInfoCity.setText(city);
                                        weatherInfoTemp.setText(String.format("%.2f", temp) + "\u00B0");

                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    Toast.makeText(getApplicationContext(), volleyError.toString().trim(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
                            requestQueue2.add(stringRequest2);

                            //LONG-TERM-WEATHER-FORECAST
                            //trzecie żądanie sieciowe long term weather forecast 5day/3hour
                            //Trzeba pomyslec co zrobic/ najbardziej optymalne do przechowywanie takich danych
                            //Docelowo potrzebna bedzie petla z wypisywanem danych do jakies tablice/strukury/klasy do przechowywania danych
                            //Testowo wypluwana jest pierwsza prognoza za 3h ((5*24))/3= 40 obiektów)
                            String longTermUrlLoc = "https://api.openweathermap.org/data/2.5/forecast?";
                            longTermUrlLoc += "lat=" + lat + "&lon=" + lon + "&appid=" + appid;
                            System.out.println(longTermUrlLoc); //https://api.openweathermap.org/data/2.5/forecast?lat=49.6663323&lon=21.16348426717198&appid=6b19c6b85668b0aa881c3d9a392fcbf8
                            StringRequest stringRequest3 = new StringRequest(Request.Method.GET, longTermUrlLoc, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response3) {
                                    Log.d("response", response3);
                                    String output = "";
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response3);
                                        JSONArray jsonArray = jsonResponse.getJSONArray("list");
                                        for(int i=1;i<=39;i++) {
                                            JSONObject jsonObjectWeather = jsonArray.getJSONObject(i);
                                            JSONArray jsonArray2 = jsonObjectWeather.getJSONArray("weather");
                                            JSONObject jsonObjectWeather2 = jsonArray2.getJSONObject(0);
                                            //String description = jsonObjectWeather2.getString("description");
                                            JSONObject jsonObjectMain = jsonObjectWeather.getJSONObject("main");
                                            double temp = jsonObjectMain.getDouble("temp") - 273.15;
                                            /*double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                                            float pressure = jsonObjectMain.getInt("pressure");
                                            int humidity = jsonObjectMain.getInt("humidity");
                                            JSONObject jsonObjectWind = jsonObjectWeather.getJSONObject("wind");
                                            float speedWind = jsonObjectWind.getInt("speed");
                                            JSONObject jsonObjectClouds = jsonObjectWeather.getJSONObject("clouds");
                                            int cloudy = jsonObjectClouds.getInt("all");*/
                                            String dataTime = jsonObjectWeather.getString("dt_txt");
                                            output += "DataTime= " + dataTime  //testowanie czy wyswietla mozna zakomentowac
                                                    //+ "description=" + description + "\n"
                                                    + " temperature=" + temp + "\n ";
                                                    /*+ "feels like=" + feelsLike + "\n "
                                                    + "Pressure=" + pressure + "\n"
                                                    + "Humidity=" + humidity + "\n"
                                                    + "Speed wind=" + speedWind + "\n"
                                                    + "Cloudy=" + cloudy + "\n";*/
                                            longTermWeather.setText(output);
                                            /*weatherInfo.setVisibility(View.VISIBLE);
                                            weatherInfoCity.setText(city);
                                            weatherInfoTemp.setText(String.format("%.2f", temp) + "\u00B0");*/
                                        }

                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    Toast.makeText(getApplicationContext(), volleyError.toString().trim(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            RequestQueue requestQueue3 = Volley.newRequestQueue(getApplicationContext());
                            requestQueue3.add(stringRequest3);
                        } else{
                            textView.setText("City not found.");
                            weatherInfo.setVisibility(View.INVISIBLE);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getApplicationContext(), volleyError.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
}