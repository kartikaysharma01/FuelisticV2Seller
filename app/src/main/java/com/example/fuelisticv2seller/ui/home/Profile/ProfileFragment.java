package com.example.fuelisticv2seller.ui.home.Profile;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fuelisticv2seller.Common.Common;
import com.example.fuelisticv2seller.R;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment
{
    public TextView userName;
    //private ProfileViewModel mViewModel;

    private RecyclerView mRecycleview;
    private List<ItemAdapter> mList = new ArrayList<>();
    private ProfileListAdapter mAdapter;
    private Context ctx;

    public static ProfileFragment newInstance()
    {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        userName = view.findViewById(R.id.profile_userName);
        userName.setText(Common.currentSellerUser.getUsername());

        ItemAdapter itemAdapter = new ItemAdapter();
        itemAdapter.setInputDesc("Full Name");
        itemAdapter.setInputValue(Common.currentSellerUser.getFullName());
        itemAdapter.setImg(R.drawable.ic_baseline_perm_identity_24);
        mList.add(itemAdapter);

        itemAdapter = new ItemAdapter();
        itemAdapter.setInputDesc("Aadhaar No.");
        itemAdapter.setInputValue(Common.currentSellerUser.getAadhaar());
        itemAdapter.setImg(R.drawable.ic_baseline_home_work_24);
        mList.add(itemAdapter);


        itemAdapter = new ItemAdapter();
        itemAdapter.setInputDesc("Email");
        itemAdapter.setInputValue(Common.currentSellerUser.getEmail());
        itemAdapter.setImg(R.drawable.ic_baseline_email_24);
        mList.add(itemAdapter);

        itemAdapter = new ItemAdapter();
        itemAdapter.setInputDesc("Phone Number");
        itemAdapter.setInputValue(Common.currentSellerUser.getPhoneNo());
        itemAdapter.setImg(R.drawable.ic_baseline_phone_24);
        mList.add(itemAdapter);

        itemAdapter = new ItemAdapter();
        itemAdapter.setInputDesc("Status");
        boolean status = Common.currentSellerUser.isActive();
        if(status)
            itemAdapter.setInputValue("Active");
        else
            itemAdapter.setInputValue("Not Active");
        itemAdapter.setImg(R.drawable.ic_baseline_notification_important_24);
        mList.add(itemAdapter);

        ctx = view.getContext();
        mRecycleview = view.findViewById(R.id.profileRecycler);
        mAdapter = new ProfileListAdapter(mList, ctx);
        mRecycleview.setAdapter(mAdapter);
        mRecycleview.setLayoutManager(new LinearLayoutManager(ctx));
        //mAdapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

}