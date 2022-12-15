package com.example.hstalk;

import com.example.hstalk.model.Account;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface API {
    public static final String BASE_Service = "https://circle.kynalab.com/api/";

    @GET("hstalk/all_Account")
    Observable<ArrayList<Account>> getALL();

    @POST("hstalk/add_Account")
    Observable<Integer> dangky(@Query("username") String username,@Query("email") String email, @Query("password") String password, @Query("phoneNumber") String phoneNumber, @Query("address") String address);
}
