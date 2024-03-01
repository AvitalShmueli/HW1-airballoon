package com.example.hw1_airballoons;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.hw1_airballoons.Models.Record;
import com.example.hw1_airballoons.Models.RecordsList;
import com.example.hw1_airballoons.Utilities.SharedPreferencesManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import java.util.Objects;


public class EndGameActivity extends AppCompatActivity {
    public static final String KEY_SCORE = "KEY_SCORE";
    private ShapeableImageView main_IMG_background;
    private String name = "";
    private ShapeableImageView dialog_IMG_header;
    private MaterialTextView dialog_LBL_score;
    private TextInputEditText dialog_TXT_name;
    private MaterialButton end_BTN_restart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

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
                        Log.d("name from dialog = ", name);
                        RecordsList fromSP = new Gson().fromJson(SharedPreferencesManager.getInstance().getString(RecordsTable.RECORDS_TABLE, ""), RecordsList.class);
                        if (fromSP == null) {
                            fromSP = new RecordsList();
                            fromSP.setListName(RecordsTable.RECORDS_TABLE);
                        }
                        Log.d("RECORDS_TABLE from SP", fromSP.toString());

                fromSP.addRecord(new Record().
                        setName(name).
                        setScore(score).
                        setLatitude(34).
                        setLongitude(32));

                Gson gson = new Gson();
                String recordsListAsJson = gson.toJson(fromSP);
                SharedPreferencesManager.getInstance().putString(RecordsTable.RECORDS_TABLE,recordsListAsJson);
                Log.d("JSON", recordsListAsJson);
                Log.d("RECORDS_TABLE from SP", fromSP.toString());

                dialog.dismiss();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                }) .create();
        alertDialog.show();
    }

    private void backToOpenningScreen() {
        Intent openingScreenActivity = new Intent(this, OpeningScreenActivity.class);
        startActivity(openingScreenActivity);
        finish();
    }

    private void findViews(View view) {

        main_IMG_background = findViewById(R.id.main_IMG_background);
        //dialog_IMG_header = view.findViewById(R.id.dialog_IMG_header);
        dialog_LBL_score = view.findViewById(R.id.dialog_LBL_score);
        dialog_TXT_name = view.findViewById(R.id.dialog_TXT_name);
        end_BTN_restart = findViewById(R.id.end_BTN_restart);

    }


}