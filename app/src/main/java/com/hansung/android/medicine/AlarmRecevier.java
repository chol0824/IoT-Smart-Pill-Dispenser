package com.hansung.android.medicine;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;


public class AlarmRecevier extends BroadcastReceiver { // 약 구매 권장 알림 구현을 위해 AlarmRecevier

    public AlarmRecevier(){ }

    NotificationManager manager;
    NotificationCompat.Builder builder;

    //오레오 이상은 반드시 채널을 설정해줘야 Notification이 작동함
    private static String CHANNEL_ID = "channel1";
    private static String CHANNEL_NAME = "Channel1";




    @Override
    public void onReceive(Context context, Intent intent) {

        // 시스템에서 AlarmManager 받아옴
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        builder = null;
        manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            manager.createNotificationChannel(
                    new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            );
            builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(context);
        }




        //알림창 클릭 시 작동할 activity 화면 설정하기
        Intent intent2 = new Intent(context, IntroActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,101,intent2, PendingIntent.FLAG_UPDATE_CURRENT);

        String name =intent.getStringExtra("user_name"); // 이름
        String pill = intent.getStringExtra("Pill_title"); //약

        //알림창 제목
        builder.setContentTitle(name+" 님 !"+"["+pill+"] 약이 다 떨어져가요 ㅠ.ㅠ ");
        //알림창 아이콘
        builder.setSmallIcon(R.drawable.startpill);
        //알림창 터치시 자동 삭제
        builder.setAutoCancel(true);

        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        manager.notify(1,notification);

    }
}
