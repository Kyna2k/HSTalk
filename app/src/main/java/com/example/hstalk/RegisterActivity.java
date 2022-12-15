package com.example.hstalk;

import static com.example.hstalk.API.BASE_Service;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtname,edtemailsignup,edtpasswordsingup;
    private Button btnsignup;
    private TextView txtsignin;
    private API requestInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(API.class);
        edtname=findViewById(R.id.edtname);
        edtemailsignup=findViewById(R.id.edtemailsignup);
        edtpasswordsingup=findViewById(R.id.edtpaswordsignup);
        btnsignup=findViewById(R.id.btndangky);
        txtsignin=findViewById(R.id.txtsignin);
        txtsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangky(edtemailsignup.getText().toString(),edtpasswordsingup.getText().toString(),"");
            }
        });
    }
    public void dangky(String username, String password, String name)
    {
        new CompositeDisposable().add(requestInterface.dangky(username,username,password,"0909090909","0909090909")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        );
    }

    private void handleResponse(Integer integer) {
        if(integer > 0)
        {
            Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            finish();
        }
    }

    private void handleError(Throwable throwable) {
    }
}