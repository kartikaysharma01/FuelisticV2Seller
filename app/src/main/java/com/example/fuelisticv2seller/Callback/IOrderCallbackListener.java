package com.example.fuelisticv2seller.Callback;

import com.example.fuelisticv2seller.Model.OrderModel;

import java.util.List;

public interface IOrderCallbackListener {
    void onLoadOrderSuccess(List<OrderModel> orderModelList);
    void onLoadOrderFailed(String message);
}
