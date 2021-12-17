package com.example.gladysweatherex;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
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

public class Results extends AppCompatActivity {
    private String city;
    private int idtem;
    private String country;
    private double lon;
    private double lat;
    private double temp;
    String tempUrl = "";
    private final String url = "https://api.openweathermap.org/data/2.5/weather?q=";
    private final String appid = "65d00499677e59496ca2f318eb68c049";
    DecimalFormat df = new DecimalFormat("#.##");
    private double divide = 1;
    private double sum = 0;
    private String clasif = " °C";

    private RadioButton rCel;
    private RadioButton rFa;
    private TextView txtCity;
    private TextView txtday;
    private TextView txtTemp;
    private TextView txtFL;
    private TextView txtHum;
    private TextView txtDesc;
    private TextView txtWind;
    private TextView txtClouds;
    private TextView txtPressure;
    private Button change;
    private Button changeday;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Bundle extras = getIntent().getExtras();

        city = extras.getString("city");
        idtem = extras.getInt("iddate");
        country = extras.getString("country");
        lon = extras.getDouble("lon");
        lat = extras.getDouble("lat");
        temp = extras.getDouble("temp");

        rCel = findViewById(R.id.rCel);
        rFa = findViewById(R.id.rFa);
        txtCity = findViewById(R.id.txtCity);
        txtday = findViewById(R.id.txtday);
        txtTemp = findViewById(R.id.txtTemp);
        txtFL = findViewById(R.id.txtFL);
        txtHum = findViewById(R.id.txtHum);
        txtDesc = findViewById(R.id.txtDesc);
        txtWind = findViewById(R.id.txtWind);
        txtClouds = findViewById(R.id.txtClouds);
        txtPressure = findViewById(R.id.txtPressure);
        change = findViewById(R.id.change);
        changeday = findViewById(R.id.changeday);

        txtCity.setText(city + " (" + country + ")");

        if(idtem == 0)
            txtTemp.setText(df.format(temp* divide + sum) + clasif);

        else
            txtTemp.setText("Real time temperature not available");

        getWeatherDetails(lat, lon);
    }

    public void getWeatherDetails(double lat , double lon){
        String tempUrl = "https://api.openweathermap.org/data/2.5/onecall?lat=" + lat + "&lon=" + lon + "&exclude=current,minutely,hourly,alerts&appid=" +appid;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("daily");
                        JSONObject jsonArrayDaily = jsonArray.getJSONObject(idtem);

                            int dt = jsonArrayDaily.getInt("dt");
                            Date date = new java.util.Date(dt * 1000L);// the format of your date
                            SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE, MMMM d, yyyy");// give a timezone reference for formatting (see comment at the bottom)
                            sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-4"));
                            txtday.setText(sdf.format(date));

                            double pressure = jsonArrayDaily.getDouble("pressure");
                            txtPressure.setText("Pressure: " + df.format(pressure) + " hPa");

                            int humidity = jsonArrayDaily.getInt("humidity");
                            txtHum.setText("Humidity: " + humidity + "%");

                            double wind = jsonArrayDaily.getDouble("wind_speed");
                            txtWind.setText("Wind Speed: " + df.format(wind) + "m/s");

                            int clouds = jsonArrayDaily.getInt("clouds");
                            txtClouds.setText("Cloudiness: " + clouds + "%");

                            JSONObject jsonObjectDaily = jsonArrayDaily.getJSONObject("temp");
                                double morn = (jsonObjectDaily.getDouble("morn") - 273.15) * divide + sum;
                                double day = (jsonObjectDaily.getDouble("day") - 273.15) * divide + sum;
                                double eve = (jsonObjectDaily.getDouble("eve") - 273.15) * divide + sum;
                                double night = (jsonObjectDaily.getDouble("night") - 273.15) * divide + sum;

                                double min = (jsonObjectDaily.getDouble("min") - 273.15) * divide + sum;
                                double max = (jsonObjectDaily.getDouble("max") - 273.15) * divide + sum;

                                txtFL.setText("Feels Like: \n"
                                    +"Morning: "+df.format(morn) + clasif +"\n"
                                    +"Day: "+df.format(day) + clasif +"\n"
                                    +"Evening: "+df.format(eve) + clasif +"\n"
                                    +"Night: "+df.format(night) + clasif +"\n"
                                    +"Min: "+df.format(min) + clasif +"\n"
                                    +"Max: "+df.format(max) + clasif);

                                    txtDesc.setText("Description: " + jsonResponse.getJSONArray("daily").getJSONObject(idtem).getJSONArray("weather").getJSONObject(0).getString("description"));




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

    public void onClick(View view) { //button radio select
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.rCel:
                if (checked) {
                    divide = 1;
                    sum = 0;
                    clasif = " °C";
                    txtTemp.setText(df.format(temp * divide + sum) + clasif);
                    getWeatherDetails(lat,lon);
                }
                break;
            case R.id.rFa:
                if (checked) {
                    divide = 1.8;
                    sum = 32;
                    clasif = " °F";
                    txtTemp.setText(df.format(temp * divide + sum) + clasif);
                    getWeatherDetails(lat,lon);
                }
                break;
        }

    }

    public void changeCity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void changeDay(View view) {
        Intent intent = new Intent(this, List.class);
        intent.putExtra("name", city);
        startActivity(intent);
    }
}