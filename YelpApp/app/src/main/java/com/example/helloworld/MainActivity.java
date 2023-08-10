package com.example.helloworld;

import static java.lang.Float.parseFloat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //validation reference https://www.geeksforgeeks.org/implement-form-validation-error-to-edittext-in-android/
    //add dynamic table https://stackoverflow.com/questions/18207470/adding-table-rows-dynamically-in-android


    //declare variables
    private Button onsubmit, onclear;
    private TextView location, distance;
    private AutoCompleteTextView keyword;
    private ImageView calender;
    private CheckBox checkbox;
    private Spinner spinner;
    boolean validate_all = false;
    private RecyclerView mRVFinish;
    private Button test;
    private List<String> ip_list = new ArrayList<>();
    private List<String> business_id = new ArrayList<>();

    //private String[] autocomplete_list =new String [0];
    private ArrayList<String> autocomplete_list = new ArrayList<String>();
    private ArrayAdapter<String> array_adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        keyword = (AutoCompleteTextView) findViewById(R.id.keyword);

        array_adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, autocomplete_list);
        keyword.setThreshold(1);//will start working from first character
        keyword.setAdapter(array_adapter);//setting the adapter data into the AutoCompleteTextView


        //if (keyword.length() > 0) {  }

        //auto listener
        //
        keyword.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword_autocomplete = String.valueOf(keyword.getText());
                update_autocomplete(keyword_autocomplete);


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });


        //set toolbar
        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);
        calender = findViewById(R.id.toolbar_calender);
        calender.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReservationActvity.class);
                startActivity(intent);
            }
        });


        //find all view ids
        spinner = findViewById(R.id.spinner);
        location = findViewById(R.id.location);
        distance = findViewById(R.id.distance);
        checkbox = findViewById(R.id.checkbox);
        onsubmit = findViewById(R.id.submit);
        onclear = findViewById(R.id.clear);
        mRVFinish = findViewById(R.id.table_view);


        //spinner: category adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Data Recycler adapter
        RecycleAdapter myAdapter = new RecycleAdapter(MainActivity.this, business_id, pos ->
                Log.i("Mainactivity get position", String.valueOf(pos)));

        mRVFinish.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mRVFinish.setAdapter(myAdapter);

        //checkbox listener
        checkbox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    //CODE TO MAKE THE EDITTEXT ENABLED
                    location.setEnabled(false);
                    location.setVisibility(View.INVISIBLE);
                } else if (!((CheckBox) v).isChecked()) {
                    //CODE TO MAKE THE EDITTEXT DISABLED
                    location.setEnabled(true);
                    location.setFocusable(true);
                    distance.setText("");
                    location.setVisibility(View.VISIBLE);
                }
            }

        });


        ArrayList<ArrayList<String>> myArray = new ArrayList<ArrayList<String>>();
        myArray.add(new ArrayList<String>());
        myArray.get(0).add("loading");
        myArray.get(0).add("loading");
        myArray.get(0).add("loading");
        myArray.get(0).add("loading");
        myArray.get(0).add("loading");

        myAdapter.setContentArray(myArray);

        validate_all = validate();

        //set clear button
        onclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword.setText("");
                keyword.clearFocus();
                location.setVisibility(View.VISIBLE);

                location.setText("");
                location.setEnabled(true);
                location.clearFocus();

                distance.setText("");
                distance.clearFocus();

                checkbox.setChecked(false);
                spinner.setSelection(0);

                mRVFinish.setVisibility(View.GONE);
                TextView no_result = findViewById(R.id.no_result);
                no_result.setVisibility(View.INVISIBLE);

                ip_list.clear();
                business_id.clear();
            }
        });

        //set submit button
        onsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate_all = validate();
                //get Text in all input textviews
                String dist = distance.getText().toString();
                if (dist.length() == 0) {
                    dist = "5";
                }

                int d = Integer.parseInt(dist);
                String term = keyword.getText().toString();
                String cate = spinner.getSelectedItem().toString();
                String cat = cate.replace("&", ",");


                find_ip();//call the func getting ip info

                //Wait handler Reference https://stackoverflow.com/questions/15874117/how-to-set-delay-in-android
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        if (ip_list.size() == 0) {
                            Log.i("MainActivity", "IP INFO LOST ip.length == 0");
                        } else {
                            String lat = ip_list.get(0);
                            String longt = ip_list.get(1);

                            if (validate_all) {
                                mRVFinish.setVisibility(View.VISIBLE);
                                //table.setVisibility(View.VISIBLE); //visible, invisible(but keep places), gone(not keep place)
                                //String longti = "-118.2436849";
                                //String lati = "34.0522342";
                                //Access to Yelp API
                                String url = "https://hw8yelp.wl.r.appspot.com";
                                String table_url = url + "/search?term=" + term + "&categories=" + cat + "&longitude=" + longt + "&latitude=" + lat + "&distance=" + d;
                                Log.d("MainActivity url", table_url);

                                //String table_url = "https://hw8yelp.wl.r.appspot.com/search?term=Sushi&categories=all&longitude=-118.2436849&latitude=34.0522342&distance=24";
                                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                StringRequest portReq;
                                portReq = new StringRequest(Request.Method.GET, table_url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                //String json = response;
                                                try {
                                                    JSONObject resultJson = new JSONObject(response);
                                                    String businesses_info = resultJson.getString("businesses");//[{},{}..{}]
                                                    //Log.d("MainActivity_getJSONObject businesses", businesses_info);
                                                    JSONArray arr = new JSONArray(businesses_info);
                                                    if (arr.length() == 0) {
                                                        TextView no_result = findViewById(R.id.no_result);
                                                        no_result.setVisibility(View.VISIBLE);
                                                        RecyclerView table_view = (RecyclerView) findViewById(R.id.table_view);
                                                        table_view.setVisibility(View.INVISIBLE);
                                                    }
                                                    Log.d("MainActivity_getJSONObject businesses", String.valueOf(arr));
                                                    ArrayList<ArrayList<String>> myArray = new ArrayList<ArrayList<String>>();

                                                    for (int i = 0; i < 10; i++) {
                                                        JSONObject obj = arr.getJSONObject(i);
                                                        String businesses_id = obj.getString("id");
                                                        String businesses_img = obj.getString("image_url");
                                                        String businesses_name = obj.getString("name");
                                                        String businesses_distance = obj.getString("distance");

                                                        myArray.add(new ArrayList<String>());

                                                        myArray.get(i).add(businesses_name);
                                                        int dist = (int) parseFloat(businesses_distance);
                                                        int d = dist / 1609;
                                                        myArray.get(i).add(String.valueOf(d));
                                                        myArray.get(i).add(businesses_img);
                                                        myArray.get(i).add(String.valueOf(d));
                                                        int n = i + 1;
                                                        myArray.get(i).add(String.valueOf(n));
                                                        business_id.add(businesses_id);
                                                        //Log.d("MainActivity businesses", String.valueOf(business_id));
                                                    }

                                                    myAdapter.setContentArray(myArray);
                                                    myAdapter.notifyDataSetChanged();


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError e) {
                                                Log.i("MainActivity", "onErrorResponse: fetch FAILED: " + e.toString());

                                            }
                                        });
                                queue.add(portReq);


                            }
                        }
                    }
                }, 800);
            }
        });
    }

    //out of OnCreate()
    private boolean validate() {
        //keyword validate
        if (keyword.length() == 0) {
            keyword.setError("This field is required");
            return false;
        }

        if (distance.length() == 0) {
            distance.setError("This field is required");
            return false;
        }

//location validate
        if (location.length() == 0 && !checkbox.isChecked()) {
            location.setError("This field is required");
            return false;
        }
        return true;
    }

    private void find_ip() {

        if (checkbox.isChecked()) {
            //find location's long and latitude
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String ip_url = "https://ipinfo.io/json?token=e06090c82c5936";
            StringRequest ipReq;
            ipReq = new StringRequest(Request.Method.GET, ip_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject resultJson = new JSONObject(response);
                                String loca = resultJson.getString("loc");
                                String longt = loca.split(",")[0];
                                String lat = loca.split(",")[1];
                                //List<String> list = new ArrayList<>();
                                ip_list.add(longt);
                                ip_list.add(lat);
                                //Log.i("MainActivity_IP", "IP INFO List" + ip_list);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError e) {
                            Log.i("MainActivity_IP", "onErrorResponse: fetch FAILED: " + e.toString());
                        }
                    });
            queue.add(ipReq);

        } else if (!checkbox.isChecked()) {
            //get google api
            String gapi = "AIzaSyCV7zMBjSOoQTvBT_rPTEIzYSmlXQggfQs";
            String loc = location.getText().toString();
            String ip_url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + loc + "&key=" + gapi;
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            StringRequest ipReq;
            ipReq = new StringRequest(Request.Method.GET, ip_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject resultJson = new JSONObject(response);
                                String res = resultJson.getString("results");
                                int res_len = res.length();
                                res = res.substring(1, res_len);
                                JSONObject resJson = new JSONObject(res);
                                String loc = resJson.getString("geometry");
                                JSONObject rJson = new JSONObject(loc);
                                String loca = rJson.getString("location");
                                JSONObject l = new JSONObject(loca);
                                String lat = l.getString("lng");
                                String longt = l.getString("lat");
                                ip_list.add(longt);
                                ip_list.add(lat);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError e) {
                            Log.i("MainActivity_IP", "onErrorResponse: fetch FAILED: " + e.toString());
                        }
                    });
            queue.add(ipReq);

        }
    }

    private void update_autocomplete(String s) {
        //autocomplete_list = new ArrayList<>();
        autocomplete_list.clear();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://hw8yelp.wl.r.appspot.com/autocomplete?text=" + s;
        StringRequest Req;
        Req = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray categories = new JSONObject(response).getJSONArray("categories");
                            JSONArray terms = new JSONObject(response).getJSONArray("terms");


                            int csize = categories.length();
                            for (int i = 0; i < csize; i++) {
                                JSONObject term = categories.getJSONObject(i);

                                String title = term.getString("title");

                                autocomplete_list.add(title);
                            }

                            for (int i = 0; i < terms.length(); i++) {
                                JSONObject term = terms.getJSONObject(i);
                                String text = term.getString("text");
                                autocomplete_list.add(text);
                            }
                            Log.i("autocomplete1", String.valueOf(autocomplete_list));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        array_adapter.clear();
                        array_adapter.addAll(autocomplete_list);
                        array_adapter.notifyDataSetChanged();
                        keyword.refreshAutoCompleteResults();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        Log.i("MainActivity_Autocomplete", "onErrorResponse: fetch FAILED: " + e.toString());
                    }
                });
        queue.add(Req);
        //return autocomplete_list;


    }
}
