package com.example.helloworld;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Reservation_Recycler extends RecyclerView.Adapter<Reservation_Recycler.ViewHolder>{

    private ArrayList<String> memail_list = new ArrayList<>();
    private ArrayList<String> mdate_list = new ArrayList<>();
    private ArrayList<String> mtime_list = new ArrayList<>();
    private ArrayList<String> mb_n_list = new ArrayList<>();
private Context mContext;

public Reservation_Recycler(ArrayList<String> email_list, ArrayList<String> date_list,ArrayList<String> time_list,ArrayList<String> b_n_list, Context context){
    memail_list = email_list;
    mdate_list = date_list;
    mtime_list =time_list;
    mb_n_list = b_n_list;
    mContext = context;
}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(mContext).inflate(R.layout.reservation_list, parent, false);
ViewHolder holder= new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.email.setText(memail_list.get(position));
        holder.date.setText(mdate_list.get(position));
        holder.b_n.setText(mb_n_list.get(position));
        holder.time.setText(mtime_list.get(position));
        int i = memail_list.size();
        holder.n.setText(String.valueOf(position+1));
    }

    @Override
    public int getItemCount() {
        return memail_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
    TextView date,time,email,b_n,n;
    LinearLayout parent_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            email = itemView.findViewById(R.id.email);
            b_n = itemView.findViewById(R.id.b_n);
            parent_layout = itemView.findViewById(R.id.parent_layout);
            n = itemView.findViewById(R.id.n);
        }
    }
}
