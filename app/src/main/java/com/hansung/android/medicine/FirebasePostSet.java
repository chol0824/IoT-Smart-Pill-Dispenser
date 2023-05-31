package com.hansung.android.medicine;

public class FirebasePostSet {
    public String pillname;
    public String day;
    public String daytime;
    public String time;
    public String name;
    public String mg;
    public String count_pill;
    public String memo;


    public FirebasePostSet(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public FirebasePostSet( String count_pill, String day, String daytime,  String memo,String mg,
                            String name, String pillname, String time) {
        this.pillname = pillname;
        this.day = day;
        this.daytime = daytime;
        this.time = time;
        this.name = name;
        this.memo = memo;
        this.mg = mg;
        this.count_pill = count_pill;

    }
}
