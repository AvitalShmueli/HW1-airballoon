package com.example.hw1_airballoons.Views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hw1_airballoons.Adapters.RecordAdapter;
import com.example.hw1_airballoons.Interfaces.Callback_highScoreClicked;
import com.example.hw1_airballoons.Models.RecordsList;
import com.example.hw1_airballoons.R;
import com.example.hw1_airballoons.Utilities.SharedPreferencesManager;
import com.google.gson.Gson;


public class RecordsListFragment extends Fragment {
    public static final String RECORDS_TABLE = "RECORDS";
    private RecyclerView list_LST_records;
    private Callback_highScoreClicked callbackHighScoreClicked;


    public RecordsListFragment() {
    }

    public void setCallbackHighScoreClicked(Callback_highScoreClicked callbackHighScoreClicked) {
        this.callbackHighScoreClicked = callbackHighScoreClicked;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_records_list, container, false);
        findViews(view);
        initViews(view);
        return view;
    }

    private void findViews(View view) {
        list_LST_records = view.findViewById(R.id.list_LST_records);
    }

    private void initViews(View view) {
        RecordsList fromSP = new Gson().fromJson(SharedPreferencesManager.getInstance().getString(RECORDS_TABLE, ""), RecordsList.class);
        RecordsList topTen = null;
        if(fromSP != null) {
            Log.d("RECORDS_TABLE from SP", fromSP.toString());
            topTen = new RecordsList().setRecordsArrayList(fromSP.getTopTenRecords());
        }
        RecordAdapter recordAdapter = new RecordAdapter(view.getContext(), topTen);
        recordAdapter.setCallbackHighScoreClicked(callbackHighScoreClicked);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        list_LST_records.setLayoutManager(linearLayoutManager);
        list_LST_records.setAdapter(recordAdapter);

    }

}