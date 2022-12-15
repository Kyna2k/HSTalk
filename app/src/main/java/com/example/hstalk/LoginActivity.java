package com.example.hstalk;

import static com.example.hstalk.API.BASE_Service;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hstalk.model.Account;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private EditText edtemail,edtpassword;
    private Button btnlogin;
    private TextView txtsignup;
    private API requestInterface;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(API.class);
        edtemail=findViewById(R.id.edtemail);
        edtpassword=findViewById(R.id.edtpasword);
        btnlogin=findViewById(R.id.btnlogin);
        txtsignup=findViewById(R.id.txtsignup);


        txtsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangnhap();
            }
        });
    }
    public void dangnhap(){
        new CompositeDisposable().add(requestInterface.getALL()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        );
    }

    private void handleError(Throwable throwable) {
    }

    private void handleResponse(ArrayList<Account> accounts) {
        if(accounts.size()>0)
        {
            for(Account ac : accounts)
            {
                if(ac.getEmail().equals(edtemail.getText().toString()) && ac.getPassword().equals(edtpassword.getText().toString()))
                {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }
        }
    }
}