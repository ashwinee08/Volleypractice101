package com.example.myapplication;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements Callback<JSONObject>{

    private  TextView responseTextView;
    private final String[] finalResponse = new String[1];
    private final int LOADER_ID=25;
    private RequestQueue reqQueue;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        responseTextView = findViewById(R.id.response_text_view);
        //Important code to scroll:
        responseTextView.setMovementMethod(new ScrollingMovementMethod());

    }


    @Override
    protected void onStart() {
        super.onStart();
        reqQueue= Volley.newRequestQueue(MainActivity.this);
    }

    public void goToTheSiteUsingVolley(View view){
        Bundle bundle=new Bundle();
        LinearLayout parentLinearLayout = (LinearLayout) view.getParent();
        switch(parentLinearLayout.getId()){
            case R.id.randomSite:
                bundle.putString("site","http://services.groupkt.com/country/get/all");
                break;
            case R.id.facebook:
                bundle.putString("site","https://www.facebook.com");
                break;
        }

        Log.d("site is : ",bundle.getString("site"));
        responseTextView.setText(getString(R.string.loading));
        StringRequest request= new StringRequest(Request.Method.GET,
                bundle.getString("site"),
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        responseTextView.setText(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        finalResponse[0] = "Error! Please try again.";
                        responseTextView.setText(finalResponse[0]);
//                        Log.e("Error is: ",error.getMessage());
                    }
                });
        reqQueue.add(request);
    }


    public void goToTheSiteUsingRetrofit(View view){
        this.startNetworkCallUsingRetrofit();
    }


    void startNetworkCallUsingRetrofit(){

        gson=new GsonBuilder().setLenient().create();

        responseTextView.setText(getString(R.string.loading));
        Retrofit retrofitObject=new Retrofit.Builder()
                .baseUrl("http://services.groupkt.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RetrofitInterface retrofitInterface=retrofitObject.create(RetrofitInterface.class);
        Call<JSONObject> call= retrofitInterface.getResponse();
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<JSONObject> call, retrofit2.Response<JSONObject> response) {
        responseTextView.setText(getString(R.string.loading));
        if(response.isSuccessful()){
            finalResponse[0]="";
            Log.d("RESPONSE BODY : ", response.body().toString());
            JSONObject jsonObject=response.body();
            try {
                JSONArray resultArrray=jsonObject.getJSONArray("result");
                JSONObject meatObject;String temp;
                for(int i=0;i<resultArrray.length();i++){
                    temp="";
                    meatObject=resultArrray.getJSONObject(i);
                    temp+=meatObject.getString("name")+"\n";
                    temp+=meatObject.getString("alpha2_code")+"\n";
                    temp+=meatObject.getString("alpha3_code")+"\n\n";
                    finalResponse[0] +=temp;
                }
            } catch (JSONException e) {
                Log.e("Error parsing JSON : ",e.toString());
                e.printStackTrace();
            }
        }else{
            finalResponse[0] = "Error! Please try again.";
        }
        responseTextView.setText(finalResponse[0]);
    }

    @Override
    public void onFailure(Call<JSONObject> call, Throwable t) {
        finalResponse[0] = "Error! Please try again.";
        responseTextView.setText(finalResponse[0]);
    }
}
