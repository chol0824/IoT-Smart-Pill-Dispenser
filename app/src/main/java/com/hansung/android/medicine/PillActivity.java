package com.hansung.android.medicine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.GridView;
import android.widget.BaseAdapter;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;

import static com.hansung.android.medicine.UserActivity.calledAlready;

public class PillActivity extends AppCompatActivity {

    TextView user_name;
    TextView user_birth;

    FirebaseDatabase database1 = FirebaseDatabase.getInstance();
    DatabaseReference databaseRef1 = database1.getReference("Druglist");

    FirebaseDatabase database2 = FirebaseDatabase.getInstance();
    DatabaseReference databaseRef2 = database2.getReference("pill");

    String User_name;
    String User_birth;
    String day;
    String motor;

    ListView listView;

    ListView listView2;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;
    ArrayList<String> listItem2;
    ArrayList<String> listItem;

    String User_pill;
    String days;
    String DAY ="";

    String [] d;
    int p;
    int dayNum;
    int pos;



    /**
     * 연/월 텍스트뷰
     */
    private TextView tvDate;
    /**
     * 그리드뷰 어댑터
     *

    /**
     * 일 저장 할 리스트
     */
    private ArrayList<String> dayList;

    /**
     * 그리드뷰
     */
    private GridView gridView;

    /**
     * 캘린더 변수
     */
    private Calendar mCal;

