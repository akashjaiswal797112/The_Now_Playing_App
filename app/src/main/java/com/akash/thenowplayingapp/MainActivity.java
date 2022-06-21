package com.akash.thenowplayingapp;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView mTextViewResult;
    private RequestQueue mQueue;
    List<String> list = new ArrayList<>();
    boolean toggle=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewResult = findViewById(R.id.text_view_result);
        Button button_fetch = findViewById(R.id.button_fetch);
        Button button_refresh = findViewById(R.id.button_refresh);


        mQueue = Volley.newRequestQueue(this);

        //this calls the funtion which provides the data from the API
        button_fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                functionFetchData();
            }
        });

        //this calls the funtion which sorts the data as per the ratings
        button_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                functionRefresh();
            }
        });
    }


    private void functionFetchData() {
        mTextViewResult.setText("");
        toggle=true;
        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=9d0e3e33437b228d3184927838d32b9b&language=en-US&page=1";


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject movies = jsonArray.getJSONObject(i);

                                double rating = movies.getDouble("vote_average");
                                String title = movies.getString("title");
                                double popularity = movies.getDouble("popularity");
                                String overview = movies.getString("overview");
                                int order=i+1;
                                mTextViewResult.append(order+")\nTITLE : " + title   + "\n"+ "POPULARITY : "+String.valueOf(popularity) + "\n"+"OVERVIEW : " + overview + "\n" +"RATINGS : "+ String.valueOf(rating)+"\n\n");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);

    }



    private void functionRefresh() {

        if(toggle==false)
        {
            Toast.makeText(this, "No data Available for sorting", Toast.LENGTH_SHORT).show();
        }
        else if(toggle==true)
        {
            mTextViewResult.setText("");
            list.clear();
            functionSortData();
        }



    }

    private void functionSortData() {
        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=9d0e3e33437b228d3184927838d32b9b&language=en-US&page=1";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject movies = jsonArray.getJSONObject(i);

                                double rating = movies.getDouble("vote_average");
                                String title = movies.getString("title");
                                double popularity = movies.getDouble("popularity");
                                String overview = movies.getString("overview");
                                int order=i+1;
                                String concat = "TITLE : "+title    +   "\n"+ "POPULARITY : "+String.valueOf(popularity) + "\n" + "OVERVIEW: "+ overview + "\n" + "RATINGS : "+ String.valueOf(rating);

                                list.add(concat);


                            }


                            String[] arr = new String[list.size()];

                            for (int i = 0; i < list.size(); i++) {
                                arr[i] = list.get(i);
                            }


                            boolean swapped=false;

                            double ratingOfI;
                            double ratingOfIless;

                            for (int i = 0; i < arr.length-1; i++) {
                                // for each step, max item will come at the last respective index
                                for (int j = 1; j < arr.length; j++) {
                                    //swap if the current item is smaller than the previous item.

                                    //this block gets the last three characters that is rating of the current element of the array
                                    String str=arr[j];
                                    String a = "";
                                    for (int l = str.length()-3; l < str.length(); l++) {
                                        a = a + String.valueOf(str.charAt(l));
                                    }
                                    ratingOfI = Double.parseDouble(a);

                                    //this block gets the last three characters that is rating of the previous element of the array
                                    String str2=arr[j-1];
                                    String b = "";
                                    for (int k = str2.length()-3; k < str2.length(); k++) {
                                        b = b + String.valueOf(str2.charAt(k));
                                    }
                                    ratingOfIless = Double.parseDouble(b);

                                    //this block checks the max rating and sort the order accordingly
                                    if (ratingOfIless < ratingOfI) {
                                        String temp = arr[j];
                                        arr[j] = arr[j - 1];
                                        arr[j - 1] = temp;
                                        swapped = true;
                                    }
                                }
                                if(!swapped)
                                {
                                    break;
                                }
                            }
                            for (int last = 0; last < arr.length; last++) {

                                int number=last+1;
                                mTextViewResult.append(number+")\n"+arr[last]+"\n\n");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }









}