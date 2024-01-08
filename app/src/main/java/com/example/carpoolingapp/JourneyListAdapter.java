package com.example.carpoolingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class JourneyListAdapter extends RecyclerView.Adapter<JourneyListAdapter.ViewHolder> implements Filterable {


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    List<Trip> tripList;
    List<Trip> fullTrip;
    Context context;
    OnItemClickListener listener;

    public JourneyListAdapter(Context context, List<Trip> tripList) {
        this.context = context;
        this.tripList = tripList;
        this.fullTrip = new ArrayList<>(tripList);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        listener = itemClickListener;
    }

    @Override
    public Filter getFilter() {
        return itemFilter;
    }


    private Filter itemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Trip> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(fullTrip);
            }
            else{
                String filteredP = constraint.toString().toLowerCase().trim();

                for (Trip prit : fullTrip){
                    if(prit.getSource().toLowerCase().contains(filteredP) || prit.getDestination().toLowerCase().contains(filteredP)){
                        filteredList.add(prit);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            tripList.clear();
            tripList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    @NonNull
    @Override
    public JourneyListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.journey_list_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JourneyListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Trip trip = tripList.get(position);

        holder.origin.setText(trip.getSource());
        holder.destination.setText(trip.getDestination());
        holder.timey.setText(trip.getTiming());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onItemClick(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout relativeLayout;
        TextView origin, destination, timey;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.journrecyclerview);
            origin = itemView.findViewById(R.id.origin);
            destination = itemView.findViewById(R.id.destt);
            timey = itemView.findViewById(R.id.timmy);
        }
    }
}
