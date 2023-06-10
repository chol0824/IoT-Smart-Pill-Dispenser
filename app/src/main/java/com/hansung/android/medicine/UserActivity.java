package com.hansung.android.medicine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity { //사용자 정보 화면

    ListView listView;
    List fileList = new ArrayList<>();
    ArrayAdapter<String> adapter;


    TextView user_name; // 이름
    TextView user_height; // 키
    TextView user_weight; // 몸무게
    TextView user_birth; // 생년월일
    TextView user_gender; // 성별
    TextView user;
    String motor;


    static boolean calledAlready = false;

    Button upload_btn;
    TextView btn_result;

    int count = 0;
    String []n;
    String gender;
    String pill;

    String day;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    FirebaseDatabase database1 = FirebaseDatabase.getInstance();
    DatabaseReference databaseRef1 = database1.getReference("Druglist"); //약 리스트 불러오기

    FirebaseDatabase database2 = FirebaseDatabase.getInstance();
    DatabaseReference databaseRef2 = database2.getReference("pill");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        user = (TextView) findViewById(R.id.user);

        user_name = (TextView) findViewById(R.id.username);
        user_birth = (TextView)findViewById(R.id.user_birth);
        user_height = (TextView) findViewById(R.id.user_height);
        user_weight = (TextView) findViewById(R.id.user_weight);
        user_gender = (TextView) findViewById(R.id.gender);


        listView = (ListView) findViewById(R.id.Pill_list);


        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fileList){


            @Override

            public View getView(int position, View convertView, ViewGroup parent)

            {

                View view = super.getView(position, convertView, parent);

                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                tv.setTextColor(Color.BLUE);


                return view;

            }

        };

        listView.setAdapter(adapter);



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseRef = database.getReference("users");

        Intent intent = getIntent();
        final String username = intent.getExtras().getString("name");



        // Read from the database 데이터베이스에서 읽어오기
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {

                        String name = fileSnapshot.child("name").getValue(String.class);
                        if(username.equals(name)) {
                            String height = fileSnapshot.child("height").getValue(String.class);
                            String weight = fileSnapshot.child("weight").getValue(String.class);
                            String age = fileSnapshot.child("age").getValue(String.class);
                            String gender = fileSnapshot.child("gender").getValue(String.class);
                            String pill = fileSnapshot.child("pill").getValue(String.class);
                            //          Log.i("TAG: value is ", name);

                            //읽어온 정보로 띄우기
                            user.setText(username + "님의 사용자 정보");
                            user_name.setText(username);
                            user_birth.setText(age);
                            user_height.setText(height);
                            user_weight.setText(weight);
                            user_gender.setText(gender);

                            if (fileSnapshot.child("pill").exists()) {

                                pill = pill.substring(1, pill.length() - 1);
                                String[] p = pill.split(",");


                                for (int i = 0; i < p.length; i++) {
                                    fileList.add(p[i]);
                                    n = p;
                                }
                                //fileList.add(p);

                                adapter.notifyDataSetChanged();
                                count += 1;

                            } else {

                            }
                        }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG: ", "Failed to read value", databaseError.toException());
            }


        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // 콜백매개변수는 순서대로 어댑터뷰, 해당 아이템의 뷰, 클릭한 순번, 항목의 아이디
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) { //약 복용 리스트에서 특정 약 이름 클릭 시
                DatabaseReference a = databaseRef2.child(username);
                pill = n[i].trim();
                a.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String Pill_name;

                        for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                            Pill_name = fileSnapshot.child("pillname").getValue(String.class);
                            System.out.println("Pill_name:"+Pill_name);
                            Pill_name = Pill_name.trim();

                            if(pill.equals(Pill_name)) { //데이터베이스에 일치하는 약 이름 확인 시
                                day = fileSnapshot.child("day").getValue(String.class);
                                motor = fileSnapshot.child("motor").getValue(String.class);
                                Intent intent = new Intent(UserActivity.this, PillActivity.class); //해당 약 복용 정보 화면으로 이동
                                intent.putExtra("day", day);
                                intent.putExtra("motor", motor);
                                intent.putExtra("name",user_name.getText().toString());
                                intent.putExtra("birth",user_birth.getText().toString());
                                intent.putExtra("pill",pill);
                                System.out.println("day1:"+day);
                                startActivity(intent);

                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TAG: ", "Failed to read value", databaseError.toException());
                    }
                });



                System.out.println("day2:"+day);

            }
        });









    }
}