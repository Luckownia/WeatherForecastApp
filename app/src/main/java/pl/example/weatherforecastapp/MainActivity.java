package pl.example.weatherforecastapp;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.view.inputmethod.InputMethodManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {
    EditText textInputLayout;
    TextView textView,longTermWeather,weatherInfoCity,weatherInfoTemp, weatherInfoDescr, futureTempOne, futureTempTwo, futureTempThree, futureTempFour,
    futureHourOne, futureHourTwo, futureHourThree, futureHourFour;
    ImageView futureImgOne, futureImgTwo, futureImgThree, futureImgFour;
    ConstraintLayout weatherInfo;
    Button button_location;
    LocationManager locationManager;
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
        weatherInfoDescr = findViewById(R.id.weatherInfoDescr);
        weatherInfo = findViewById(R.id.weatherInfo);
        longTermWeather = findViewById(R.id.longTermForecast);
        futureImgOne = findViewById(R.id.futureImgOne);
        futureImgTwo = findViewById(R.id.futureImgTwo);
        futureImgThree = findViewById(R.id.futureImgThree);
        futureImgFour = findViewById(R.id.futureImgFour);
        futureTempOne = findViewById(R.id.futureTempOne);
        futureTempTwo = findViewById(R.id.futureTempTwo);
        futureTempThree = findViewById(R.id.futureTempThree);
        futureTempFour = findViewById(R.id.futureTempFour);
        futureHourOne = findViewById(R.id.futureHourOne);
        futureHourTwo = findViewById(R.id.futureHourTwo);
        futureHourThree = findViewById(R.id.futureHourThree);
        futureHourFour = findViewById(R.id.futureHourFour);
        button_location = findViewById(R.id.currentLocation);

        //Asking for location permission
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!=
        PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }
        button_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create method
                getLocation();
            }
        });
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
                            getWeatherDetails(lat,lon);
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
    //Testing
    private void getWeatherDetails(double lat, double lon) {
        // Drugie żądanie sieciowe
        String tempUrlLoc = "https://api.openweathermap.org/data/2.5/weather?";
        tempUrlLoc += "lat=" + lat + "&lon=" + lon + "&appid=" + appid;
        System.out.println(tempUrlLoc);
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
                    String polishDescription = translateWeatherDescription(description);
                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    double temp = jsonObjectMain.getDouble("temp") - 273.15;
                    double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                    float pressure = jsonObjectMain.getInt("pressure");
                    int humidity = jsonObjectMain.getInt("humidity");
                    JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                    float speedWind = jsonObjectWind.getInt("speed");
                    JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                    int cloudy = jsonObjectClouds.getInt("all");
                    String city = jsonResponse.getString("name");
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
                    weatherInfoDescr.setText(polishDescription);
                    hideKeyboard();

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

        //LONG-TERM-WEATHER-FORECAST
        //trzecie żądanie sieciowe long term weather forecast 5day/3hour
        //Testowo wypluwana jest pierwsza prognoza za 3h ((5*24))/3= 40 obiektów)
        String longTermUrlLoc = "https://api.openweathermap.org/data/2.5/forecast?";
        longTermUrlLoc += "lat=" + lat + "&lon=" + lon + "&appid=" + appid;
        System.out.println(longTermUrlLoc);
        StringRequest stringRequest3 = new StringRequest(Request.Method.GET, longTermUrlLoc, new Response.Listener<String>() {
            @Override
            public void onResponse(String response3) {
                Log.d("response", response3);
                String output = "";
                try {
                    JSONObject jsonResponse = new JSONObject(response3);
                    JSONArray jsonArray = jsonResponse.getJSONArray("list");
                    for(int i=1;i<=4;i++) {
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(i);
                        JSONArray jsonArray2 = jsonObjectWeather.getJSONArray("weather");
                        JSONObject jsonObjectWeather2 = jsonArray2.getJSONObject(0);
                        String description = jsonObjectWeather2.getString("description");
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
                        String hour = dataTime.substring(dataTime.indexOf(' ') + 1, dataTime.lastIndexOf(':'));
                        int imageResource;
                        switch (description) {
                            case "overcast clouds":
                                imageResource = R.drawable.clouds;
                                break;
                            case "light rain":
                                imageResource = R.drawable.rain;
                                break;
                            case "rain":
                                imageResource = R.drawable.rain;
                                break;
                            case "scattered clouds":
                                imageResource = R.drawable.clouds;
                                break;
                            case "few clouds":
                                imageResource = R.drawable.clouds;
                                break;
                            case "broken clouds":
                                imageResource = R.drawable.clouds;
                                break;
                            case "light snow":
                                imageResource = R.drawable.snow;
                                break;
                            case "snow":
                                imageResource = R.drawable.snow;
                                break;
                            case "clear sky":
                                imageResource = R.drawable.sun;
                                break;
                            case "sunrise":
                                imageResource = R.drawable.sun;
                                break;
                            default:
                                imageResource = R.drawable.unknown_weather; // Ustawienie domyślnego obrazka w przypadku braku dopasowania
                                break;
                        }
                        switch (i) {
                            case 1:
                                futureTempOne.setText(String.format("%.2f", temp) + "\u00B0");
                                futureHourOne.setText(hour);
                                futureImgOne.setImageResource(imageResource);
                                break;
                            case 2:
                                futureTempTwo.setText(String.format("%.2f", temp) + "\u00B0");
                                futureHourTwo.setText(hour);
                                futureImgTwo.setImageResource(imageResource);
                                break;
                            case 3:
                                futureTempThree.setText(String.format("%.2f", temp) + "\u00B0");
                                futureHourThree.setText(hour);
                                futureImgThree.setImageResource(imageResource);
                                break;
                            case 4:
                                futureTempFour.setText(String.format("%.2f", temp) + "\u00B0");
                                futureHourFour.setText(hour);
                                futureImgFour.setImageResource(imageResource);
                                break;
                            default:
                                // Obsługa, gdy przekraczasz maksymalną liczbę przyszłych temperatur
                                break;
                        }
                        output += "DataTime= " + dataTime  //testowanie czy wyswietla mozna zakomentowac
                                //+ "description=" + description + "\n"
                                + " temperature=" + temp + "\n ";
                            /*+ "feels like=" + feelsLike + "\n "
                            + "Pressure=" + pressure + "\n"
                            + "Humidity=" + humidity + "\n"
                            + "Speed wind=" + speedWind + "\n"
                            + "Cloudy=" + cloudy + "\n";*/
                        //longTermWeather.setText(output);
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

        RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
        requestQueue2.add(stringRequest2);
        RequestQueue requestQueue3 = Volley.newRequestQueue(getApplicationContext());
        requestQueue3.add(stringRequest3);
    }
    //ToDo Dodanie description haze (mgła)
    private String translateWeatherDescription(String description) {
        String polishDescription;
        switch (description) {
            case "overcast clouds":
                polishDescription = "Zachmurzenie całkowite";
                break;
            case "light rain":
                polishDescription = "Lekki deszcz";
                break;
            case "scattered clouds":
                polishDescription = "Rozproszone chmury";
                break;
            case "light snow":
                polishDescription = "Lekki śnieg";
                break;
            case "snow":
                polishDescription = "Śnieg";
                break;
            case "sunrise":
                polishDescription = "Wschód słońca";
                break;
            case "few clouds":
                polishDescription = "Pochmurno";
                break;
            case "broken clouds":
                polishDescription = "Rozbite chmury";
                break;
            case "rain":
                polishDescription = "Deszcz";
                break;
            case "clear sky":
                polishDescription = "Czyste Niebo";
                break;
            default:
                polishDescription = "brak tłumaczenia";
        }
        return polishDescription;
    }
    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        try {
            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String address = addresses.get(0).getAddressLine(0); //cały adress current location miejscowsci gdzies USA
            lat = addresses.get(0).getLatitude();
            lon = addresses.get(0).getLongitude();
            //textView.setText(address);
            getWeatherDetails(lat,lon);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @SuppressLint("MissingPermission")
    private void getLocation() {

        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,MainActivity.this);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}