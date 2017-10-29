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

    static JsonObjectRequest jsonObjectRequest;
    TextView tempText;
    EditText editText;
    String str;
    TextView descText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tempText = findViewById(R.id.textView);
        editText = findViewById(R.id.editText);
        descText = findViewById(R.id.description);


        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = buildURL(editText.getText().toString());
                getTemp(str);
            }
        });

    }

    void getTemp(String url) {
        jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("RESPONSE", response.toString());
                        try {
                            JSONArray listJSON = response.getJSONArray("list");
                            JSONObject listJSONJSONObject = listJSON.getJSONObject(0);
                            JSONObject main = listJSONJSONObject.getJSONObject("main");
                            JSONArray weatherArray = listJSONJSONObject.getJSONArray("weather");
                            JSONObject weather = weatherArray.getJSONObject(0);
                            String temp = String.valueOf(Math.round(main.getDouble("temp")));
                            String desc = weather.getString("description");
                            descText.setText(desc);
                            tempText.setText(temp + "°");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }


                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
    static String buildURL(String str){
        return "http://api.openweathermap.org/data/2.5/forecast?q=" + str + "&APPID=6bfdce5b63454f2e9c82ae90cec7882f&units=metric";
    }
}
