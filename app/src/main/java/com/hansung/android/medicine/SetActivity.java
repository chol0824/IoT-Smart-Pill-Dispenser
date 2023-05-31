package com.hansung.android.medicine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class SetActivity extends AppCompatActivity {

    private TextView textView_Date;
    private TimePickerDialog.OnTimeSetListener callbackMethod;

    CheckBox Mon;
    CheckBox Tue;
    CheckBox Wed;
    CheckBox Thr;
    CheckBox Fri;
    CheckBox Sat;
    CheckBox Sun;

    CheckBox morning;
    CheckBox lunch;
    CheckBox dinner;

    CheckBox before;
    CheckBox after;

    TextView pill_title;
    EditText mg;
    EditText count_pill;
    EditText detail_memo;
    Button btn_save;
    String day;
    String daytime;
    String time;
    String from;
    int week;

    //int Count_Pill;

    private AlarmManager alarmManager;
    private GregorianCalendar mCalender;
    private NotificationManager notificationManager;
    NotificationCompat.Builder builder;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_time);

        Intent intent = getIntent();
        final String Pill_title = intent.getExtras().getString("user_pill");
        final String user_name = intent.getExtras().getString("user_name");
        pill_title = (TextView)findViewById(R.id.pill_title);
        pill_title.setText(Pill_title);

        Mon = (CheckBox)findViewById(R.id.Mon);
        Tue = (CheckBox)findViewById(R.id.Tue);
        Wed = (CheckBox)findViewById(R.id.Wed);
        Thr = (CheckBox)findViewById(R.id.Thr);
        Fri = (CheckBox)findViewById(R.id.Fri);
        Sat = (CheckBox)findViewById(R.id.Sat);
        Sun = (CheckBox)findViewById(R.id.Sun);
        morning = (CheckBox)findViewById(R.id.morning);
        lunch = (CheckBox)findViewById(R.id.lunch);
        dinner = (CheckBox)findViewById(R.id.dinner);
        before = (CheckBox)findViewById(R.id.before);
        after = (CheckBox)findViewById(R.id.after);

        mg = (EditText)findViewById(R.id.mg);
        count_pill = (EditText)findViewById(R.id.count_pill);
        detail_memo = (EditText)findViewById(R.id.detail_memo);
        btn_save = (Button)findViewById(R.id.btn_save);

        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        btn_save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                day = "";
                if(Mon.isChecked() == true){ day += "월,"; }
                if(Tue.isChecked() == true){ day += "화,"; }
                if(Wed.isChecked() == true){ day += "수,"; }
                if(Thr.isChecked() == true){ day += "목,"; }
                if(Fri.isChecked() == true){ day += "금,"; }
                if(Sat.isChecked() == true){ day += "토,"; }
                if(Sun.isChecked() == true){ day += "일,"; }
                daytime = "";
                if(morning.isChecked() == true){ daytime += "아침,"; }
                if(lunch.isChecked() == true){ daytime += "점심,"; }
                if(dinner.isChecked() == true){ daytime += "저녁,"; }
                time = "";
                if(after.isChecked() == true){ time += "식후,"; }
                if(before.isChecked() == true){ time += "식전,"; }

                Toast.makeText(getApplicationContext(),"매주 " + day + " "+daytime+" "+time+"에 "+
                        Pill_title+"을 복용합니다.",Toast.LENGTH_LONG).show();


                FirebasePostSet pillset = new FirebasePostSet(count_pill.getText().toString(),day, daytime, detail_memo.getText().toString(),
                        mg.getText().toString(),user_name, Pill_title,time);

                FirebaseTimeSet timeset = new FirebaseTimeSet(user_name, Pill_title, day, daytime, time);

                //    weight w = new weight(user_weight.getText().toString());

                // 앞 child는 맨위꺼 그밑에 child가 밑으로 싸임 rgrg
                // 하나쓰면 왜.... 여러개 user만드는거는 더 봐야할듯

                //이렇게 하나써주면 setValue에있는 값들 +싹뜸

                // we we = new we(user_weight.getText().toString());
                databaseReference.child("pill").child(user_name).child(Pill_title).setValue(pillset);
                databaseReference.child("Time").child(day).child(daytime).setValue(timeset);

                //setAlarm();
                    //AlarmReceiver에 값 전달

                    //System.out.println(user_name + day + daytime + time);

                   String []dayarr = day.split(",");
                   String []dtarr = daytime.split(",");
                   String []tarr = time.split(",");






                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat weekdayFormat = new SimpleDateFormat("EE", Locale.getDefault());
                SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
                SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

                String weekDay = weekdayFormat.format(currentTime);
                String year = yearFormat.format(currentTime);
                String month = monthFormat.format(currentTime);
                String day = dayFormat.format(currentTime);
                String count = count_pill.getText().toString();
                int d = dayarr.length;
                int Count_Pill = Integer.parseInt(count);

                week = Count_Pill/d - 1;
                week = week * 7;

                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.DATE, week);

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String strDate = df.format(cal.getTime());
                //System.out.println(strDate);


                Intent receiverIntent = new Intent(SetActivity.this, AlarmRecevier.class);
                receiverIntent.putExtra("user_name", user_name);
                receiverIntent.putExtra("Pill_title", Pill_title);
                receiverIntent.setData(Uri.parse(user_name));
                sendBroadcast(receiverIntent);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(SetActivity.this, 0, receiverIntent, 0);

                    //String from = "2021-05-13 18:17:00"; //임의로 날짜와 시간을 지정
                    //날짜 포맷을 바꿔주는 소스코드
                    //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date datetime = null;
                    try {
                        datetime = df.parse(strDate);
                        System.out.println("구매알림을 보냅니다"+datetime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        System.out.println("안됨");
                    }

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(datetime);

                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pendingIntent);


            }
        });
        //Count_Pill = Integer.parseInt(count_pill.getText().toString());


    }

}
