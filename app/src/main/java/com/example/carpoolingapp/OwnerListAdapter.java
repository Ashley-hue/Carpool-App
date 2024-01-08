package com.example.carpoolingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OwnerListAdapter extends RecyclerView.Adapter<OwnerListAdapter.ViewHolder> {

    List<Userr> carOwning;
    DatabaseHelper dbHelper ;
    Context context;

    public OwnerListAdapter(List<Userr> carOwning, Context context) {
        this.carOwning = carOwning;
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public OwnerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OwnerListAdapter.ViewHolder holder, int position) {
        Userr userr = carOwning.get(position);


        holder.owning.setText(userr.getEmail());
        holder.tele.setText(userr.getPhone());
        holder.fromWhere.setText(userr.getAddress());

        holder.deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adPosition = holder.getBindingAdapterPosition();
                if(adPosition != RecyclerView.NO_POSITION){
                    String mai =carOwning.get(adPosition).getEmail();
                    dbHelper.deleteUser(mai);
                    carOwning.remove(adPosition);
                    notifyItemRemoved(adPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return carOwning.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView owning, tele, fromWhere;
        ImageView deleteUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            owning = itemView.findViewById(R.id.who);
            tele = itemView.findViewById(R.id.callme);
            fromWhere = itemView.findViewById(R.id.where);
            deleteUser = itemView.findViewById(R.id.delete_owner);
        }
    }
}
