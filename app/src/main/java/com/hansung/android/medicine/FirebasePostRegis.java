package com.hansung.android.medicine;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class FirebasePostRegis {
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
        this.name = name;
        this.height = height;
        this.gender = gender;
        this.age = age;
        this.weight = weight;
        this.pill = pill;

    }

//    @Exclude
   /* public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);


        return result;
    }*/
}