package com.example.helloworld;


import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TableActivity extends MainActivity {

    //TABLAYOUT reference https://www.youtube.com/watch?v=ziJ6-AT3ymg
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String photo1, price, lat, lng;
    private String photo3;
    private String photo2;
    private String phone;
    private String loc,name;
    private String yelp;
    private String category;
    private String status;
    private String rating, rating2, rating3, user_name, user_name2, user_name3, time_created,time_created2,time_created3,
    review,review2,review3;
    private TextView title;
    private ImageView twitter,fb,back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_table);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.tab_page);
        tabLayout.setupWithViewPager(viewPager);
        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);





        /*String photo1 = intent.getStringExtra("photo1");
        String name = intent.getStringExtra("name");
        String loc = intent.getStringExtra("loca");
        String yelp = intent.getStringExtra("yelp");
        String category = intent.getStringExtra("categories");
        String status = intent.getStringExtra("status");*/
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        title = findViewById(R.id.toolbar_name);
        title.setText(name);


        photo1 = intent.getStringExtra("photo1");
        photo2 = intent.getStringExtra("photo2");
        photo3 = intent.getStringExtra("photo3");

        lat = intent.getStringExtra("lat");
        lng = intent.getStringExtra("lng");

        loc = intent.getStringExtra("loca");
        yelp = intent.getStringExtra("yelp");
        category = intent.getStringExtra("categories");
        status = intent.getStringExtra("status");
        phone = intent.getStringExtra("phone");
        price = intent.getStringExtra("price");

        rating = intent.getStringExtra("rating");
        rating2 = intent.getStringExtra("rating2");
        rating3 = intent.getStringExtra("rating3");

        review = intent.getStringExtra("reviews");
        review2 = intent.getStringExtra("reviews2");
       review3 = intent.getStringExtra("reviews3");

        user_name = intent.getStringExtra("user_name");
        user_name2 = intent.getStringExtra("user_name2");
        user_name3 = intent.getStringExtra("user_name3");

        time_created = intent.getStringExtra("time_created");
         time_created2 = intent.getStringExtra("time_created2");
        time_created3 = intent.getStringExtra("time_created3");


        vpAdapter.addFrag(new FragDetail(), "business details");
        vpAdapter.addFrag(new FragmentMaps(), "Map location");
        vpAdapter.addFrag(new FragReview(), "reviews");

        viewPager.setAdapter(vpAdapter);

        Log.d("MainActivitytrating", String.valueOf(rating));
//Load Image Picasso.get().load(URL).resize(50, 50).into(LAYOUT);

        back = findViewById(R.id.toolbar_back);
        fb = findViewById(R.id.toolbar_fb);
        twitter = findViewById(R.id.toolbar_twitter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(TableActivity.this, MainActivity.class);
                //startActivity(intent);
                finish();
            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                String uri =  "https://twitter.com/intent/tweet?text=" + "Check"+ name + "on Yelp" + "&url=" + yelp;
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }

        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                String uri = "https://www.facebook.com/sharer/sharer.php?u=" + yelp;
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }

        });
    }

    //https://stackoverflow.com/questions/12739909/send-data-from-activity-to-fragment-in-android
    public String getPhoto1() {
        return photo1;
    }

    public String getPhoto2() {
        return photo2;
    }

    public String getPhoto3() {
        return photo3;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {return lng;}
    public String getYelp() {
        return yelp;
    }
public String getName(){return name;};
    public String getLoc() {
        return loc;
    }

    public String getStatus() {
        return status;
    }

    public String getCategory() {
        return category;
    }

    public String getPhone() {
        return phone;
    }

    public String getPrice() {
        return price;
    }

    public String getuser_name() {
        return user_name;
    }
    public String getuser_name2() {
        return user_name2;
    }
    public String getuser_name3() {
        return user_name3;
    }

    public String gettime_created() {
        return time_created;
    }
    public String gettime_created2() {
        return time_created2;
    }
    public String gettime_created3() {
        return time_created3;
    }

    public String getreviews() {
        return review;}

    public String getreviews2() {
        return review2;
    }
    public String getreviews3() {
        return review3;
    }

    public String getrating() {
        return rating;
    }
    public String getrating2() {
        return rating2;
    }
    public String getrating3() {
        return rating3;
    }





}



