package com.hanict.safepatrol;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TimePicker;

import java.util.HashMap;

public class commuting extends Activity {
    // DB관련 변수
    private DataBaseManager m_ins = null;
    private HashMap<String,Integer> InsideDataBase = null;
    // 엑티비티 관련 변수
    private boolean SettingCommutingStartButton1 = false;
    private boolean SettingCommutingEndButton1 = false;
    private boolean SettingCommutingStartButton2 = false;
    private boolean SettingCommutingEndButton2 = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commuting);
        // 출근 출발 시간 설정 버튼 이벤트 설정
        Button CommutingStartButton1 = (Button)findViewById(R.id.commuting_start_button1);
        CommutingStartButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button CommutingStartButton1 = (Button)findViewById(R.id.commuting_start_button1);
                Button CommutingEndButton1 = (Button)findViewById(R.id.commuting_end_button1);
                TimePicker CommutingTimePickerStart1 = (TimePicker)findViewById(R.id.commuting_start_1);
                TimePicker CommutingTimePickerEnd1 = (TimePicker)findViewById(R.id.commuting_end_1);
                if(SettingCommutingStartButton1==false) {
                    CommutingStartButton1.setText("시작시간 설정 △");
                    CommutingEndButton1.setText("종료시간 설정 ▽");
                    CommutingTimePickerStart1.setVisibility(View.VISIBLE);
                    CommutingTimePickerEnd1.setVisibility(View.GONE);
                    SettingCommutingStartButton1 = true;
                    SettingCommutingEndButton1 = false;
                } else {
                    CommutingStartButton1.setText("시작시간 설정 ▽");
                    CommutingTimePickerStart1.setVisibility(View.GONE);
                    SettingCommutingStartButton1 = false;
                }
            }
        });
        // 출근 종료 시간 설정 버튼 이벤트 설정
        Button CommutingEndButton1 = (Button)findViewById(R.id.commuting_end_button1);
        CommutingEndButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button CommutingEndButton1 = (Button)findViewById(R.id.commuting_end_button1);
                Button CommutingStartButton1 = (Button)findViewById(R.id.commuting_start_button1);
                TimePicker CommutingTimePickerEnd1 = (TimePicker)findViewById(R.id.commuting_end_1);
                TimePicker CommutingTimePickerStart1 = (TimePicker)findViewById(R.id.commuting_start_1);
                if(SettingCommutingEndButton1==false) {
                    CommutingEndButton1.setText("종료시간 설정 △");
                    CommutingStartButton1.setText("시작시간 설정 ▽");
                    CommutingTimePickerEnd1.setVisibility(View.VISIBLE);
                    CommutingTimePickerStart1.setVisibility(View.GONE);
                    SettingCommutingEndButton1 = true;
                    SettingCommutingStartButton1 = false;
                } else {
                    CommutingEndButton1.setText("종료시간 설정 ▽");
                    CommutingTimePickerEnd1.setVisibility(View.GONE);
                    SettingCommutingEndButton1 = false;
                }
            }
        });
        // 퇴근 출발 시간 설정 버튼 이벤트 설정
        Button CommutingStartButton2 = (Button)findViewById(R.id.commuting_start_button2);
        CommutingStartButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button CommutingStartButton2 = (Button)findViewById(R.id.commuting_start_button2);
                Button CommutingEndButton2 = (Button)findViewById(R.id.commuting_end_button2);
                TimePicker CommutingTimePickerStart2 = (TimePicker)findViewById(R.id.commuting_start_2);
                TimePicker CommutingTimePickerEnd2 = (TimePicker)findViewById(R.id.commuting_end_2);
                if(SettingCommutingStartButton2==false) {
                    CommutingStartButton2.setText("시작시간 설정 △");
                    CommutingEndButton2.setText("종료시간 설정 ▽");
                    CommutingTimePickerStart2.setVisibility(View.VISIBLE);
                    CommutingTimePickerEnd2.setVisibility(View.GONE);
                    SettingCommutingStartButton2 = true;
                    SettingCommutingEndButton2 = false;
                } else {
                    CommutingStartButton2.setText("시작시간 설정 ▽");
                    CommutingTimePickerStart2.setVisibility(View.GONE);
                    SettingCommutingStartButton2 = false;
                }
            }
        });
        // 퇴근 종료 시간 설정 버튼 이벤트 설정
        Button CommutingEndButton2 = (Button)findViewById(R.id.commuting_end_button2);
        CommutingEndButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button CommutingEndButton2 = (Button)findViewById(R.id.commuting_end_button2);
                Button CommutingStartButton2 = (Button)findViewById(R.id.commuting_start_button2);
                TimePicker CommutingTimePickerEnd2 = (TimePicker)findViewById(R.id.commuting_end_2);
                TimePicker CommutingTimePickerStart2 = (TimePicker)findViewById(R.id.commuting_start_2);
                if(SettingCommutingEndButton2==false) {
                    CommutingEndButton2.setText("종료시간 설정 △");
                    CommutingStartButton2.setText("시작시간 설정 ▽");
                    CommutingTimePickerEnd2.setVisibility(View.VISIBLE);
                    CommutingTimePickerStart2.setVisibility(View.GONE);
                    SettingCommutingEndButton2 = true;
                    SettingCommutingStartButton2 = false;
                } else {
                    CommutingEndButton2.setText("종료시간 설정 ▽");
                    CommutingTimePickerEnd2.setVisibility(View.GONE);
                    SettingCommutingEndButton2 = false;
                }
            }
        });
        // 데이터 베이스 작업전 타임피커 모두 선언하기
        TimePicker CommutingTimePickerStart1 = (TimePicker)findViewById(R.id.commuting_start_1);
        TimePicker CommutingTimePickerEnd1 = (TimePicker)findViewById(R.id.commuting_end_1);
        TimePicker CommutingTimePickerEnd2 = (TimePicker)findViewById(R.id.commuting_end_2);
        TimePicker CommutingTimePickerStart2 = (TimePicker)findViewById(R.id.commuting_start_2);
        try{ // 데이터 베이스 정의하는 작업
                m_ins = new DataBaseManager(this);
        }catch(Exception e){
            e.printStackTrace();
        }
        // DB에서 가져온 값을 적용하는 과정
        InsideDataBase = m_ins.TimeSelect();
        CommutingTimePickerStart1.setHour(InsideDataBase.get("start_time_hour_1"));
        CommutingTimePickerStart1.setMinute(InsideDataBase.get("start_time_Minute_1"));
        CommutingTimePickerEnd1.setHour(InsideDataBase.get("end_time_hour_1"));
        CommutingTimePickerEnd1.setMinute(InsideDataBase.get("end_time_Minute_1"));
        CommutingTimePickerStart2.setHour(InsideDataBase.get("start_time_hour_2"));
        CommutingTimePickerStart2.setMinute(InsideDataBase.get("start_time_Minute_2"));
        CommutingTimePickerEnd2.setHour(InsideDataBase.get("end_time_hour_2"));
        CommutingTimePickerEnd2.setMinute(InsideDataBase.get("end_time_Minute_2"));
        // 출근 출발 시간 변경시 이벤트 설정
        CommutingTimePickerStart1.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                try{ // 데이터 베이스 정의하는 작업
                    m_ins = new DataBaseManager(getApplication());
                    m_ins.TimeAppend(i,i1,0);
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        });
        // 출근 종료 시간 변경시 이벤트 설정
        CommutingTimePickerEnd1.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                try{ // 데이터 베이스 정의하는 작업
                    m_ins = new DataBaseManager(getApplication());
                    m_ins.TimeAppend(i,i1,1);
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        });
        // 퇴근 출발 시간 변경시 이벤트 설정
        CommutingTimePickerStart2.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                try{ // 데이터 베이스 정의하는 작업
                    m_ins = new DataBaseManager(getApplication());
                    m_ins.TimeAppend(i,i1,2);
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        });
        // 출근 종료 시간 변경시 이벤트 설정
        CommutingTimePickerEnd2.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                try{ // 데이터 베이스 정의하는 작업
                    m_ins = new DataBaseManager(getApplication());
                    m_ins.TimeAppend(i,i1,3);
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        });
        // 뒤로 가는 이미지 버튼 클릭시 해당 레이아웃을 종료
        ImageButton ReverseButton = (ImageButton)findViewById(R.id.commuting_reverse_button);
        ReverseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    // 키가 눌렸을때 이벤트 설정
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AudioManager mAudioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP :
                mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                return true;
        }
        return false;
    }

    // 키가 떼였을때 이벤트 설정
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        AudioManager mAudioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP :
                mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                return true;
            case KeyEvent.KEYCODE_BACK:
                finish();
                return true;
        }
        return false;
    }
}
