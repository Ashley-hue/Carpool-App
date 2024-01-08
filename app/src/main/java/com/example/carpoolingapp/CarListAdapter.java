package com.example.carpoolingapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CarListAdapter extends RecyclerView.Adapter<CarListAdapter.ViewHolder> {

    public interface onCarClickListener{
        void onCarClick(int position);
    }

    public interface carDeleteListener{
        void onCarDelete(int position);
    }

    private List<Car> cars;
    Context context;
    private onCarClickListener carClickListener;
    private carDeleteListener listener;


    public CarListAdapter(List<Car> cars, Context context, onCarClickListener carClickListener, carDeleteListener listener) {
        this.cars = cars;
        this.context = context;
        this.carClickListener = carClickListener;
        this.listener = listener;
    }



    @NonNull
    @Override
    public CarListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_list_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarListAdapter.ViewHolder holder, int position) {
        Car carList = cars.get(position);

        holder.regNum.setText(carList.getReg());
        holder.vehicleName.setText(carList.getCarname());
        holder.year.setText(String.valueOf(carList.getModelYear()));
        holder.imageView.setImageBitmap(getImageFromByteArray(carList.getImage()));
        holder.bin.setOnClickListener(v -> {
            listener.onCarDelete(position);
        });
    }

    private Bitmap getImageFromByteArray(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView imageView, bin;
        TextView regTitle, carTitle, modelTitle, regNum, vehicleName, year;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardImage);
            imageView = itemView.findViewById(R.id.carImage);
            regTitle = itemView.findViewById(R.id.regisTitle);
            regNum = itemView.findViewById(R.id.regisNo);
            carTitle = itemView.findViewById(R.id.vehicleTitle);
            vehicleName = itemView.findViewById(R.id.vehicleName);
            modelTitle = itemView.findViewById(R.id.modelTitle);
            year = itemView.findViewById(R.id.modelyear);
            bin = itemView.findViewById(R.id.delete_car);

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                carClickListener.onCarClick(position);
            });
        }
    }
}
