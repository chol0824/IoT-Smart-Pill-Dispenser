package com.hansung.android.medicine;

public class FirebaseTimeSet { //약 복용 시간 설정 시 Firebase에 올릴 데이터 선언
    public String pillname; //약
    public String day; // 복용일
    public String daytime; //복용시간대
    public String time; //식전 식후
    public String name; // 이름


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
