package com.example.gladysweatherex;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
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
import java.text.SimpleDateFormat;
import java.util.Date;

public class List extends AppCompatActivity {
    TextView temp0;
    TextView temp1;
    TextView temp2;
    TextView temp3;
    TextView temp4;
    TextView temp5;
    TextView temp6;
    TextView temp7;

    private String city;
    String tempUrl = "";
    private final String url = "https://api.openweathermap.org/data/2.5/weather?q=";
    private final String appid = "65d00499677e59496ca2f318eb68c049";
    DecimalFormat df = new DecimalFormat("#.##");
    private double lat;
    private double lon;
    private String country;
    private double temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        temp0 = findViewById(R.id.temp0);
        temp1 = findViewById(R.id.temp1);
        temp2 = findViewById(R.id.temp2);
        temp3 = findViewById(R.id.temp3);
        temp4 = findViewById(R.id.temp4);
        temp5 = findViewById(R.id.temp5);
        temp6 = findViewById(R.id.temp6);
        temp7 = findViewById(R.id.temp7);

        Bundle extras = getIntent().getExtras();
        city = extras.getString("name");
        getWeatherDetails(); //json object for set Text
    }

    public void getWeatherDetails() {
        tempUrl = url + city + "&appid=" + appid;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                    city = jsonResponse.getString("name");
                    country = jsonObjectSys.getString("country");

                    /*JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    String description = jsonObjectWeather.getString("description");
                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    double temp = jsonObjectMain.getDouble("temp") - 273.15;
                    double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                    float pressure = jsonObjectMain.getInt("pressure");
                    int humidity = jsonObjectMain.getInt("humidity");
                    JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                    String wind = jsonObjectWind.getString("speed");
                    JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                    String clouds = jsonObjectClouds.getString("all");

                    */
                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    temp = jsonObjectMain.getDouble("temp") - 273.15;
                    JSONObject jsonObjectCoord = jsonResponse.getJSONObject("coord");
                    lat = jsonObjectCoord.getDouble("lat");
                    lon = jsonObjectCoord.getDouble("lon") ;
                    extra(lat, lon);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }}, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void extra(double lat , double lon){
        String tempUrl = "https://api.openweathermap.org/data/2.5/onecall?lat=" + lat + "&lon=" + lon + "&exclude=current,minutely,hourly,alerts&appid=" +appid;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String output = "";
                try {
                    for(int i = 0; i<8; i++) {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("daily");
                        JSONObject jsonArrayDaily = jsonArray.getJSONObject(i);
                        JSONObject jsonObjectDaily = jsonArrayDaily.getJSONObject("temp");
                        int dt = jsonArrayDaily.getInt("dt");
                        double temp = jsonObjectDaily.getDouble("day") - 273.15;

                        Date date = new java.util.Date(dt * 1000L);// the format of your date
                        SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE, MMMM d, yyyy");// give a timezone reference for formatting (see comment at the bottom)
                        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-4"));
                        String formattedDate = sdf.format(date);


                        output = formattedDate + "\n Temperature: " + df.format(temp) + " °C";

                        switch(i){
                            case 0:
                                temp0.setText(output);
                                break;
                            case 1:
                                temp1.setText(output);
                                break;
                            case 2:
                                temp2.setText(output);
                                break;
                            case 3:
                                temp3.setText(output);
                                break;
                            case 4:
                                temp4.setText(output);
                                break;
                            case 5:
                                temp5.setText(output);
                                break;
                            case 6:
                                temp6.setText(output);
                                break;
                            case 7:
                                temp7.setText(output);
                                break;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void Day(View view) {
        Intent intent = new Intent(this, Results.class);
        switch(view.getId()){
            case R.id.temp0:
                intent.putExtra("iddate", 0);
                break;
            case R.id.temp1:
                intent.putExtra("iddate", 1);
                break;
            case R.id.temp2:
                intent.putExtra("iddate", 2);
                break;
            case R.id.temp3:
                intent.putExtra("iddate", 3);
                break;
            case R.id.temp4:
                intent.putExtra("iddate", 4);
                break;
            case R.id.temp5:
                intent.putExtra("iddate", 5);
                break;
            case R.id.temp6:
                intent.putExtra("iddate", 6);
                break;
            case R.id.temp7:
                intent.putExtra("iddate", 7);
                break;
        }

        intent.putExtra("city", city);
        intent.putExtra("country", country);
        intent.putExtra("lon", lon);
        intent.putExtra("lat", lat);
        intent.putExtra("temp", temp);
        startActivity(intent);
    }

    public void changeCity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}