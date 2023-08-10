package com.example.helloworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;


public class ReservationActvity extends AppCompatActivity {
    private ArrayList<String> email_list = new ArrayList<>();
    private ArrayList<String> date_list = new ArrayList<>();
    private ArrayList<String> time_list = new ArrayList<>();
    private ArrayList<String> b_n_list = new ArrayList<>();
    private Reservation_Recycler r_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        //Referenc https://www.youtube.com/watch?v=jiD2fxn8iKA
        SharedPreferences sp = getApplicationContext().getSharedPreferences("reservation", Context.MODE_PRIVATE);

        //get data trnafer to string
        String[] split_email = sp.getString("email", "").split(",");
        for (int i = 0; i < split_email.length; i++) {
            String item = split_email[i];
            if (item != "") email_list.add(item);
        }

        String[] split_date = sp.getString("date", "").split(",");
        for (int i = 0; i < split_date.length; i++) {
            String item = split_date[i];
            if (item != "") date_list.add(item);
        }

        String[] split_time = sp.getString("time", "").split(",");
        for (int i = 0; i < split_time.length; i++) {
            String item = split_time[i];
            if (item != "") time_list.add(item);
        }

        String[] split_b_n = sp.getString("b_n", "").split(",");
        for (int i = 0; i < split_b_n.length; i++) {
            String item = split_b_n[i];
            if (item != "") b_n_list.add(item);
        }

        //go back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_reservation);
        // getSupportActionBar().setDisplayShowTitleEnabled(false); ;
        ImageView back = (ImageView) findViewById(R.id.toolbar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (email_list.size() == 0 || date_list.size() == 0) {
            TextView no_reservation = findViewById(R.id.no_reservation);
            no_reservation.setVisibility(View.VISIBLE);
            RecyclerView reservation_recycler = findViewById(R.id.reservation_recycler);
            reservation_recycler.setVisibility(View.GONE);
        } else {

            initRecyclerView();
        }

    }

    private void initRecyclerView() {

        RecyclerView recyclerView = findViewById(R.id.reservation_recycler);
        r_adapter = new Reservation_Recycler(email_list, date_list, time_list, b_n_list, this);
        recyclerView.setAdapter(r_adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            email_list.remove(viewHolder.getAdapterPosition());

            //date_list.remove();
            //time_list.remove(viewHolder.getAdapterPosition());
            //b_n_list.remove(viewHolder.getAdapterPosition());

            int position = viewHolder.getAdapterPosition();

            SharedPreferences sp = getApplicationContext().getSharedPreferences("reservation", 0);
            //sp.edit().remove("email").commit();
            //sp.edit().remove("date").commit();
            //sp.edit().remove("time").commit();
            //sp.edit().remove("b_n").commit();

            // get current reservations
            ArrayList<String> saved_email = new ArrayList<String>(Arrays.asList(sp.getString("email", "").split(",")));
            ArrayList<String> saved_date = new ArrayList<String>(Arrays.asList(sp.getString("date", "").split(",")));
            ArrayList<String> saved_time = new ArrayList<String>(Arrays.asList(sp.getString("time", "").split(",")));
            ArrayList<String> saved_b_n = new ArrayList<String>(Arrays.asList(sp.getString("b_n", "").split(",")));

            saved_email.remove(position);
            saved_date.remove(position);
            saved_time.remove(position);
            saved_b_n.remove(position);


            //declare lists and put list
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("email", String.join(",", saved_email));
            editor.putString("date", String.join(",", saved_date));
            editor.putString("time", String.join(",", saved_time));
            editor.putString("b_n", String.join(",", saved_b_n));
            editor.commit();

            r_adapter.notifyDataSetChanged();
            if (email_list.size() == 0) {
                TextView x = findViewById(R.id.no_reservation);
                x.setVisibility(View.VISIBLE);

            }
        }
    };
}