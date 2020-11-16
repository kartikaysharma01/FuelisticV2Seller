package com.example.fuelisticv2seller.Callback;

import android.app.AlertDialog;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.fuelisticv2seller.Model.DriverModel;
import com.example.fuelisticv2seller.Model.OrderModel;

import java.util.List;

public interface IDriverLoadCallbackListener {
    void onDriverLoadSuccess(List<DriverModel> driverModelList);
    void onDriverLoadSuccess(int pos, OrderModel orderModel, List<DriverModel> driverModels ,
                             AlertDialog dialog,
                             Button btn_update, Button btn_cancel,
                             RadioButton rdi_cancelled, RadioButton rdi_completed,
                             RadioButton rdi_confirmed, RadioButton rdi_delete, RadioButton rdi_restore_order);
    void onDriverLoadFailed(String message);
}
