package com.example.fuelisticv2seller.ui.home.Order;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fuelisticv2seller.Callback.IOrderCallbackListener;
import com.example.fuelisticv2seller.Common.Common;
import com.example.fuelisticv2seller.Model.OrderModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class OrderViewModel extends ViewModel implements IOrderCallbackListener {
    private MutableLiveData<List<OrderModel>> orderModelMutableLiveData;
    private MutableLiveData<String> messageError;

    private IOrderCallbackListener listener;

    public OrderViewModel(){
        orderModelMutableLiveData= new MutableLiveData<>();
        messageError = new MutableLiveData<>();
        listener = this;
    }

    public MutableLiveData<List<OrderModel>> getOrderModelMutableLiveData() {
        loadOrderByStatus(0);
        return orderModelMutableLiveData;
    }

    public void loadOrderByStatus(int status) {
        List<OrderModel> tempList = new ArrayList<>();
        Query orderRef = FirebaseDatabase.getInstance().getReference("Orders")
                .orderByChild("orderStatus")
                .equalTo(status);
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot itemSnapShot:snapshot.getChildren())
                {
                    OrderModel orderModel = itemSnapShot.getValue(OrderModel.class);
                    orderModel.setKey(itemSnapShot.getKey());
                    tempList.add(orderModel);
                }
                listener.onLoadOrderSuccess(tempList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onLoadOrderFailed(error.getMessage());
            }
        });
    }

    public MutableLiveData<String> getMessageError(){
        return messageError;
    }

    @Override
    public void onLoadOrderSuccess(List<OrderModel> orderModelList) {
        if(orderModelList.size()>0)
        {
            Collections.sort(orderModelList, (orderModel , t1) -> {
                if(orderModel.getOrderDate()< t1.getOrderDate())
                    return -1;
                return orderModel.getOrderDate() == t1.getOrderDate() ? 0:1;

            });
        }
        orderModelMutableLiveData.setValue(orderModelList);
    }

    @Override
    public void onLoadOrderFailed(String message) {
        messageError.setValue(message);
    }

//    public void setMutableLiveDataOrderList(List<OrderModel> orderList) {
//        mutableLiveDataOrderList.setValue(orderList);
    
}