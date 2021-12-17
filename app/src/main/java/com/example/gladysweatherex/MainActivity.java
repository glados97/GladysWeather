package com.example.gladysweatherex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private EditText city;
    private Button search;

    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city = findViewById(R.id.city);
        search = findViewById(R.id.search);

    }

    public void callResults(View v){
        Intent intent = new Intent(this, List.class);
        name=city.getText().toString();
        intent.putExtra("name", name);
        startActivity(intent);
    }
}