    private GridAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pill_list);

        Intent intent = getIntent();
        User_name = intent.getExtras().getString("name");
        User_name = User_name.trim();

        user_name = (TextView)findViewById(R.id.name);
        user_name.setText(User_name);
        User_birth = intent.getExtras().getString("birth");
        user_birth = (TextView)findViewById(R.id.birth);
        user_birth.setText(User_birth);
        User_pill = intent.getExtras().getString("pill");
        User_pill = User_pill.trim();
        day = intent.getExtras().getString("day");
        motor = intent.getExtras().getString("motor");
        System.out.println("motor:"+motor);



        //cal = (CalendarView)findViewById(R.id.calendarView);

        listItem = new ArrayList<String>();
        listItem2 = new ArrayList<String>();

        listView = (ListView) findViewById(R.id.pill_ok);
        listView2 = (ListView) findViewById(R.id.pill_no);

        listItem.add(User_pill);
        int count = 0;

        tvDate = (TextView)findViewById(R.id.tv_date);
        gridView = (GridView)findViewById(R.id.gridview);



        //cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            //@Override
            //public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                //Toast.makeText(PillActivity.this,""+year+"/"+(month+1)+"/" +dayOfMonth,Toast.LENGTH_SHORT).show();
            //}
        //});

        databaseRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String User_pill1;

                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {

                    User_pill1 = fileSnapshot.child("user-pill").getValue(String.class);


                    if(User_pill.equals(User_pill1)) {
                        String pill_no = fileSnapshot.child("pill-no").getValue(String.class);


                        String[] p = pill_no.split(",");
                        for (int i = 0; i < p.length; i++) {

                            listItem2.add(p[i]);
                        }
                        adapter2.notifyDataSetChanged();
                    }else {

                    }
                }
                adapter2.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG: ", "Failed to read value", databaseError.toException());
            }
        });

        User_name = intent.getExtras().getString("name");
        DatabaseReference a = databaseRef2.child(User_name);

        a.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String Pill_name;

                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {

                    Pill_name = fileSnapshot.child("pillname").getValue(String.class);

                    if(User_pill.equals(Pill_name)) {
                        //String count_pill = fileSnapshot.child("count_pill").getValue(String.class);
                        String day = fileSnapshot.child("day").getValue(String.class);
                        String daytime = fileSnapshot.child("daytime").getValue(String.class);
                        String time = fileSnapshot.child("time").getValue(String.class);

                        listItem.add("복용일: "+day);
                        listItem.add("복용 시간: "+daytime);
                        listItem.add(time+ " 30분 복용 권장");

                        adapter.notifyDataSetChanged();
                    }
                    adapter2.notifyDataSetChanged();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG: ", "Failed to read value", databaseError.toException());
            }
        });



        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItem){

            @Override

            public View getView(int position, View convertView, ViewGroup parent){

                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.BLACK);
                return view;
            }
        };


        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItem2){
            @Override

            public View getView(int position, View convertView, ViewGroup parent){

                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.RED);
                return view;
            }
        };

        long now = System.currentTimeMillis();
        final Date date = new Date(now);

        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);

        //현재 날짜 텍스트뷰에 뿌려줌
        tvDate.setText(curYearFormat.format(date) + "." + curMonthFormat.format(date));

        //gridview 요일 표시
        dayList = new ArrayList<String>();
        dayList.add("일");
        dayList.add("월");
        dayList.add("화");
        dayList.add("수");
        dayList.add("목");
        dayList.add("금");
        dayList.add("토");

        mCal = Calendar.getInstance();

        //이번달 1일 무슨요일인지 판단 mCal.set(Year,Month,Day)
        mCal.set(Integer.parseInt(curYearFormat.format(date)), Integer.parseInt(curMonthFormat.format(date)) - 1, 1);
        dayNum = mCal.get(Calendar.DAY_OF_WEEK);

        //1일 - 요일 매칭 시키기 위해 공백 add
        for (int i = 1; i < dayNum; i++) {
            dayList.add("");
            System.out.println("이번달 1일은 "+ dayNum);
        }
        setCalendarDate(mCal.get(Calendar.MONTH) + 1); //공백 주고 setCalendarDate 호출

        gridAdapter = new GridAdapter(getApplicationContext(), dayList);
        gridView.setAdapter(gridAdapter);

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listView2.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();

    }

    private void setCalendarDate(int month) { //이게 실제로 날짜를 집어넣는
        mCal.set(Calendar.MONTH, month - 1);

        Intent intent = getIntent();
        day = intent.getExtras().getString("day"); // ex) 월,수,금,
        d = day.split(","); //ex) 월 수 금 쪼개져서 배열에 넣어졌음

        for(int i=0; i<d.length; i++){
            System.out.println("d:"+d[i]); // 여기서 확인 가능
        }

        for (int i = 0; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) { //여기에 그 코드를 넣어야 하는 거 같은데
            dayList.add("" + (i + 1));                                           //뭘 해도 원하는대로 안 나옴,,,,,
        }
    }



    /**
     * 그리드뷰 어댑터
     *
     */
    private class GridAdapter extends BaseAdapter {

        private final List<String> list;

        private final LayoutInflater inflater;

        /**
         * 생성자
         *
         * @param context
         * @param list
         */
        public GridAdapter(Context context, List<String> list) {
            this.list = list;
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_calendar_gridview, parent, false);

                holder = new ViewHolder();

                holder.tvItemGridView = (TextView)convertView.findViewById(R.id.tv_item_gridview);
                holder.PILL = (ImageView) convertView.findViewById(R.id.PILL);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.tvItemGridView.setText("" + getItem(position));

            if(position % 7 == 0){
                holder.tvItemGridView.setTextColor(Color.RED);
            }else if(position % 7 == 6){
                holder.tvItemGridView.setTextColor(Color.BLUE);
            }
            for(int i=0;i<d.length;i++) {
                switch (d[i]) {
                    case "월":
                        pos = 1;
                        break;
                    case "화":
                        pos = 2;
                        break;
                    case "수":
                        pos = 3;
                        break;
                    case "목":
                        pos = 4;
                        break;
                    case "금":
                        pos = 5;
                        break;
                    case "토":
                        pos = 6;
                        break;
                    case "일":
                        pos = 0;
                        break;
                    default:
                        break;
                }
                if (position % 7 == pos && position >= 7) {
                    holder.tvItemGridView.setTextColor(Color.MAGENTA);
                    if (holder.tvItemGridView.getText() == "") {

                    } else {
                        holder.tvItemGridView.setText(holder.tvItemGridView.getText() + "\n");
                        holder.PILL.setImageResource(R.drawable.pill);

                    }

                }
            }




            //해당 날짜 텍스트 컬러,배경 변경
            mCal = Calendar.getInstance();
            //오늘 day 가져옴
            Integer today = mCal.get(Calendar.DAY_OF_MONTH);
            String sToday = String.valueOf(today);
            if (sToday.equals(getItem(position))) { //오늘 day 텍스트 컬러 변경
                holder.tvItemGridView.setTextColor(getResources().getColor(R.color.colorAccent));
                if("1".equals(motor)){
                    holder.PILL.setImageResource(R.drawable.pill_0);
                }
            }

            return convertView;
        }


    }

    private class ViewHolder {
        TextView tvItemGridView;
        ImageView PILL;
    }




}


