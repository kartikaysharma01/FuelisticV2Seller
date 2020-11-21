package com.example.fuelisticv2seller.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuelisticv2seller.Callback.IRecyclerClickListener;
import com.example.fuelisticv2seller.Common.Common;
import com.example.fuelisticv2seller.Model.OrderModel;
import com.example.fuelisticv2seller.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    Context context;
    List<OrderModel> orderModelList;
    SimpleDateFormat simpleDateFormat;
    Calendar calendar;

    public OrderAdapter(Context context, List<OrderModel> orderModelList) {
        this.context = context;
        this.orderModelList = orderModelList;
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_order_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_order_number.setText(orderModelList.get(position).getKey());
        Common.setSpanStringColor("Delivery date: ", orderModelList.get(position).getDeliveryDate(),
                holder.txt_delivery_date, Color.parseColor("#333639"));
        Common.setSpanStringColor("Order status: ", Common.convertStatusToString(orderModelList.get(position).getOrderStatus()),
                holder.txt_order_status, Color.parseColor("#00579A"));
        Common.setSpanStringColor("Customer Name: ", orderModelList.get(position).getUserName(),
                holder.txt_cust_name, Color.parseColor("#00574B"));
        Common.setSpanStringColor("Quantity(in lt.): ", orderModelList.get(position).getQuantity(),
                holder.txt_order_amount, Color.parseColor("#4B647D"));

        holder.setRecyclerClickListener(new IRecyclerClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                showDialog(position);

            }
        });

    }

    private void showDialog(int position) {
        View layout_dialog = LayoutInflater.from(context).inflate(R.layout.layout_dialog_order_detail, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(layout_dialog);

        Button btn_ok = (Button) layout_dialog.findViewById(R.id.btn_ok);
        TextInputEditText order_details_order_num= (TextInputEditText) layout_dialog.findViewById(R.id.order_details_order_num);
        TextInputEditText order_details_cust_name= (TextInputEditText) layout_dialog.findViewById(R.id.order_details_cust_name);
        TextInputEditText order_details_order_status= (TextInputEditText) layout_dialog.findViewById(R.id.order_details_order_status);
        TextInputEditText order_details_cust_phone= (TextInputEditText) layout_dialog.findViewById(R.id.order_details_cust_phone);
        TextInputEditText order_details_transaction_id= (TextInputEditText) layout_dialog.findViewById(R.id.order_details_transaction_id);
        TextInputEditText order_details_delivery_address= (TextInputEditText) layout_dialog.findViewById(R.id.order_details_delivery_address);
        TextInputEditText order_details_delivery_charge= (TextInputEditText) layout_dialog.findViewById(R.id.order_details_delivery_charge);
        TextInputEditText order_details_total_order_amt= (TextInputEditText) layout_dialog.findViewById(R.id.order_details_total_order_amt);
        TextInputEditText order_details_order_date= (TextInputEditText) layout_dialog.findViewById(R.id.order_details_order_date);
        TextInputEditText order_details_order_quantity= (TextInputEditText) layout_dialog.findViewById(R.id.order_details_order_quantity);
        TextInputEditText order_details_delivery_mode= (TextInputEditText) layout_dialog.findViewById(R.id.order_details_delivery_mode);
        TextInputEditText order_details_delivery_cmt= (TextInputEditText) layout_dialog.findViewById(R.id.order_details_delivery_cmt);
        TextInputEditText order_details_delivery_date= (TextInputEditText) layout_dialog.findViewById(R.id.order_details_delivery_date);

        order_details_order_num.setText(orderModelList.get(position).getKey());
        order_details_cust_name.setText(orderModelList.get(position).getUserName());
        order_details_order_status.setText(Common.convertStatusToString(orderModelList.get(position).getOrderStatus()) );
        order_details_cust_phone.setText(orderModelList.get(position).getUserPhone());
        order_details_transaction_id.setText(orderModelList.get(position).getTransactionId());
        order_details_delivery_address.setText(orderModelList.get(position).getShippingAddress());
        order_details_delivery_charge.setText(Integer.toString(orderModelList.get(position).getDeliveryCharge()));

        calendar.setTimeInMillis(orderModelList.get(position).getOrderDate());
        Date date = new Date(orderModelList.get(position).getOrderDate());
        order_details_order_date.setText(new StringBuilder(Common.getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK)))
                .append(", ")
                .append(simpleDateFormat.format(date)));
        order_details_total_order_amt.setText(String.valueOf(orderModelList.get(position).getTotalPayment()));
        order_details_order_quantity.setText(orderModelList.get(position).getQuantity());
        order_details_delivery_mode.setText(orderModelList.get(position).getDeliveryMode());
        order_details_delivery_cmt.setText(orderModelList.get(position).getComment());
        order_details_delivery_date.setText(orderModelList.get(position).getDeliveryDate());


        //show dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // custom dialog
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);

        btn_ok.setOnClickListener(view -> dialog.dismiss());
    }

    @Override
    public int getItemCount() {
        return orderModelList.size();
    }

    public OrderModel getItemAtPosition(int pos) {
        return orderModelList.get(pos);
    }

    public void removeItem(int pos) {
        orderModelList.remove(pos);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.txt_order_amount)
        TextView txt_order_amount;

        @BindView(R.id.txt_order_status)
        TextView txt_order_status;

        @BindView(R.id.txt_delivery_date)
        TextView txt_delivery_date;

        @BindView(R.id.txt_order_number)
        TextView txt_order_number;

        @BindView(R.id.txt_cust_name)
        TextView txt_cust_name;


        private Unbinder unbinder;

        IRecyclerClickListener recyclerClickListener;

        public void setRecyclerClickListener(IRecyclerClickListener recyclerClickListener) {
            this.recyclerClickListener = recyclerClickListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recyclerClickListener.onItemClickListener(view, getAdapterPosition());
        }
    }
}
