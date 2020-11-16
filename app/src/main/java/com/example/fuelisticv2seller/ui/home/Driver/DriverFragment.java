package com.example.fuelisticv2seller.ui.home.Driver;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fuelisticv2seller.Adapter.MyDriverAdapter;
import com.example.fuelisticv2seller.Common.Common;
import com.example.fuelisticv2seller.Model.DriverModel;
import com.example.fuelisticv2seller.Model.EventBus.UpdateDriverEvent;
import com.example.fuelisticv2seller.R;
import com.example.fuelisticv2seller.ui.home.Order.OrderViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class DriverFragment extends Fragment {

    private DriverViewModel mViewModel;

    private Unbinder unbinder;

    @BindView(R.id.recycler_driver)
    RecyclerView recycler_driver;

    AlertDialog dialog;
    MyDriverAdapter adapter;
    List<DriverModel> driverModelList;

    public static DriverFragment newInstance() {
        return new DriverFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.fragment_driver, container, false);
        mViewModel = ViewModelProviders.of(this).get(DriverViewModel.class);
        unbinder = ButterKnife.bind(this, itemView);
        initViews();
        mViewModel.getMessageError().observe(getViewLifecycleOwner(), s -> {
            Toast.makeText(getContext(), "" + s, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        mViewModel.getDriverMutableList().observe(getViewLifecycleOwner(), drivers -> {
            dialog.dismiss();
            adapter = new MyDriverAdapter(getContext(), drivers);
            recycler_driver.setAdapter(adapter);
        });
        return itemView;
    }

    private void initViews() {

        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler_driver.setLayoutManager(layoutManager);
        recycler_driver.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));
//        recycler_driver.setAdapter(adapter);

    }



    @Override
    public void onStart() {
        super.onStart();

        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

    }

    @Override
    public void onStop() {

        if(EventBus.getDefault().hasSubscriberForEvent(UpdateDriverEvent.class))
            EventBus.getDefault().removeStickyEvent(UpdateDriverEvent.class);
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);

        super.onStop();

    }

    @Override
    public void onDestroy() {
//        EventBus.getDefault().postSticky(new ChangeMenuClick());
        super.onDestroy();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onUpdateDriverActive(UpdateDriverEvent event)
    {
        Map<String,Object> updateData = new HashMap<>();
        updateData.put("active", event.isActive());
        FirebaseDatabase.getInstance()
                .getReference(Common.DRIVER_REF)
                .child(event.getDriverModel().getKey())
                .updateChildren(updateData)
                .addOnFailureListener(e -> Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show()).addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Update Driver status to " +event.isActive(), Toast.LENGTH_SHORT).show());


    }

}