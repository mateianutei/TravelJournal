package com.example.anuteistravelingjournal.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.anuteistravelingjournal.R;

import java.security.Principal;

/// the activity which handles the motion layout
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);

        //the enter app button + listener to get into the main interface
        Button start_button=findViewById(R.id.enter_app_button);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PrincipalInterfaceActivity.class);
                startActivity(i);
            }
        });
    }
}