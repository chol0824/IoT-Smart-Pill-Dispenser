package com.hansung.android.medicine;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Context;
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
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textView_Date;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;

    private ListView listView;
    List fileList = new ArrayList<>();
    ArrayAdapter adapter;
    static boolean calledAlready = false;

    TextView textuser;


    //ArrayAdapter<String> adapter2;
    //ArrayList<String> listItem2;
    String str;
    String [] n;
    int count;

    String morning_after = " 08:30:00";
    String morning_before = " 07:30:00";
    String lunch_after = " 13:30:00";
    String lunch_before = " 12:00:00";
    String dinner_after = " 19:30:00";
    String dinner_before = " 18:00:00";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (!calledAlready)
        {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true); // 다른 인스턴스보다 먼저 실행되어야 한다.
            calledAlready = true;
        }

        listView = (ListView) findViewById(R.id.user_list);


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


        textuser = (TextView) findViewById(R.id.text1);
        textuser.setText("등록된 사용자가 없습니다.");



        final Button btn_regis = (Button) findViewById(R.id.register);
        btn_regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisActivity.class);
                startActivity(intent);
            }
        });



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseRef = database.getReference("users");




        // Read from the database
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    //MyFiles filename = (MyFiles) fileSnapshot.getValue(MyFiles.class);

                    if(fileSnapshot.exists()) {

                         str = fileSnapshot.child("name").getValue(String.class);

                        Log.i("TAG: value is ", str);
                        fileList.add(str);
                        count +=1;
                    //    btn_regis.setVisibility(View.INVISIBLE);
                        textuser.setVisibility(View.INVISIBLE);
                    }
                    else{
                    //    btn_regis.setVisibility(View.VISIBLE);
                        textuser.setVisibility(View.VISIBLE);
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
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                intent.putExtra("name", fileList.get(i).toString());
                startActivity(intent);
            }
           /* public void onItemDoubleClick(AdapterView<?> adapterView, View view, int i, long l){

                Toast.makeText(getApplicationContext(),listItem.get(i).toString() + "삭제",Toast.LENGTH_SHORT).show();
                listItem.remove(i);
                adapter.notifyDataSetChanged();

            }

            */
        });

        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        DatabaseReference databaseRef2 = database2.getReference("pill");





    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReference.removeEventListener(mChild);
    }
}
