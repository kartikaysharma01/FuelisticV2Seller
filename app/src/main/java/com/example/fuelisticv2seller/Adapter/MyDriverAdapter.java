package com.example.fuelisticv2seller.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuelisticv2seller.Common.Common;
import com.example.fuelisticv2seller.Model.DriverModel;
import com.example.fuelisticv2seller.Model.EventBus.UpdateDriverEvent;
import com.example.fuelisticv2seller.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyDriverAdapter extends RecyclerView.Adapter<MyDriverAdapter.MyViewHolder> {

    Context context;
    List<DriverModel> driverModelList;

    public MyDriverAdapter(Context context, List<DriverModel> driverModelList) {
        this.context = context;
        this.driverModelList = driverModelList;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_driver,parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        Log.i("driverModelList",driverModelList.get(position).toString());
//        Log.i("holder.txt_driver_name",holder.txt_driver_name.toString());
        Common.setSpanString("", driverModelList.get(position).getFullName(),
                holder.txt_driver_name);
        Common.setSpanString("", driverModelList.get(position).getPhoneNo(),
                holder.txt_driver_phone);
        Common.setSpanString("", driverModelList.get(position).getLicensePlate(),
                holder.txt_driver_license_plate);

        holder.btn_enable.setChecked(driverModelList.get(position).isActive());

        holder.btn_enable.setOnCheckedChangeListener((compoundButton, b) -> {
            EventBus.getDefault().postSticky(new UpdateDriverEvent(driverModelList.get(position), b));
        });
    }

    @Override
    public int getItemCount() {
        return driverModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.txt_driver_name)
        TextView txt_driver_name;

        @BindView(R.id.txt_driver_phone)
        TextView txt_driver_phone;

        @BindView(R.id.txt_driver_license_plate)
        TextView txt_driver_license_plate;

        @BindView(R.id.btn_enable)
        SwitchCompat btn_enable;

        private Unbinder unbinder;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }
    }


}
