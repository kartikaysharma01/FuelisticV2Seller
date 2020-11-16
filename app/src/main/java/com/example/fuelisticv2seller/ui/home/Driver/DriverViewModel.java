package com.example.fuelisticv2seller.ui.home.Driver;

import android.app.AlertDialog;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fuelisticv2seller.Callback.IDriverLoadCallbackListener;
import com.example.fuelisticv2seller.Callback.IOrderCallbackListener;
import com.example.fuelisticv2seller.Common.Common;
import com.example.fuelisticv2seller.Model.DriverModel;
import com.example.fuelisticv2seller.Model.OrderModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DriverViewModel extends ViewModel implements IDriverLoadCallbackListener {

    private MutableLiveData<List<DriverModel>> driverMutableList;
    private MutableLiveData<String> messageError = new MutableLiveData<>();
    private IDriverLoadCallbackListener driverLoadCallbackListener;

    public DriverViewModel() {
        driverLoadCallbackListener = this;
    }

    public MutableLiveData<List<DriverModel>> getDriverMutableList() {
        if(driverMutableList == null){
            driverMutableList = new MutableLiveData<>();
            loadDriver();
        }
        return driverMutableList;
    }

    private void loadDriver() {
        List<DriverModel> tempList = new ArrayList<>();
        DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference(Common.DRIVER_REF);

        driverRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot itemSnapShot:snapshot.getChildren())
                {
                    DriverModel driverModel = itemSnapShot.getValue(DriverModel.class);
                    driverModel.setKey(itemSnapShot.getKey());
                    tempList.add(driverModel);
                }
                driverLoadCallbackListener.onDriverLoadSuccess(tempList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                driverLoadCallbackListener.onDriverLoadFailed(error.getMessage());
            }
        });
    }

    public MutableLiveData<String> getMessageError() {
        return messageError;
    }

    @Override
    public void onDriverLoadSuccess(List<DriverModel> driverModelList) {
        if(driverMutableList != null)
            driverMutableList.setValue(driverModelList);
    }

    @Override
    public void onDriverLoadSuccess(int pos, OrderModel orderModel, List<DriverModel> driverModels, AlertDialog dialog, Button btn_update, Button btn_cancel, RadioButton rdi_cancelled, RadioButton rdi_completed, RadioButton rdi_confirmed, RadioButton rdi_delete, RadioButton rdi_restore_order) {
        // DO NOTHING
    }

    @Override
    public void onDriverLoadFailed(String message) {
        messageError.setValue(message);
    }
}