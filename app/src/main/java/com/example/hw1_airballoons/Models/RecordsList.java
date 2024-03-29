package com.example.hw1_airballoons.Models;

import java.util.ArrayList;
import java.util.Comparator;

public class RecordsList {
    private String listName = "";
    private ArrayList<Record> recordsArrayList = new ArrayList<>();

    public RecordsList() {
    }

    public String getListName() {
        return listName;
    }

    public RecordsList setListName(String listName) {
        this.listName = listName;
        return this;
    }

    public ArrayList<Record> getRecordsArrayList() {
        return recordsArrayList;
    }

    public ArrayList<Record> getTopTenRecords() {
        ArrayList<Record> topTen = new ArrayList<>();
        for(int i = 0; i < 10 && i < recordsArrayList.size(); i++) {
            topTen.add(recordsArrayList.get(i));
        }
        return topTen;
    }

    public RecordsList setRecordsArrayList(ArrayList<Record> recordsArrayList) {
        this.recordsArrayList = recordsArrayList;
        return this;
    }

    public RecordsList setRecordArrayList(ArrayList<Record> recordsArrayList) {
        this.recordsArrayList = recordsArrayList;
        return this;
    }

    public RecordsList addRecord(Record record) {
        this.recordsArrayList.add(record);
        this.recordsArrayList.sort(new Comparator<Record>() {
            @Override
            public int compare(Record o1, Record o2) {
                if(o1.getScore() < o2.getScore())
                    return 1;
                else if(o1.getScore() == o2.getScore())
                    return 0;
                return -1;
            }});
        return this;
    }

    @Override
    public String toString() {
        return "RecordsList{" +
                "appName='" + listName + '\'' +
                ", recordsArrayList=" + recordsArrayList +
                '}';
    }

    public int getSize(){
        return recordsArrayList.size();
    }

    public Record get(int position){
        return recordsArrayList.get(position);
    }

}
