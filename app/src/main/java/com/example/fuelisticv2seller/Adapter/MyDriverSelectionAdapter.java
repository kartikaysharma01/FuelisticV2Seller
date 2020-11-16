package com.example.fuelisticv2seller.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuelisticv2seller.Callback.IRecyclerClickListener;
import com.example.fuelisticv2seller.Common.Common;
import com.example.fuelisticv2seller.Model.DriverModel;
import com.example.fuelisticv2seller.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyDriverSelectionAdapter extends RecyclerView.Adapter<MyDriverSelectionAdapter.MyViewHolder> {

    private Context context;
    List<DriverModel> driverModelList;
    private ImageView lastCheckedImageView = null;
    private DriverModel selectedDriver = null;

    public MyDriverSelectionAdapter(Context context, List<DriverModel> driverModelList) {
        this.context = context;
        this.driverModelList = driverModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_driver_selected,parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Common.setSpanString("", driverModelList.get(position).getFullName(),
                holder.txt_driver_name);
        Common.setSpanString("", driverModelList.get(position).getPhoneNo(),
                holder.txt_driver_phone);
        Common.setSpanString("", driverModelList.get(position).getLicensePlate(),
                holder.txt_driver_license_plate);

        holder.setiRecyclerClickListener((view, pos) ->{
            if(lastCheckedImageView != null){
                lastCheckedImageView.setImageResource(0);
            }
            holder.img_checked.setImageResource(R.drawable.ic_baseline_check_24);
            lastCheckedImageView = holder.img_checked;
            selectedDriver = driverModelList.get(pos);
        } );
    }

    public DriverModel getSelectedDriver() {
        return selectedDriver;
    }

    @Override
    public int getItemCount() {
        return driverModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Unbinder unbinder;

        @BindView(R.id.txt_driver_name)
        TextView txt_driver_name;

        @BindView(R.id.txt_driver_phone)
        TextView txt_driver_phone;

        @BindView(R.id.txt_driver_license_plate)
        TextView txt_driver_license_plate;

        @BindView(R.id.img_checkeds)
        ImageView img_checked;

        IRecyclerClickListener iRecyclerClickListener;

        public void setiRecyclerClickListener(IRecyclerClickListener iRecyclerClickListener) {
            this.iRecyclerClickListener = iRecyclerClickListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder= ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            iRecyclerClickListener.onItemClickListener(view, getAdapterPosition());
        }
    }
}
