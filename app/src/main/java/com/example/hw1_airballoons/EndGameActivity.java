package com.example.hw1_airballoons;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.hw1_airballoons.Models.Record;
import com.example.hw1_airballoons.Models.RecordsList;
import com.example.hw1_airballoons.Utilities.SharedPreferencesManager;
import com.example.hw1_airballoons.Utilities.SignalManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import java.util.Objects;


public class EndGameActivity extends AppCompatActivity {
    public static final String KEY_SCORE = "KEY_SCORE";
    private static final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private ShapeableImageView main_IMG_background;
    private String name = "";
    private MaterialTextView dialog_LBL_score;
    private TextInputEditText dialog_TXT_name;
    private MaterialButton end_BTN_restart;
    Location currentLocation = null;
    FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getDeviceLocation();

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_save_record, null);
        findViews(dialogView);

        Glide
                .with(this)
                .load(R.drawable.sky)
                .centerCrop()
                .placeholder(R.drawable.background)
                .into(main_IMG_background);

        end_BTN_restart.setOnClickListener(view -> backToOpenningScreen());

        Intent previousScreen = getIntent();
        int score = previousScreen.getIntExtra(KEY_SCORE, 0);
        String dialogTitle = "You have scored " + score + " points";
        dialog_LBL_score.setText(dialogTitle);
        AlertDialog alertDialog = new MaterialAlertDialogBuilder(this).setView(dialogView)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        name = Objects.requireNonNull(dialog_TXT_name.getText()).toString();
                        RecordsList fromSP = new Gson().fromJson(SharedPreferencesManager.getInstance().getString(RecordsTableActivity.RECORDS_TABLE, ""), RecordsList.class);
                        if (fromSP == null) {
                            fromSP = new RecordsList();
                            fromSP.setListName(RecordsTableActivity.RECORDS_TABLE);
                        }
                        Log.d("RECORDS_TABLE from SP", fromSP.toString());
                        double lan, lon;
                        if (currentLocation != null) {
                            lan = currentLocation.getLatitude();
                            lon = currentLocation.getLongitude();
                        } else {
                            lan = App.DEFAULT_LAN;
                            lon = App.DEFAULT_LON;
                        }
                        fromSP.addRecord(new Record().
                                setName(name).
                                setScore(score).
                                setLatitude(lan).
                                setLongitude(lon));

                        Gson gson = new Gson();
                        String recordsListAsJson = gson.toJson(fromSP);
                        SharedPreferencesManager.getInstance().putString(RecordsTableActivity.RECORDS_TABLE, recordsListAsJson);
                        Log.d("RECORDS_TABLE from SP", fromSP.toString());
                        dialog.dismiss();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                }).create();
        alertDialog.show();
    }

    private void backToOpenningScreen() {
        Intent openingScreenActivity = new Intent(this, OpeningScreenActivity.class);
        startActivity(openingScreenActivity);
        finish();
    }

    private void findViews(View view) {
        main_IMG_background = findViewById(R.id.main_IMG_background);
        dialog_LBL_score = view.findViewById(R.id.dialog_LBL_score);
        dialog_TXT_name = view.findViewById(R.id.dialog_TXT_name);
        end_BTN_restart = findViewById(R.id.end_BTN_restart);
    }

    private void getDeviceLocation() {
        Log.d("getDeviceLocation", "getting the current devices current location");
        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};
        boolean mLocationPermissionsGranted = false;
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
                return;
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                }
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        SignalManager.getInstance().toast("Location permissions are missing");
                        return;
                    }
                }
                getDeviceLocation();
            }
        }
    }

}