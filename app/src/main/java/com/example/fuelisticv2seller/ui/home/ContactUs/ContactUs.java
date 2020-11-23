package com.example.fuelisticv2seller.ui.home.ContactUs;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.fuelisticv2seller.Common.Common;
import com.example.fuelisticv2seller.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ContactUs extends Fragment {

    private ContactUsViewModel mViewModel;
    private Context context;
    private Unbinder unbinder;

    @BindView(R.id.btn_click_here)
    Button btn_click_here;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.contact_us_fragment, container, false);

        unbinder = ButterKnife.bind(this, root);

        return root;
    }

    @OnClick(R.id.btn_click_here)
    public void showDialogCallEmail(){
        View layout_dialog = LayoutInflater.from(getContext()).inflate(R.layout.layout_contact_us, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(layout_dialog);

        Button btn_call = (Button) layout_dialog.findViewById(R.id.btn_call);
        Button btn_email = (Button) layout_dialog.findViewById(R.id.btn_email);

        //show dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // custom dialog
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        btn_call.setOnClickListener(view -> Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.CALL_PHONE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse(new StringBuilder("tel: +911111111111").toString()));
                        startActivity(intent);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(getContext(), "You must accept" + response.getPermissionName(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check());

        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] addresses = {"fuelistic.helpdesk@gmail.com"};
                String[] cc = {"customer.relation@fuelistic.com", "hr@fuelistic.com"};

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, addresses);
                intent.putExtra(Intent.EXTRA_CC, cc);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Help me out!!");
                intent.putExtra(Intent.EXTRA_TEXT, "Dear Fuelistic,\n\n\n\n\nWarm Regards,\n"+ Common.currentSellerUser.getFullName());
                if (intent.resolveActivity(getActivity().getPackageManager()) != null ) {
                    startActivity(intent);
                }
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause()   {
        super.onPause();
    }



}