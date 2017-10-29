package com.viner.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private JsonObjectRequest mJsonObjectRequest;
    private TextView mTempText;
    private EditText mEditText;
    private String mCity;
    private TextView mDescriptionTextView;
    private RequestQueue mRequestQueue;
    private final String TAG_RESPONSE = "response";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTempText = findViewById(R.id.textView);
        mEditText = findViewById(R.id.editText);
        mDescriptionTextView = findViewById(R.id.description);


        findViewById(R.id.button).setOnClickListener((v) -> {
            mCity = buildURL(mEditText.getText().toString());
            getTemp(mCity);
        });

    }

    private void getTemp(String url) {
        mJsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    Log.v(TAG_RESPONSE, response.toString());
                    try {
                        JSONArray listJSON = response.getJSONArray("list");
                        JSONObject listJSONJSONObject = listJSON.getJSONObject(0);
                        JSONObject main = listJSONJSONObject.getJSONObject("main");
                        JSONArray weatherArray = listJSONJSONObject.getJSONArray("weather");
                        JSONObject weather = weatherArray.getJSONObject(0);
                        String temp = String.valueOf(Math.round(main.getDouble("temp")));
                        String desc = weather.getString("description");
                        mDescriptionTextView.setText(desc);
                        mTempText.setText(temp + "°");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    // TODO Auto-generated method stub

                });
        mRequestQueue = Volley.newRequestQueue(this);
        mRequestQueue.add(mJsonObjectRequest);
    }

    private String buildURL(String str) {
        return "http://api.openweathermap.org/data/2.5/forecast?q=" + str + "&APPID=6bfdce5b63454f2e9c82ae90cec7882f&units=metric";
    }
}
