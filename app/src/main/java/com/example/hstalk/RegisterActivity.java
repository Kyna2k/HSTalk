package com.example.hstalk;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtname,edtemailsignup,edtpasswordsingup;
    private Button btnsignup;
    private TextView txtsignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtname=findViewById(R.id.edtname);
        edtemailsignup=findViewById(R.id.edtemailsignup);
        edtpasswordsingup=findViewById(R.id.edtpaswordsignup);
        btnsignup=findViewById(R.id.btndangky);
        txtsignin=findViewById(R.id.txtsignin);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        txtsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
    }
}