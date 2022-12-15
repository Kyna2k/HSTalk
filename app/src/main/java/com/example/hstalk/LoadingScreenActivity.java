package com.example.hstalk;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoadingScreenActivity extends AppCompatActivity {
    private Button btnsignup;
    private TextView txtskip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        btnsignup=findViewById(R.id.btnsignup);
        txtskip=findViewById(R.id.txtskip);


        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoadingScreenActivity.this,RegisterActivity.class));
            }
        });
        txtskip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoadingScreenActivity.this,LoginActivity.class));
            }
        });
    }
}