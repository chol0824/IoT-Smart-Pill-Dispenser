package com.hansung.android.medicine;

public class FirebaseTimeSet {
    public String pillname;
    public String day;
    public String daytime;
    public String time;
    public String name;


    public FirebaseTimeSet(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public FirebaseTimeSet( String name, String pillname, String day, String daytime, String time) {
        this.name = name;
        this.pillname = pillname;
        this.day = day;
        this.daytime = daytime;
        this.time = time;

    }
}
