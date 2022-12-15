package com.example.hstalk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    private EditText edtemail,edtpassword;
    private Button btnlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtemail=findViewById(R.id.edtemail);
        edtpassword=findViewById(R.id.edtpasword);
        btnlogin=findViewById(R.id.btnlogin);
    }
}