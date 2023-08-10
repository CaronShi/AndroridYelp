package com.example.helloworld;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private Context mContext;
    private List<String> mid;
    private OnItemClickListener mListener;
    private ArrayList<ArrayList<String>> ContentArray;
    private String name, status, lat, lng;
    private String phone;
    private String loca;
    private String yelp_url;
    private String price;
    //private String catego;
    //private ArrayList<ArrayList<String>> rating, user_name, time_created;

    List<String> reviews = new ArrayList<>();
    List<String> rating = new ArrayList<>();
    List<String> user_name = new ArrayList<>();
    List<String> time_created = new ArrayList<>();
    //Adapter Reference https://superior-leo.gitee.io/2021/01/31/as-zhi-ce-hua-jie-mian-dai-ma/#toc-heading-3

    public void setContentArray(ArrayList<ArrayList<String>> ContentArray) {
        this.ContentArray = ContentArray;
    }

    public RecycleAdapter(Context context, List<String> business_id, OnItemClickListener listener) {
        this.mid = business_id;
        this.mContext = context;
        this.mListener = listener;
    }

    public void get_reviews(String id) {

        String review_url = "https://hw8yelp.wl.r.appspot.com/reviews?id=" + id;
        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest revReq;

        revReq = new StringRequest(Request.Method.GET, review_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject resultJson = new JSONObject(response);
                            JSONArray revs = resultJson.getJSONArray("reviews");
                            Log.d("MainActivity resultJson", String.valueOf(resultJson));
                            int x = revs.length();
                            //Log.d("MainActivitycat", String.valueOf(x));
                            for (int i = 0; i < x; i++) {
                                JSONObject json = revs.getJSONObject(i);
                                String text = json.getString("text");
                                reviews.add(text);
                                rating.add( json.getString("rating")) ;
                                time_created.add(json.getString("time_created"));
                                user_name.add(json.getJSONObject("user").getString("name"));
                            }


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
        queue.add(revReq);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.table, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        List<String> photo_url = new ArrayList<>();

        if (ContentArray.size() == 0) {
            ((LinearViewHolder) holder).b_n.setText("");
            ((LinearViewHolder) holder).n.setText("");

        } else if (position >= ContentArray.size()) {
            ((LinearViewHolder) holder).b_n.setText("");
            ((LinearViewHolder) holder).n.setText("");

        } else {
            ArrayList<String> content = ContentArray.get(position);
            Log.i("ADAPTER ACTIVITY", String.valueOf(content));
            ((LinearViewHolder) holder).b_n.setText(content.get(0)); //Order: b_name, distance, img_url, id, n
            name = content.get(0);
            ((LinearViewHolder) holder).dist.setText(content.get(1));
            ((LinearViewHolder) holder).n.setText(content.get(4));
            String img_url = content.get(2);
            //Load Image
            Picasso.get().load(img_url).resize(50, 50).into(((LinearViewHolder) holder).img);

        }



        holder.itemView.setOnClickListener((v) -> {
            mListener.onClick(position);
            String id = mid.get(position);
            //API access to backend
            String detail_url = "https://hw8yelp.wl.r.appspot.com/detail?id=" + id;
            //



            RequestQueue queue = Volley.newRequestQueue(mContext);
            Intent intent = new Intent(mContext, TableActivity.class);
            get_reviews(id);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms

                    if (reviews.size() == 0) {
                        Log.i("MainActivity", "IP INFO LOST ip.length == 0");
                    } else {

                        StringRequest detailReq;
                        detailReq = new StringRequest(Request.Method.GET, detail_url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        String category = "";
                                        Log.d("MainActivity ONresponse", String.valueOf(rating));
                                        try {
                                            JSONObject resultJson = new JSONObject(response);

                                            name = resultJson.getString("name");
                                            //set toolbar's title to the name
                                            phone = resultJson.getString("display_phone");
                                            price = resultJson.getString("price");
                                            yelp_url = resultJson.getString("url");
                                            Log.d("MainActivity", response);

                                            //status
                                            String hour = resultJson.getString("hours");
                                            int l = hour.length();
                                            String hours = hour.substring(1, l);
                                            JSONObject open = new JSONObject(hours);
                                            String open_now = open.getString("is_open_now");
                                            if (open_now == "true") {
                                                status = "OPEN NOW";
                                            } else {
                                                status = "CLOSED";
                                            }

                                            //location
                                            String location = resultJson.getString("location");
                                            JSONObject loc = new JSONObject(location);
                                            String add1 = loc.getString("address1");
                                            String city = loc.getString("city");
                                            String state = loc.getString("state");
                                            String zip_code = loc.getString("zip_code");
                                            loca = add1 + " " + city + " " + state + " " + zip_code;

                                            //lat,lng
                                            String coordinates = resultJson.getString("coordinates");
                                            JSONObject coor = new JSONObject(coordinates);
                                            lat = coor.getString("latitude");
                                            lng = coor.getString("longitude");

                                            //lat = "34.0522342";
                                            //lng = "-118.2436849";

                                            //category
                                            JSONArray cat = resultJson.getJSONArray("categories");
                                            int x = cat.length();
                                            //Log.d("MainActivitycat", String.valueOf(x));
                                            for (int i = 0; i < x; i++) {
                                                String title = cat.getString(i);
                                                JSONObject json = new JSONObject(title);
                                                String cati = json.getString("title");
                                                //Log.d("MainActivity", String.valueOf(cati));
                                                category += cati;
                                                category += ", ";
                                            }

                                            //photos
                                            JSONArray p = resultJson.getJSONArray("photos");
                                            Log.d("MainActivity photo", String.valueOf(p));

                                            for (int i = 0; i < p.length(); i++) {
                                                String single_url = p.getString(i);
                                                photo_url.add(single_url);
                                            }


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        //Log.d("MainActivityphoto url", String.valueOf(photo_url));

                                        if (photo_url.size()==0){
                                            photo_url.add("https://www.justonecookbook.com/wp-content/uploads/2020/01/Sushi-Rolls-Maki-Sushi-%E2%80%93-Hosomaki-1106-II.jpg");
                                            photo_url.add("https://upload.wikimedia.org/wikipedia/commons/thumb/7/7a/Various_sushi%2C_beautiful_October_night_at_midnight.jpg/640px-Various_sushi%2C_beautiful_October_night_at_midnight.jpg");
                                            photo_url.add("https://www.happyfoodstube.com/wp-content/uploads/2016/03/homemade-sushi-image.jpg");
                                        }

                                        Log.d("MainActivityphoto url", String.valueOf(photo_url.get(0)));
                                        String photo1 = photo_url.get(0);
                                        String photo2 = photo_url.get(1);
                                        String photo3 = photo_url.get(2);

                                        intent.putExtra("photo2", photo2);
                                        intent.putExtra("photo3", photo3);
                                        intent.putExtra("photo1", photo1);
                                        intent.putExtra("name", name);
                                        intent.putExtra("loca", loca);
                                        intent.putExtra("yelp", yelp_url);
                                        intent.putExtra("categories", category);
                                        intent.putExtra("status", status);
                                        intent.putExtra("phone", phone);
                                        intent.putExtra("price", price);

                                        intent.putExtra("lat", lat);
                                        intent.putExtra("lng", lng);

                                        intent.putExtra("reviews", reviews.get(reviews.size()-3));
                                        intent.putExtra("reviews2", reviews.get(reviews.size()-2));
                                        intent.putExtra("reviews3", reviews.get(reviews.size()-1));


                                        intent.putExtra("rating", rating.get(rating.size()-3));
                                        intent.putExtra("rating2", rating.get(rating.size()-2));
                                        intent.putExtra("rating3", rating.get(rating.size()-1));

                                        intent.putExtra("user_name", user_name.get(user_name.size()-3));
                                        intent.putExtra("user_name2", user_name.get(user_name.size()-2));
                                        intent.putExtra("user_name3", user_name.get(user_name.size()-1));

                                        intent.putExtra("time_created", time_created.get(time_created.size()-3));
                                        intent.putExtra("time_created2", time_created.get(time_created.size()-2));
                                        intent.putExtra("time_created3", time_created.get(time_created.size()-1));

                                        //Log.d("MainActivity ID", String.valueOf(user_name));
                                        //Log.d("MainActivity ID", String.valueOf(reviews));
                                        //Log.d("MainActivity ID", String.valueOf(time_created));
                                        mContext.startActivity(intent);

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError e) {
                                        Log.i("MainActivity_IP", "onErrorResponse: fetch FAILED: " + e.toString());
                                    }
                                });
                        queue.add(detailReq);

                    }
                }


            }, 500);
            //Toast.makeText(mContext,"click..."+ holder.getAdapterPosition(),Toast.LENGTH_SHORT).show();
            //Intent Reference https://www.youtube.com/watch?v=ZXoGG2XTjzU

        });


    }


    @Override
    public int getItemCount() {
        return 10;
    }

    //change date
    class LinearViewHolder extends RecyclerView.ViewHolder {
        private TextView n, b_n, dist;
        private ImageView img;

        public LinearViewHolder(View itemView) {
            super(itemView);
            b_n = itemView.findViewById(R.id.b_n);
            n = itemView.findViewById(R.id.n);
            dist = itemView.findViewById(R.id.dist);
            img = itemView.findViewById(R.id.imgItem);
        }
    }


    public interface OnItemClickListener {
        void onClick(int pos);
    }
}