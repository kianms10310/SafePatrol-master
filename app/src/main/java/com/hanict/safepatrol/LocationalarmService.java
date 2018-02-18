package com.hanict.safepatrol;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by answo on 2017-07-14.
 * 백그라운드 소리 알람 서비스, 백그라운드에서 위치 계속 확인
 */


public class LocationalarmService extends Service {
    // DB관련 변수 추가 (Han)
    private DataBaseManager DB = null;
    private HashMap<String,Integer> InsideDataBase = null;
    private HashMap<Integer, String> InsideDataBase_alarm = null;
    // * DB에서 가져온 값을 저장할 변수(Han) *
    // 첫번째 변수들 출근시간 범위 변수
    // 두번째 변수들 퇴근시간 범위 변수
    public int start_time_hour_1, start_time_Minute_1, end_time_hour_1, end_time_Minute_1;
    public int start_time_hour_2, start_time_Minute_2, end_time_hour_2, end_time_Minute_2;
    // 무단횡단 관련 알림 변수
    public boolean Unauthorized_crossing_alarm_use;
    public int Unauthorized_crossing_user_alarm_use;
    // 보행자 관련 알림 변수
    public boolean pedestrian_alarm_use;
    public int pedestrian_user_alarm_use;
    // 스쿨존 관련 알림 변수
    public boolean School_Zone_alarm_use;
    public int  School_Zone_user_alarm_use;
    // 자전거 관련 알림 변수
    public boolean bicycle_alarm_use;
    public int bicycle_user_alarm_use;
    // DB 관련 상수(Han)
    final int Unauthorized_crossing = 1;
    final int pedestrian = 2;
    final int School_Zone = 3;
    final int bicycle = 4;

    // 현재시간 관련 변수(Han)
    long mNow;
    Date mDate;
    int now;

    //TAG 변수
    private static final String TAG = "LocationalarmService";

    final int DIV_VALUE = 10000000;

    //DB 관련 변수
    private DataBaseManager m_ins = null;
    private HashMap<Integer, Double> InsideDataBase_locatoion = null;

    // 위치 저장 관련 변수
    public double Latitude, Longitude;

    // 사운드 알람 관련 변수
    SoundAlarm Soundalarm = null;

    // OutLocation 클래스 선언
    OutLocation outlocation;

    public LatLng myLocation = null;
    LocationManager lm;
    Intent intent;
    boolean onAir = true;
    //GPS 리스너
    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    class locationThread extends Thread {
        @Override
        public void run() {
            SystemClock.sleep(1000);
            while (onAir) {
                try {
                    lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    Location l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    myLocation = new LatLng(l.getLatitude(), l.getLongitude());
                    intent = new Intent("com.hanict.safepatrol.LOCATION"); //  매번생성에서 보내줘야합니다. 보낼떄마다 새로생성해야됩니다.
                    Latitude =  myLocation.latitude;
                    Longitude = myLocation.longitude;
                    intent.putExtra("Latitude", myLocation.latitude);
                    intent.putExtra("Longitude", myLocation.longitude);
                    sendBroadcast(intent);
                    // Log.d(TAG, "In service location : " + l.getLongitude() + " ," + l.getLatitude());
                } catch (SecurityException e) {
                    Log.d(TAG, e.toString());
                }
                SystemClock.sleep(1000);
            }
        }
    }

    @Override
    public void onCreate() {
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1, mLocationListener);
        }catch(SecurityException e){
            Log.d(TAG, e.toString());
        }
        try {
            Soundalarm = new SoundAlarm(); // 사운드 알람 클래스 선언(Han)
        } catch (IOException e) {
            e.printStackTrace();
        }
        locationThread t = new locationThread();
        t.start();
        outlocation = new OutLocation();
