package com.example.helloworld;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

import java.util.HashSet;
import java.util.Locale;

import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Set;
import java.util.regex.Pattern;


public class FragDetail extends Fragment {
    private TextView address;
    private Button button;

    private int hour, minute, year, month, day;
    private EditText Date, Time, email;
    private TextView a, pr, ph, s, c, v;
    private String date, time, email_address;
    private SharedPreferences sp;
    String TAG = "Mainactivity Time";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag_detail, container, false);
        TableActivity activity = (TableActivity) getActivity();
        //final Context context = this;

        button = view.findViewById(R.id.reserve_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.activity_dialog);
                String name = activity.getName();
                dialog.setTitle(name);
                TextView cancel = dialog.findViewById(R.id.cancel_button);
                dialog.show();

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                final Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                //sp Referenc https://www.youtube.com/watch?v=jiD2fxn8iKA
                sp = getContext().getSharedPreferences("reservation", Context.MODE_PRIVATE);
                TextView submit = dialog.findViewById(R.id.submit_button);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        email = dialog.findViewById(R.id.email_id);
                        String email_check = String.valueOf(email.getText());


                        if (!email_check.contains("@")) {
                            Toast.makeText(getContext(), "Invalid Email Address.", Toast.LENGTH_LONG).show();
                        } else if (hour < 10 || hour > 17) {
                            Toast.makeText(getContext(), "Time should be between 10 AM and 5 PM.", Toast.LENGTH_LONG).show();
                        } else {
                            //save reservation
                            email_address = email.getText().toString();
                            time = Time.getText().toString();
                            date = Date.getText().toString();

                            // get current reservations
                            String saved_email = sp.getString("email", "");
                            String saved_date = sp.getString ("date", "");
                            String saved_time = sp.getString ("time", "");
                            String saved_b_n = sp.getString ("b_n", "");

                            //declare lists and put list
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("email",  saved_email +  email_address+",");
                            editor.putString("date", saved_date + date+",");
                            editor.putString("time", saved_time + time+",");
                            editor.putString("b_n", saved_b_n + name+",");
                            editor.commit();


                            //Log.i("SharedPreferences detail ", check);
                            Log.i("SharedPreferences detail", email_address);
                            dialog.dismiss();
                            Toast.makeText(getContext(), "Reservation Booked.", Toast.LENGTH_LONG).show();
                        }

                        //save to reservation table

                    }
                });

                Date = dialog.findViewById(R.id.date_id);
                Time = dialog.findViewById(R.id.time_id);

                Date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog dialog1 = new DatePickerDialog(dialog.getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month = month + 1;
                                String date = dayOfMonth + "/" + month + "/" + year;
                                Date.setText(date);
                            }
                        }, year, month, day);
                        dialog1.show();
                    }
                });

                Time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                hour = selectedHour;
                                minute = selectedMinute;
                                Time.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));


                            }
                        };


                        TimePickerDialog Dialog2 = new TimePickerDialog(dialog.getContext(), onTimeSetListener, hour, minute, false);

                        Dialog2.show();

                    }
                });


            }
        });






        //get bold
        a = view.findViewById(R.id.address);
        a.setTypeface(null, Typeface.BOLD);


        s = view.findViewById(R.id.status);
        s.setTypeface(null, Typeface.BOLD);

        ph = view.findViewById(R.id.phone);
        ph.setTypeface(null, Typeface.BOLD);

        pr = view.findViewById(R.id.price);
        pr.setTypeface(null, Typeface.BOLD);

        c = view.findViewById(R.id.category_detail);
        c.setTypeface(null, Typeface.BOLD);

        v = view.findViewById(R.id.visit);
        v.setTypeface(null, Typeface.BOLD);


        String my_address = activity.getLoc();
        address = view.findViewById(R.id.address_ans);
        address.setText(my_address);


        String getStatus = activity.getStatus();
        address = view.findViewById(R.id.status_ans);
        address.setText(getStatus);

        String getCategory = activity.getCategory();
        address = view.findViewById(R.id.category_ans);
        address.setText(getCategory);

        String getPhone = activity.getPhone();
        address = view.findViewById(R.id.phone_ans);
        address.setText(getPhone);

        String getPrice = activity.getPrice();
        address = view.findViewById(R.id.price_ans);
        address.setText(getPrice);


        String getYelp = activity.getYelp();
        TextView visit = view.findViewById(R.id.visit_ans);
        String text = getYelp;

        visit.setText(text);
        //visit.setMovementMethod(LinkMovementMethod.getInstance());

        //photos
        String photo1 = activity.getPhoto1();
        ImageView img1 = view.findViewById(R.id.img1);
        Picasso.get().load(photo1).into(img1);

        String photo2 = activity.getPhoto2();
        ImageView img2 = view.findViewById(R.id.img2);
        Picasso.get().load(photo2).into(img2);

        String photo3 = activity.getPhoto3();
        ImageView img3 = view.findViewById(R.id.img3);
        Picasso.get().load(photo3).into(img3);


        return view;
    }

//Clickable Textview https://stackoverflow.com/questions/2734270/how-to-make-links-in-a-textview-clickable


}