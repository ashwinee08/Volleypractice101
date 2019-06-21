package com.example.myapplication;



import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitInterface {

    @GET("country/get/all")
    Call<JSONObject> getResponse();
}
