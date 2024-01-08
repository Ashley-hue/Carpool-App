package com.example.carpoolingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {

    List<Paymeant> paymeantList;
    Context context;

    public PaymentAdapter(List<Paymeant> paymeantList, Context context) {
        this.paymeantList = paymeantList;
        this.context = context;
    }

    @NonNull
    @Override
    public PaymentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentAdapter.ViewHolder holder, int position) {
        Paymeant payup = paymeantList.get(position);

        holder.drives.setText(payup.getDrive());
        holder.passen.setText(payup.getPassenger());
        holder.amount.setText(String.valueOf(payup.getPricings()));
    }

    @Override
    public int getItemCount() {
        return paymeantList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView drives, passen, amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            drives = itemView.findViewById(R.id.driving);
            passen = itemView.findViewById(R.id.passy);
            amount = itemView.findViewById(R.id.costy);
        }
    }
}
