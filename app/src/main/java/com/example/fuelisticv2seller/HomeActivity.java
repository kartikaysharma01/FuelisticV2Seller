package com.example.fuelisticv2seller;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fuelisticv2seller.Common.Common;
import com.example.fuelisticv2seller.LoginSignUp.AppStartUpScreen;
import com.example.fuelisticv2seller.Model.FCMResponse;
import com.example.fuelisticv2seller.Model.FCMSendData;
import com.example.fuelisticv2seller.Remote.IFCMService;
import com.example.fuelisticv2seller.Remote.RetrofitFCMClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int PICK_IMAGE_REQUEST = 1212;
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private NavController navController;
    private int menuClick = -1;

    private ImageView img_upload;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private IFCMService ifcmService;
    private Uri imgUri = null;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ifcmService = RetrofitFCMClient.getInstance().create(IFCMService.class);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        subscribeToTopic(Common.createTopicOrder());
        updateToken();

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_order, R.id.nav_driver, R.id.nav_myProfile, R.id.nav_contactUs)
                .setDrawerLayout(drawer)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();

        //hello user at the header vIEW
        View headerView = navigationView.getHeaderView(0);
        TextView txt_hello = headerView.findViewById(R.id.txt_hello);
        Common.setSpanString("Hello,\n", Common.currentSellerUser.getFullName(), txt_hello);

        menuClick = R.id.nav_order;      //Default

        checkIsOpenFromActivity();


    }

    private void checkIsOpenFromActivity() {
        boolean isOpenFromNewOrder = getIntent().getBooleanExtra(Common.IS_OPEN_ACTIVITY_NEW_ORDER, false);
        if (isOpenFromNewOrder) {
            navController.popBackStack();
            navController.navigate(R.id.nav_order);
            menuClick = R.id.nav_order;
        }
    }

    private void updateToken() {
        FirebaseInstanceId.getInstance()
                .getInstanceId()
                .addOnFailureListener(e -> Toast.makeText(HomeActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show())
                .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        Common.updateToken(HomeActivity.this, instanceIdResult.getToken(),
                                true,
                                false);
                    }
                });
    }

    private void subscribeToTopic(String topicOrder) {
        FirebaseMessaging.getInstance()
                .subscribeToTopic(topicOrder)
                .addOnFailureListener(e -> Toast.makeText(HomeActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show()).addOnCompleteListener(task -> {
            if (!task.isSuccessful())
                Toast.makeText(this, "Failed: " + task.isSuccessful(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        drawer.closeDrawers();

        switch (item.getItemId()) {

            case R.id.nav_order:
                if (item.getItemId() != menuClick) {
                    navController.popBackStack();
                    navController.navigate(R.id.nav_order);

                }
                break;

            case R.id.nav_myProfile:
                if (item.getItemId() != menuClick) {
                    navController.popBackStack();
                    navController.navigate(R.id.nav_myProfile);
                }
                break;

            case R.id.nav_driver:
                if (item.getItemId() != menuClick) {
                    navController.popBackStack();
                    navController.navigate(R.id.nav_driver);
                }
                break;

            case R.id.nav_contactUs:
                if (item.getItemId() != menuClick) {
                    navController.popBackStack();
                    navController.navigate(R.id.nav_contactUs);
                }
                break;

            case R.id.nav_logout:
                logOut();
                break;

            case R.id.nav_send_news:
                showNewsDialog();
                break;

            default:
                menuClick = -1;
                break;
        }
        menuClick = item.getItemId();
        return true;
    }

    private void showNewsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("News System");
        builder.setMessage("Send notification to all clients");
        View itemView = LayoutInflater.from(this).inflate(R.layout.layout_news_system, null);

        ///Views
        EditText edt_title = (EditText) itemView.findViewById(R.id.edt_title);
        EditText edt_content = (EditText) itemView.findViewById(R.id.edt_content);
        EditText edt_link = (EditText) itemView.findViewById(R.id.edt_link);
        img_upload = (ImageView) itemView.findViewById(R.id.img_upload);
        RadioButton rbd_none = (RadioButton) itemView.findViewById(R.id.rdb_none);
        RadioButton rdb_link = (RadioButton) itemView.findViewById(R.id.rdb_link);
        RadioButton rdb_image = (RadioButton) itemView.findViewById(R.id.rdb_image);

        //Event
        rbd_none.setOnClickListener(view -> {
            edt_link.setVisibility(View.GONE);
            img_upload.setVisibility(View.GONE);
        });

        rdb_link.setOnClickListener(view -> {
            edt_link.setVisibility(View.VISIBLE);
            img_upload.setVisibility(View.GONE);
        });

        rdb_image.setOnClickListener(view -> {
            edt_link.setVisibility(View.GONE);
            img_upload.setVisibility(View.VISIBLE);
        });

        img_upload.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

        });

        builder.setView(itemView);
        builder.setNegativeButton("CANCEL", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });
        builder.setPositiveButton("SEND", (dialogInterface, i) -> {
            if (rbd_none.isChecked()) {
                sendNews(edt_title.getText().toString(), edt_content.getText().toString());
            } else if (rdb_link.isChecked()) {
                sendNews(edt_title.getText().toString(), edt_content.getText().toString(), edt_link.getText().toString());
            } else if (rdb_image.isChecked()) {

                if (imgUri != null) {
                    AlertDialog dialog = new AlertDialog.Builder(this).setMessage("Uploading...").create();
                    dialog.show();

                    String file_name = UUID.randomUUID().toString();  //UUID.randomUUID().toString();
                    StorageReference newsImages = storageReference.child("news/" + file_name);
                    newsImages.putFile(imgUri)
                            .addOnFailureListener(e -> {
                                dialog.dismiss();
                                Toast.makeText(HomeActivity.this, "well" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }).addOnSuccessListener(taskSnapshot -> {
                                dialog.dismiss();
                                newsImages.getDownloadUrl().addOnSuccessListener(uri -> { sendNews(edt_title.getText().toString(), edt_content.getText().toString(), uri.toString());
                        });

                            }).addOnProgressListener(snapshot -> {
                                double progress = Math.round((100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount()));
                                dialog.setMessage(new StringBuilder("Uploading: ").append(progress).append("%"));

                            });
                }

            }

        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void sendNews(String title, String content, String url) {
        Map<String, String> notificationData = new HashMap<String, String>();
        notificationData.put(Common.NOTI_TITLE, title);
        notificationData.put(Common.NOTI_CONTENT, content);
        notificationData.put(Common.IS_SEND_IMAGE, "true");
        notificationData.put(Common.IMAGE_URL, url);

        FCMSendData fcmSendData = new FCMSendData(Common.getNewsTopic(), notificationData);

        AlertDialog dialog = new AlertDialog.Builder(this).setMessage("Waiting...").create();
        dialog.show();

        compositeDisposable.add(ifcmService.sendNotification(fcmSendData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fcmResponse -> {
                    dialog.dismiss();
                    if (fcmResponse.getMessage_id() != 0)
                        Toast.makeText(this, "News has been sent", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(this, "News could not be sent", Toast.LENGTH_SHORT).show();

                }));
    }

    private void sendNews(String title, String content) {
        Map<String, String> notificationData = new HashMap<String, String>();
        notificationData.put(Common.NOTI_TITLE, title);
        notificationData.put(Common.NOTI_CONTENT, content);
        notificationData.put(Common.IS_SEND_IMAGE, "false");

        FCMSendData fcmSendData = new FCMSendData(Common.getNewsTopic(), notificationData);

        AlertDialog dialog = new AlertDialog.Builder(this).setMessage("Waiting...").create();
        dialog.show();

        compositeDisposable.add(ifcmService.sendNotification(fcmSendData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fcmResponse -> {
                    dialog.dismiss();
                    if (fcmResponse.getMessage_id() != 0)
                        Toast.makeText(this, "News has been sent", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(this, "News could not be sent", Toast.LENGTH_SHORT).show();

                }));


    }

    private void logOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("LogOut")
                .setMessage("Are you sure you want to log out?")
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Common.currentSellerUser = null;
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(HomeActivity.this, AppStartUpScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                imgUri = data.getData();
                img_upload.setImageURI(imgUri);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}