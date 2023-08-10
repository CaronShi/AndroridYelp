package com.example.helloworld;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class FragReview extends Fragment {
    private TextView review, rating, user_name, time_created;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_frag_review, container, false);

        TableActivity activity = (TableActivity) getActivity();

        String my_r = activity.getrating();
        rating = view.findViewById(R.id.rating);
        String my_rating = "Rating :"+my_r+"/5";
        rating.setText(my_rating);

        String my_r3 = activity.getrating2();
        rating = view.findViewById(R.id.rating2);
        String my_rating3 = "Rating :"+my_r3+"/5";
        rating.setText(my_rating3);

        String my_r2 = activity.getrating3();
        rating = view.findViewById(R.id.rating3);
        String my_rating2 = "Rating :"+my_r2+"/5";
        rating.setText(my_rating2);


        String my_u = activity.getuser_name();
        user_name = view.findViewById(R.id.user_name);
        user_name.setText(my_u);

        String my_u2 = activity.getuser_name2();
        user_name = view.findViewById(R.id.user_name2);
        user_name.setText(my_u2);

        String my_u3 = activity.getuser_name3();
        user_name = view.findViewById(R.id.user_name3);
        user_name.setText(my_u3);

        String t = activity.gettime_created();
        String my_t = t.substring(0,10);
        time_created  = view.findViewById(R.id.time_value);
        time_created.setText(my_t);

        String t2 = activity.gettime_created2();
        String my_t2 = t2.substring(0,10);
        time_created  = view.findViewById(R.id.time_value2);
        time_created .setText(my_t2);

        String t3 = activity.gettime_created3();
        String my_t3 = t3.substring(0,10);
        time_created  = view.findViewById(R.id.time_value3);
        time_created .setText(my_t3);

        String my_re = activity.getreviews();
        review  = view.findViewById(R.id.review);
        review.setText(my_re);

        String my_re2 = activity.getreviews2();
        review  = view.findViewById(R.id.review2);
        review .setText(my_re2);

        String my_re3 = activity.getreviews3();
        review  = view.findViewById(R.id.review3);
        review .setText(my_re3);

        return view;
    }
}