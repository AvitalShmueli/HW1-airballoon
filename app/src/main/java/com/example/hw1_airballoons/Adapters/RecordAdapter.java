package com.example.hw1_airballoons.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw1_airballoons.Interfaces.Callback_highScoreClicked;
import com.example.hw1_airballoons.Models.Record;
import com.example.hw1_airballoons.Models.RecordsList;
import com.example.hw1_airballoons.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {
    private Context context;
    private RecordsList recordArrayList;
    private Callback_highScoreClicked callbackHighScoreClicked;


    public RecordAdapter(Context context, RecordsList recordArrayList) {
        this.context = context;
        this.recordArrayList = recordArrayList;
    }

    public RecordAdapter setCallbackHighScoreClicked(Callback_highScoreClicked callbackHighScoreClicked) {
        this.callbackHighScoreClicked = callbackHighScoreClicked;
        return this;
    }

    @NonNull
    @Override
    public RecordAdapter.RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_record_item,parent,false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordAdapter.RecordViewHolder holder, int position) {
        Record record = getItem(position);
        holder.record_LBL_name.setText(record.getName());
        holder.record_LBL_score.setText(String.valueOf(record.getScore()));
        holder.record_LBL_latitude.setText(String.valueOf(record.getLatitude()));
        holder.record_LBL_longitude.setText(String.valueOf(record.getLongitude()));
    }

    @Override
    public int getItemCount() {
        return recordArrayList == null ? 0 :  recordArrayList.getSize();
    }

    private Record getItem(int position){
        return recordArrayList.get(position);
    }

    public class RecordViewHolder extends RecyclerView.ViewHolder {
        private MaterialTextView record_LBL_name;
        private MaterialTextView record_LBL_score;
        private MaterialTextView record_LBL_latitude;
        private MaterialTextView record_LBL_longitude;


        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            record_LBL_name = itemView.findViewById(R.id.record_LBL_name);
            record_LBL_score = itemView.findViewById(R.id.record_LBL_score);
            record_LBL_latitude = itemView.findViewById(R.id.record_LBL_latitude);
            record_LBL_longitude = itemView.findViewById(R.id.record_LBL_longitude);

        }
    }
}
