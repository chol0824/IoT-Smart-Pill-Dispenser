package com.hansung.android.medicine;

public class we { public String name;
    public String gender;
    public String age;
    public String height;
    public String weight;
    public String pill;


    public we(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public we(String weight) {
        //  this.name = name;
        //this.gender = gender;


        this.weight = weight;

    }

}
