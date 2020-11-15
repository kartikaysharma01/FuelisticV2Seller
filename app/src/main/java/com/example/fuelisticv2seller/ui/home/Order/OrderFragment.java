package com.example.fuelisticv2seller.ui.home.Order;

import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fuelisticv2seller.Adapter.OrderAdapter;
import com.example.fuelisticv2seller.Common.BottomSheetOrderFragment;
import com.example.fuelisticv2seller.Common.Common;
import com.example.fuelisticv2seller.Common.MySwipeHelper;
import com.example.fuelisticv2seller.Model.EventBus.LoadOrderEvent;
import com.example.fuelisticv2seller.Model.FCMSendData;
import com.example.fuelisticv2seller.Model.OrderModel;
import com.example.fuelisticv2seller.Model.TokenModel;
import com.example.fuelisticv2seller.R;
import com.example.fuelisticv2seller.Remote.IFCMService;
import com.example.fuelisticv2seller.Remote.RetrofitFCMClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class OrderFragment extends Fragment {

    @BindView(R.id.recycler_orders)
    RecyclerView recycler_orders;

    @BindView(R.id.txt_order_filter)
    TextView txt_order_filter;

    Unbinder unbinder;
    LayoutAnimationController layoutAnimationController;
    OrderAdapter adapter;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private IFCMService ifcmService;

    private OrderViewModel orderViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        orderViewModel =
                ViewModelProviders.of(this).get(OrderViewModel.class);
        View root = inflater.inflate(R.layout.fragment_order, container, false);
        unbinder = ButterKnife.bind(this, root);
        initViews();

        orderViewModel.getMessageError().observe(getViewLifecycleOwner(), s -> {
            Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
        });

        orderViewModel.getOrderModelMutableLiveData().observe(getViewLifecycleOwner(), orderModels -> {
            if (orderModels != null) {
                adapter = new OrderAdapter(getContext(), orderModels);
                recycler_orders.setAdapter(adapter);
//                recycler_orders.setLayoutAnimation(layoutAnimationController);

                updateTextCounter();
            }
        });

        return root;
    }

    private void initViews() {

        ifcmService = RetrofitFCMClient.getInstance().create(IFCMService.class);

        setHasOptionsMenu(true);

        recycler_orders.setHasFixedSize(true);
        recycler_orders.setLayoutManager(new LinearLayoutManager(getContext()));

//        layoutAnimationController = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_item_from_left);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        MySwipeHelper mySwipeHelper = new MySwipeHelper(getContext(), recycler_orders, width / 6) {

            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> buf) {
                buf.add(new UnderlayButton(getContext(), "Directions", 30, 0, Color.parseColor("#9b0000"),
                        pos -> {


                        }));

                buf.add(new UnderlayButton(getContext(), "Call", 30, 0, Color.parseColor("#560027"),
                        pos -> {
                            Dexter.withActivity(getActivity())
                                    .withPermission(Manifest.permission.CALL_PHONE)
                                    .withListener(new PermissionListener() {
                                        @Override
                                        public void onPermissionGranted(PermissionGrantedResponse response) {

                                            OrderModel orderModel = adapter.getItemAtPosition(pos);
                                            Intent intent = new Intent();
                                            intent.setAction(Intent.ACTION_DIAL);
                                            intent.setData(Uri.parse(new StringBuilder("tel: ")
                                                    .append(orderModel.getUserPhone()).toString()));
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onPermissionDenied(PermissionDeniedResponse response) {
                                            Toast.makeText(getContext(), "You must accept" + response.getPermissionName(), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                                        }
                                    }).check();

                        }));

                buf.add(new UnderlayButton(getContext(), "Remove", 30, 0, Color.parseColor("#12005e"),
                        pos -> {

                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                                    .setTitle("Delete")
                                    .setMessage("This order will be permanently deleted. Do you really want to delete this order?")
                                    .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss()).setPositiveButton("DELETE", (dialogInterface, i) -> {

                                        OrderModel orderModel = adapter.getItemAtPosition(pos);
                                        FirebaseDatabase.getInstance()
                                                .getReference(Common.ORDER_REF)
                                                .child(orderModel.getKey())
                                                .removeValue()
                                                .addOnFailureListener(e -> Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show())
                                                .addOnSuccessListener(aVoid -> {
                                                    adapter.removeItem(pos);
                                                    adapter.notifyItemRemoved(pos);
                                                    updateTextCounter();
                                                    dialogInterface.dismiss();
                                                    Toast.makeText(getContext(), "Order had been deleted!", Toast.LENGTH_SHORT).show();
                                                });
                                    });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                            Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                            negativeButton.setTextColor(Color.GRAY);
                            Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                            positiveButton.setTextColor(Color.RED);

                        }));

                buf.add(new UnderlayButton(getContext(), "Edit", 30, 0, Color.parseColor("#336699"),
                        pos -> {
                            showEditDialog(adapter.getItemAtPosition(pos), pos);
                        }));

            }
        };
    }

    private void showEditDialog(OrderModel orderModel, int pos) {
        View layout_dialog;
        AlertDialog.Builder builder;
        if (orderModel.getOrderStatus() == 0) {                 // Placed
            layout_dialog = LayoutInflater.from(getContext())
                    .inflate(R.layout.layout_dialog_confirmed, null);
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Light_NoActionBar_Fullscreen)
                    .setView(layout_dialog);
        } else if (orderModel.getOrderStatus() == -1) {          // Cancelled
            layout_dialog = LayoutInflater.from(getContext())
                    .inflate(R.layout.layout_dialog_cancelled, null);
            builder = new AlertDialog.Builder(getContext())
                    .setView(layout_dialog);
        } else {                                               // Completed
            layout_dialog = LayoutInflater.from(getContext())
                    .inflate(R.layout.layout_dialog_completed, null);
            builder = new AlertDialog.Builder(getContext())
                    .setView(layout_dialog);
        }

        //view
        Button btn_update = (Button) layout_dialog.findViewById(R.id.btn_update);
        Button btn_cancel = (Button) layout_dialog.findViewById(R.id.btn_cancel);

        RadioButton rdi_confirmed = (RadioButton) layout_dialog.findViewById(R.id.rdi_confirmed);
        RadioButton rdi_cancelled = (RadioButton) layout_dialog.findViewById(R.id.rdi_cancelled);
        RadioButton rdi_delete = (RadioButton) layout_dialog.findViewById(R.id.rdi_delete);
        RadioButton rdi_restore_order = (RadioButton) layout_dialog.findViewById(R.id.rdi_restore_order);
        RadioButton rdi_completed = (RadioButton) layout_dialog.findViewById(R.id.rdi_completed);

        TextView txt_current_status = layout_dialog.findViewById(R.id.txt_current_status);

        // Set Data
        txt_current_status.setText(new StringBuilder("Current Status: ")
                .append(Common.convertStatusToString(orderModel.getOrderStatus())));

        // Create Dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Custom Dialog
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);

        btn_cancel.setOnClickListener(view -> dialog.dismiss());
        btn_update.setOnClickListener(view -> {
            dialog.dismiss();
            if (rdi_cancelled != null && rdi_cancelled.isChecked())
                updateOrder(pos, orderModel, -1);
            else if (rdi_confirmed != null && rdi_confirmed.isChecked())
                updateOrder(pos, orderModel, 1);
            else if (rdi_completed != null && rdi_completed.isChecked())
                updateOrder(pos, orderModel, 2);
            else if (rdi_restore_order != null && rdi_restore_order.isChecked())
                updateOrder(pos, orderModel, 0);
            else if (rdi_delete != null && rdi_delete.isChecked())
                deleteOrder(pos, orderModel);
        });
    }

    private void deleteOrder(int pos, OrderModel orderModel) {
        if (!TextUtils.isEmpty(orderModel.getKey())) {


            FirebaseDatabase.getInstance()
                    .getReference(Common.ORDER_REF)
                    .child(orderModel.getKey())
                    .removeValue()
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            adapter.removeItem(pos);
                            adapter.notifyItemRemoved(pos);
                            updateTextCounter();
                            Toast.makeText(getContext(), "Order Deleted Successfully!", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getContext(), "Order Number must not be null or empty!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateOrder(int pos, OrderModel orderModel, int status) {
        if (!TextUtils.isEmpty(orderModel.getKey())) {
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("orderStatus", status);

            FirebaseDatabase.getInstance()
                    .getReference(Common.ORDER_REF)
                    .child(orderModel.getKey())
                    .updateChildren(updateData)
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show())
                    .addOnSuccessListener(aVoid -> {

                        android.app.AlertDialog dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();
                        dialog.show();

                        FirebaseDatabase.getInstance()
                                .getReference(Common.TOKEN_REF)
                                .child(orderModel.getUserPhone())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists())
                                        {
                                            TokenModel tokenModel = snapshot.getValue(TokenModel.class);
                                            Map<String,String> notiData = new HashMap<>();
                                            notiData.put(Common.NOTI_TITLE, "Your Order status has changed!!");
                                            notiData.put(Common.NOTI_CONTENT, new StringBuilder("Your Order ")
                                            .append(orderModel.getKey())
                                            .append("just got ")
                                            .append(Common.convertStatusToString(status)).toString());

                                            FCMSendData sendData = new FCMSendData(tokenModel.getToken() , notiData);

                                            compositeDisposable.add(ifcmService.sendNotification(sendData)
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(fcmResponse -> {
                                                        dialog.dismiss();
                                                        if(fcmResponse.getSuccess()==1)
                                                        {
                                                            Toast.makeText(getContext(), "Order Updated Successfully!", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else {
                                                            Toast.makeText(getContext(), "Order Updated Successfully but failed to send notification!", Toast.LENGTH_SHORT).show();
                                                        }

                                                    }, throwable -> {
                                                        dialog.dismiss();
                                                        Toast.makeText(getContext(), ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }));
                                        }
                                        else
                                        {
                                            dialog.dismiss();
                                            Toast.makeText(getContext(), "Token not found!", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        dialog.dismiss();
                                        Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                        adapter.removeItem(pos);
                        adapter.notifyItemRemoved(pos);
                        updateTextCounter();

                    });
        } else {
            Toast.makeText(getContext(), "Order Number must not be null or empty!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTextCounter() {
        txt_order_filter.setText(new StringBuilder("Orders (")
                .append(adapter.getItemCount())
                .append(")"));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.order_filter_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_filter) {
            BottomSheetOrderFragment bottomSheetOrderFragment = BottomSheetOrderFragment.getInstance();
            bottomSheetOrderFragment.show(getActivity().getSupportFragmentManager(), "OrderFilter");
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        if (EventBus.getDefault().hasSubscriberForEvent(LoadOrderEvent.class))
            EventBus.getDefault().removeStickyEvent(LoadOrderEvent.class);
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        compositeDisposable.clear();
        super.onStop();

    }

    @Override
    public void onDestroy() {
//        EventBus.getDefault().postSticky(new ChangeMenuClick(true));
        super.onDestroy();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onLoadOrderEvent(LoadOrderEvent event) {
        orderViewModel.loadOrderByStatus(event.getStatus());
    }

}