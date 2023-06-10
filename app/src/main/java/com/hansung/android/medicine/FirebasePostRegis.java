package com.hansung.android.medicine;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class FirebasePostRegis { //사용자 등록 시 Firebase에 올릴 데이터 선언
    public String name;
    public String gender;
    public String age;
    public String height;
    public String weight;
    public String pill;


    public FirebasePostRegis(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public FirebasePostRegis(String age, String gender, String height, String name,
                             String weight, String pill) {
        this.name = name; //이름
        this.height = height; //키
        this.gender = gender; //성별
        this.age = age; //나이
        this.weight = weight; //몸무게
        this.pill = pill; //약

    }

//    @Exclude
   /* public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);


        return result;
    }*/
}