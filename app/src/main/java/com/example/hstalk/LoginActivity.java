package com.example.hstalk;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    private EditText edtemail,edtpassword;
    private Button btnlogin;
    private TextView txtsignup;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtemail=findViewById(R.id.edtemail);
        edtpassword=findViewById(R.id.edtpasword);
        btnlogin=findViewById(R.id.btnlogin);
        txtsignup=findViewById(R.id.txtsignup);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        txtsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }
}