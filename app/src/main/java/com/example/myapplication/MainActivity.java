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

public class MainActivity extends AppCompatActivity{

    private  TextView responseTextView;
    private final String[] finalResponse = new String[1];
    private final int LOADER_ID=25;
    private RequestQueue reqQueue;
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

    public void goToTheSite(View view){
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
}