//        LocationalarmService.startForeground(this);
//        Intent localIntent = new Intent(this, LocationalarmService.class);
//        startService(localIntent);
    }

    @Override
    public void onDestroy() {

        onAir=false;
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void startForeground(Service service){
        if(service != null){
            try{
                Notification notification = getNotification(service);
                if(notification != null){
                    service.startForeground(1220,notification);
                }
            }catch (Exception e){

            }
        }
    }
    public static Notification getNotification(Context paramContext){
        int smallIcon = R.mipmap.ic_launcher;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            smallIcon = R.mipmap.ic_launcher_round;
        }
        Notification notification =  new NotificationCompat.Builder(paramContext)
                .setSmallIcon(smallIcon)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setAutoCancel(true)
                .setWhen(0)
                .setTicker("").build();
        notification.flags = 16;
        return  notification;
    }

    public void RocationCheck(double Longitude, double Latitude, DataBaseManager m_ins) {
        outlocation.CheckingOutLocation(Longitude,Latitude);
        // 추가 위치가 비슷한 값을 받아오는 메소드(Han)
        InsideDataBase_locatoion = m_ins.ReturenCSV(Longitude, Latitude);
        //Log.d(TAG, "RocationCheck : Saving Items"); // DB_Upgrade 메소드 체크
        if (InsideDataBase_locatoion.get(0) != 1) {
            //Log.d(TAG, "RocationCheck : go to if()_1"); // DB_Upgrade 메소드 체크
            for (int count = 1; count < InsideDataBase_locatoion.get(0); count = count + 3) {
                if(outlocation.SerchingOutLocation(InsideDataBase_locatoion.get(count),InsideDataBase_locatoion.get(count+1))) {
                    //Log.d(TAG, "RocationCheck : SoundAlarm_play Start!"); // DB_Upgrade 메소드 체크
                    outlocation.ChekInLocation(InsideDataBase_locatoion.get(count),InsideDataBase_locatoion.get(count+1));
                    Soundalarm.Alarm_Play(InsideDataBase_locatoion.get(count + 2));
                }
            }
        }
    }
    // 알람 관련 내부 Class
    class SoundAlarm {
        // 사운드 관련 변수 정의
        MediaPlayer Unauthorized_crossing_Alarm;
        MediaPlayer pedestrian_Alarm;
        MediaPlayer School_Zone_Alarm;
        MediaPlayer bicycle_Alarm;
        MediaPlayer Human_ignore;
        MediaPlayer Human_Walker;
        MediaPlayer Human_Scool;
        MediaPlayer Human_Bicycle;
        SoundAlarm() throws IOException {
            // 알람 재생 생성자(효과음 및 배경음 선언)
            Unauthorized_crossing_Alarm = MediaPlayer.create(getApplication(), R.raw.horse);
            pedestrian_Alarm = MediaPlayer.create(getApplication(), R.raw.goat);
            School_Zone_Alarm = MediaPlayer.create(getApplication(), R.raw.cat);
            bicycle_Alarm = MediaPlayer.create(getApplication(), R.raw.chicken);
            Human_ignore = MediaPlayer.create(getApplication(), R.raw.humanignore);
            Human_Walker = MediaPlayer.create(getApplication(), R.raw.humanwalker);
            Human_Scool = MediaPlayer.create(getApplication(), R.raw.humanschool);
            Human_Bicycle = MediaPlayer.create(getApplication(), R.raw.humanbicycle);
        }
        void Alarm_Play(Double alarm_stat) {
            //Log.d(TAG, "Alarm_Play : AlarmStart!"); // DB_Upgrade 메소드 체크
            switch (String.valueOf(alarm_stat)) {
                case "2016052.0": //무단횡단
                    switch(Unauthorized_crossing_user_alarm_use) {
                        case 0:
                            Human_ignore.start();
                            break;
                        case 1:
                            Unauthorized_crossing_Alarm.start(); // 무단횡단 동물 사운드 재생 메소드
                            break;
                    }
                    break;
                case "2016046.0": //보행자
                    if(pedestrian_alarm_use) {
                        switch(pedestrian_user_alarm_use) {
                            case 0:
                                Human_Walker.start();
                                break;
                            case 1:
                                pedestrian_Alarm.start(); // 보행자 동물 사운드 재생 메소드
                                break;
                        }
                    }
                    break;
                case "2016040.0": //스쿨존
                    if(School_Zone_alarm_use) {
                        switch(School_Zone_user_alarm_use) {
                            case 0:
                                Human_Scool.start();
                                break;
                            // 소스코드 추가
                            case 1:
                                School_Zone_Alarm.start(); // 스쿨존 동물 사운드 재생 메소드
                                break;
                        }
                    }
                    break;
                case "2016047.0": //자전거
                    if(bicycle_alarm_use) {
                        switch(bicycle_user_alarm_use) {
                            case 0:
                                Human_Bicycle.start();
                                break;
                            case 1:
                                bicycle_Alarm.start(); // 자전거 동물 사운드 재생 메소드
                                break;
                        }
                    }
                    break;
            }
        }
        void AllSoundStop() {
            Unauthorized_crossing_Alarm.stop();
            pedestrian_Alarm.stop();
            School_Zone_Alarm.stop();
            bicycle_Alarm.stop();
        } // 모든 알림 사운드를 중지하는 메소드
    }

    Runnable mRunnable;
    public int onStartCommand(Intent intent, int flags, int startId) {
        final Handler mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                DB = new DataBaseManager(getApplicationContext());
                SystemClock.sleep(1000);
                while(onAir){
                    try {
                        DBUpgrade(DB);
                        NowTime();
                        //Log.d(TAG, "Now Handler Statas : Time Chkeing Start"); // Rocation 메소드 체크
                        if(timecheck()) {
                            //Log.d(TAG, "Now Handler Statas : RocationCheck Method Start"); // Rocation 메소드 체크
                            RocationCheck(Longitude,Latitude, DB);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    SystemClock.sleep(1000);
                }
            }
        };
        mHandler.postDelayed(mRunnable, 10 * 1000);
        return super.onStartCommand(intent, flags, startId);
    }

    public void DBUpgrade(DataBaseManager m_ins) {

        InsideDataBase = m_ins.TimeSelect();
        start_time_hour_1 = InsideDataBase.get("start_time_hour_1");
        start_time_Minute_1 = InsideDataBase.get("start_time_Minute_1");
        end_time_hour_1 = InsideDataBase.get("end_time_hour_1");
        end_time_Minute_1 = InsideDataBase.get("end_time_Minute_1");
        start_time_hour_2 = InsideDataBase.get("start_time_hour_2");
        start_time_Minute_2 = InsideDataBase.get("start_time_Minute_2");
        end_time_hour_2 = InsideDataBase.get("end_time_hour_2");
        end_time_Minute_2 = InsideDataBase.get("end_time_Minute_2");
        // DB관련 알람 자료 추출 소스코드(Han) - 무단횡단
        InsideDataBase_alarm = m_ins.SignalViolationSelect(Unauthorized_crossing);
        if(InsideDataBase_alarm.get(1).equals("true"))
            Unauthorized_crossing_alarm_use = true;
        else
            Unauthorized_crossing_alarm_use = false;
        Unauthorized_crossing_user_alarm_use = Integer.parseInt(InsideDataBase_alarm.get(2));

        // DB관련 알람 자료 추출 소스코드(Han) - 보행자
        InsideDataBase_alarm = m_ins.SignalViolationSelect(pedestrian);
        if(InsideDataBase_alarm.get(1).equals("true"))
            pedestrian_alarm_use = true;
        else
            pedestrian_alarm_use = false;
        pedestrian_user_alarm_use = Integer.parseInt(InsideDataBase_alarm.get(2));

        // DB관련 알람 자료 추출 소스코드(Han) - 스쿨존
        InsideDataBase_alarm = m_ins.SignalViolationSelect(School_Zone);
        if(InsideDataBase_alarm.get(1).equals("true"))
            School_Zone_alarm_use = true;
        else
            School_Zone_alarm_use = false;
            School_Zone_user_alarm_use = Integer.parseInt(InsideDataBase_alarm.get(2));

        // DB관련 알람 자료 추출 소스코드(Han) - 자전거
        InsideDataBase_alarm = m_ins.SignalViolationSelect(bicycle);
        if(InsideDataBase_alarm.get(1).equals("true"))
            bicycle_alarm_use = true;
        else
            bicycle_alarm_use = false;
        bicycle_user_alarm_use = Integer.parseInt(InsideDataBase_alarm.get(2));
    }

    // 현재시간 구하기
    public void NowTime() {
        int hour;
        int minute;
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        String inTime   = new java.text.SimpleDateFormat("HHmmss").format(new java.util.Date());
        hour = Integer.parseInt(inTime.substring(0,2));
        minute = Integer.parseInt(inTime.substring(2,4));
        now = (hour * 60) + minute;
    }
    // 시간 비교후 return
    public boolean timecheck() {
        int start_time_1 = (start_time_hour_1 * 60) + start_time_Minute_1;
        int start_end_1 = (end_time_hour_1 * 60) + end_time_Minute_1;
        int start_time_2 = (start_time_hour_2 * 60) + start_time_Minute_2;
        int start_end_2 = (end_time_hour_2 * 60) + end_time_Minute_2;
        if(start_time_1 <= now && start_end_1 >= now) {
            return true;
        }
        else if(start_time_2 <= now && start_end_2 >= now)
            return true;
        else
        {
            return false;
        }
    }
    class OutLocation {
        //중복 방지 Location 설정
        private Double[][] outLocation = new Double[10][2];
        final int MaxLow = 10;
        private int NowLow = 0;
        public void ChekInLocation(Double Longitude, Double Latitude) {
            outLocation[NowLow][0] = Longitude;
            outLocation[NowLow][1] = Latitude;
            NowLow++;
            //Log.d(TAG,"★ OutLocation class : CheckIn!");
        }
        public void CheckingOutLocation(double longitude, double laitude) {
            double maxlongitude = longitude + 0.0005;
            double minlongitude = longitude - 0.0005;
            double maxlaitude = laitude + 0.0005;
            double minlaitude = laitude - 0.0005;
            for(int i = 0; i < NowLow; i++) {
               // Log.d(TAG,"OutLocation class : LowLow = " + NowLow);
                double Longitube = outLocation[i][0].doubleValue();
                double Laitude = outLocation[i][1].doubleValue();
                if(!(Longitube <= maxlongitude && Longitube >= minlongitude &&
                        Laitude <= maxlaitude && Laitude >= minlaitude)) {
                    if(NowLow > 1) {
                        for(int j=i; i < NowLow-1; j++) {
                            outLocation[j][0] = outLocation[j+1][0];
                            outLocation[j][1] = outLocation[j+1][0];
                        }
                    } else {
                        outLocation[0][0] = null;
                        outLocation[0][1] = null;
                    }
                    NowLow--;
                }
            }
        }
        public boolean SerchingOutLocation(Double longitude, Double laitude) {
            if(NowLow==0) {
                return true;
            }
            boolean Check = true;
            for (int i = 0; i < NowLow; i++) {
                if (outLocation[i][0].doubleValue() == longitude.doubleValue() && outLocation[i][1].doubleValue() == laitude.doubleValue()) {
                    //Log.d(TAG, "OutLocation class : Check = " + Check);
                    Check = false;
                }
            }
            if(Check == false) {
                return false;
            } else {
                return true;
            }
        }
    }
}
