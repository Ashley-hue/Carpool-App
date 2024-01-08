package com.example.carpoolingapp;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.List;

public class BookedTripsAdapter extends RecyclerView.Adapter<BookedTripsAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    List<Trip> bookedTrips;
    private static final int ERROR_DIALOG_REQUEST = 9001;

    OnItemClickListener listenerr;

    public BookedTripsAdapter(List<Trip> bookedTrips) {
        this.bookedTrips = bookedTrips;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        listenerr = itemClickListener;
    }

    @NonNull
    @Override
    public BookedTripsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booked_trips_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookedTripsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Trip btrips = bookedTrips.get(position);

        holder.ab.setText(btrips.getMerlin());
        holder.cd.setText(btrips.getDestination());
        holder.ef.setText(btrips.getTiming());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listenerr != null){
                    listenerr.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookedTrips.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ab, cd, ef, trackRide;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ab = itemView.findViewById(R.id.abc);
            cd = itemView.findViewById(R.id.def);
            ef = itemView.findViewById(R.id.ghi);
            trackRide = itemView.findViewById(R.id.viewMap);

            trackRide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isServicesOK()){
                        Intent intent = new Intent(v.getContext(), LeMap.class);
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }

        public boolean isServicesOK() {
            Log.d(TAG, "isServicesOK: checking google services version");

            int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(itemView.getContext());
            if (available == ConnectionResult.SUCCESS) {
                Log.d(TAG, "isServicesOK: Google play services is working");
                return true;
            } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
                Log.d(TAG, "isServicesOK: Error occured but is fixable");
                Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog((Activity) itemView.getContext(), available, ERROR_DIALOG_REQUEST);
                dialog.show();
            } else {
                Toast.makeText(itemView.getContext(), "You can't make map requests", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }
}
