package com.nathan630pm.nk_final_project.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nathan630pm.nk_final_project.OnParkingClickListener;
import com.nathan630pm.nk_final_project.ParkingDetailsFragment;
import com.nathan630pm.nk_final_project.ProfileFragment;
import com.nathan630pm.nk_final_project.R;
import com.nathan630pm.nk_final_project.ViewParkingFragment;
import com.nathan630pm.nk_final_project.models.Parking;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ParkingAdapter extends RecyclerView.Adapter<ParkingAdapter.ParkingViewHolder> {
    private static final String TAG = "ParkingAdapter";
    private Context context;
    private ArrayList<Parking> parkingList;
    private OnParkingClickListener clickListener;


    public ParkingAdapter(Context context, ArrayList<Parking> parkingList, OnParkingClickListener clickListener) {
        this.context = context;
        this.parkingList = parkingList;
        this.clickListener = clickListener;


    }

    @NonNull
    @Override
    public ParkingAdapter.ParkingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.parking_item, parent, false);
        return new ParkingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkingViewHolder holder, int position) {
        holder.bind(parkingList.get(position), clickListener);
        Parking item = parkingList.get(position);

        Log.e(TAG, "ADDED PARKING: " + item.toString());
        holder.tvAddr.setText("Parking Address:" + item.getParkingAddr());
        holder.tvDate.setText("Date: " + item.getDate().toString());
        holder.tvPlateNo.setText("Plate No: " + item.getCarPlateNumber());
        Picasso.get()
                .load(R.drawable.ic_parking_list_icon)
                .placeholder(R.drawable.ic_parking_list_icon)
                .into(holder.img);
        holder.parkingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Parking clicked with id: " + item.getId());
                clickListener.onParkingClickListener(item);
            }
        });


    }

    @Override
    public int getItemCount() {
        return parkingList.size();
    }


    public class ParkingViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvAddr, tvPlateNo, tvDate;
        RelativeLayout parkingLayout;


        public ParkingViewHolder(@NonNull View itemView) {
            super(itemView);
            parkingLayout = itemView.findViewById(R.id.parkingLayout);
            img = itemView.findViewById(R.id.img);
            tvAddr = itemView.findViewById(R.id.tvAddr);
            tvPlateNo = itemView.findViewById(R.id.tvPlateNo);
            tvDate = itemView.findViewById(R.id.tvDate);



        }

        public void bind(Parking parking, OnParkingClickListener parkingClickListener) {




            parkingLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    parkingClickListener.onParkingClickListener(parking);

                }
            });


        }
    }



}
