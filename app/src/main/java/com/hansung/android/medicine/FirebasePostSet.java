package com.hansung.android.medicine;

public class FirebasePostSet { //약 복용 정보 등록 시 Firebase에 올릴 데이터 선언
    public String pillname; //약
    public String day; //복용날 ( 월~일)
    public String daytime; //복용 시간대 ( 아침, 점심 저녁 )
    public String time; //시간 (식전, 식후 )
    public String name; //이름
    public String mg; //용량
    public String count_pill; //잔여 약 갯수
    public String memo; // 메모


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
