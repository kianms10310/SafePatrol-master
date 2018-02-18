package com.hanict.safepatrol;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.ToggleButton;

import java.util.HashMap;
// Andriod 파일 탐색기 관련 사이트 http://www.androidside.com/bbs/board.php?bo_table=B56&wr_id=31382
public class alarm extends Activity {
    // DB관련 상수
    final int Unauthorized_crossing = 1;
    final int pedestrian = 2;
    final int School_Zone = 3;
    final int bicycle = 4;
    // DB관련 변수
    private DataBaseManager m_ins = null;
    private HashMap<Integer, String> InsideDataBase = null;

    // 기본 변수
    private String[] Unauthorized_crossing_List = {"안내사운드", "동물(말) 소리"};
    private String[] pedestrian_List = {"안내사운드", "동물(양) 소리"};
    private String[] School_Zone_List = {"안내사운드", "동물(고양이) 소리"};
    private String[] bicycle_List = {"안내사운드", "동물(닭) 소리"};
    private Spinner AlarmSpinner4;
    private Spinner AlarmSpinner3;
    private Spinner AlarmSpinner2;
    private Spinner AlarmSpinner1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        // 알람 SeekBar로 사운드 크기 설정
        SeekBar AlarmSeekBar = (SeekBar) findViewById(R.id.alarm_seekBar);
        final AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int nMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int nCurrentVolumn = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        AlarmSeekBar.setMax(nMax);
        AlarmSeekBar.setProgress(nCurrentVolumn);
        AlarmSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar){}
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }
        });

        // 무단횡단 사운드 알람 Spinner 설정
        AlarmSpinner1 = (Spinner) findViewById (R.id.alarm_spinner1);
        ArrayAdapter<String> list;  //문자열 어댑터 선언
        list = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Unauthorized_crossing_List); //어댑터 객체를 생성하고 보여질 아이템 리소스와 문자열 지정
        AlarmSpinner1.setAdapter(list); //스피너에 adapter 연결
        // 무단횡단 사운드 알람 ToggleSwich 설정
        final ToggleButton AlarmSwitch1 = (ToggleButton)findViewById(R.id.alarm_switch1);
        AlarmSwitch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{ // 데이터 베이스 정의하는 작업
                    m_ins = new DataBaseManager(getApplication());

                }catch(Exception e){
                    e.printStackTrace();
                }
                if(AlarmSwitch1.isChecked()) {
                    m_ins.SignalViolationAppend("true",0,Unauthorized_crossing);
                }
                else {
                    m_ins.SignalViolationAppend("false",0,Unauthorized_crossing);
                }
            }
        });
        try{ // 데이터 베이스 정의하는 작업
            m_ins = new DataBaseManager(this);
        }catch(Exception e){
            e.printStackTrace();
        }
        InsideDataBase = m_ins.SignalViolationSelect(Unauthorized_crossing);
        // DB에서 받은 값을 토태로 ToggleSwich에 적용
        if(InsideDataBase.get(1).equals("true"))
            AlarmSwitch1.setChecked(true);
        else
            AlarmSwitch1.setChecked(false);
        // DB에서 받은 값을 토태로 Spinner에 적용
        switch(Integer.parseInt(InsideDataBase.get(2))) {
            case 0:
                AlarmSpinner1.setSelection(0);
                break;
            case 1:
                AlarmSpinner1.setSelection(1);
                break;
        }
        // 무단횡단 사운드 알람 Spinner 이벤트 함수
        SettingSpinnerEvent(AlarmSpinner1);

        // 보행자 사운드 알람 Spinner 설정
        AlarmSpinner2 = (Spinner) findViewById (R.id.alarm_spinner2);   //xml에 선언한 스피너를 id값으로 불러옴
        list = new ArrayAdapter(this, android.R.layout.simple_list_item_1, pedestrian_List); //어댑터 객체를 생성하고 보여질 아이템 리소스와 문자열 지정
        AlarmSpinner2.setAdapter(list); //스피너에 adapter 연결
        // 보행자 사운드 알람 ToggleSwich 설정
        final ToggleButton AlarmSwitch2 = (ToggleButton)findViewById(R.id.alarm_switch2);
        AlarmSwitch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{ // 데이터 베이스 정의하는 작업
                    m_ins = new DataBaseManager(getApplication());
                }catch(Exception e){
                    e.printStackTrace();
                }
                if(AlarmSwitch2.isChecked()) {
                    m_ins.SignalViolationAppend("true",0,pedestrian);
                }
                else {
                    m_ins.SignalViolationAppend("false",0,pedestrian);
                }
            }
        });
        try{ // 데이터 베이스 정의하는 작업
            m_ins = new DataBaseManager(getApplication());
        }catch(Exception e){
            e.printStackTrace();
        }
        InsideDataBase = m_ins.SignalViolationSelect(pedestrian);
        // DB에서 받은 값을 토태로 ToggleSwich에 적용
        if(InsideDataBase.get(1).equals("true"))
            AlarmSwitch2.setChecked(true);
        else
            AlarmSwitch2.setChecked(false);
        // DB에서 받은 값을 토태로 Spinner에 적용
        switch(Integer.parseInt(InsideDataBase.get(2))) {
            case 0:
                AlarmSpinner2.setSelection(0);
                break;
            case 1:
                AlarmSpinner2.setSelection(1);
                break;
        }
        // 보행자 사운드 알람 Spinner 이벤트 함수
        SettingSpinnerEvent(AlarmSpinner2);

        // 스쿨존 사운드 알람 Spinner 설정
        AlarmSpinner3 = (Spinner) findViewById (R.id.alarm_spinner3);   //xml에 선언한 스피너를 id값으로 불러옴
        list = new ArrayAdapter(this, android.R.layout.simple_list_item_1, School_Zone_List); //어댑터 객체를 생성하고 보여질 아이템 리소스와 문자열 지정
        AlarmSpinner3.setAdapter(list); //스피너에 adapter 연결
        // 스쿨존 사운드 알람 ToggleSwich 설정
        final ToggleButton AlarmSwitch3 = (ToggleButton)findViewById(R.id.alarm_switch3);
        AlarmSwitch3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{ // 데이터 베이스 정의하는 작업
                    m_ins = new DataBaseManager(getApplication());
                }catch(Exception e){
                    e.printStackTrace();
                }
                if(AlarmSwitch3.isChecked()) {
                    m_ins.SignalViolationAppend("true",0,School_Zone );
                }
                else {
                    m_ins.SignalViolationAppend("false",0,School_Zone );
                }
            }
        });
        try{ // 데이터 베이스 정의하는 작업
            m_ins = new DataBaseManager(this);
        }catch(Exception e){
            e.printStackTrace();
        }
        InsideDataBase = m_ins.SignalViolationSelect(School_Zone );
        // DB에서 받은 값을 토태로 ToggleSwich에 적용
        if(InsideDataBase.get(1).equals("true"))
            AlarmSwitch3.setChecked(true);
        else
            AlarmSwitch3.setChecked(false);
        // DB에서 받은 값을 토태로 Spinner에 적용
        switch(Integer.parseInt(InsideDataBase.get(2))) {
            case 0:
                AlarmSpinner3.setSelection(0);
                break;
            case 1:
                AlarmSpinner3.setSelection(1);
                break;
        }
        // 스쿨존 사운드 알람 Spinner 이벤트 함수
        SettingSpinnerEvent(AlarmSpinner3);

        // 자전거 사운드 알람 Spinner 설정
        AlarmSpinner4 = (Spinner) findViewById (R.id.alarm_spinner4);   //xml에 선언한 스피너를 id값으로 불러옴
        list = new ArrayAdapter(this, android.R.layout.simple_list_item_1, bicycle_List); //어댑터 객체를 생성하고 보여질 아이템 리소스와 문자열 지정
        AlarmSpinner4.setAdapter(list); //스피너에 adapter 연결
        // 자전거 사운드 알람 ToggleSwich 설정
        final ToggleButton AlarmSwitch4 = (ToggleButton)findViewById(R.id.alarm_switch4);
        AlarmSwitch4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{ // 데이터 베이스 정의하는 작업
                    m_ins =  new DataBaseManager(getApplication());
                }catch(Exception e){
                    e.printStackTrace();
                }
                if(AlarmSwitch4.isChecked()) {
                    m_ins.SignalViolationAppend("true",0,bicycle);
                }
                else {
                    m_ins.SignalViolationAppend("false",0,bicycle);
                }
            }
        });
        try{ // 데이터 베이스 정의하는 작업
            m_ins = new DataBaseManager(this);
        }catch(Exception e){
            e.printStackTrace();
        }
        InsideDataBase = m_ins.SignalViolationSelect(bicycle);
        // DB에서 받은 값을 토태로 ToggleSwich에 적용
        if(InsideDataBase.get(1).equals("true"))
            AlarmSwitch4.setChecked(true);
        else
            AlarmSwitch4.setChecked(false);
        // DB에서 받은 값을 토태로 Spinner에 적용
        switch(Integer.parseInt(InsideDataBase.get(2))) {
            case 0:
                AlarmSpinner4.setSelection(0);
                break;
            case 1:
                AlarmSpinner4.setSelection(1);
                break;
        }

        // 자전거 사운드 알람 Spinner 이벤트 함수
        SettingSpinnerEvent(AlarmSpinner4);

        // 뒤로 가는 이미지 버튼 클릭시 해당 레이아웃을 종료 이벤트 설정
        ImageButton AlarmReverseButton = (ImageButton)findViewById(R.id.alarm_reverse_button);
        AlarmReverseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void SettingSpinnerEvent(Spinner spin) {
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try{ // 데이터 베이스 정의하는 작업
                    m_ins = new DataBaseManager(getApplication());
                }catch(Exception e){
                    e.printStackTrace();
                }
                if(adapterView==AlarmSpinner1) {
                    m_ins.SignalViolationAppend(String.valueOf(i), 1, Unauthorized_crossing);
                } else if (adapterView==AlarmSpinner2) {
                    m_ins.SignalViolationAppend(String.valueOf(i), 1, pedestrian);
                } else if (adapterView==AlarmSpinner3) {
                    m_ins.SignalViolationAppend(String.valueOf(i), 1, School_Zone);
                } else {
                    m_ins.SignalViolationAppend(String.valueOf(i), 1, bicycle);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }
    // 키가 눌렸을때 이벤트 설정
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AudioManager mAudioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        SeekBar AlarmSeekBar = (SeekBar) findViewById(R.id.alarm_seekBar);
        int nCurrentVolumn = 0;
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP :
                mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                nCurrentVolumn = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                AlarmSeekBar.setProgress(nCurrentVolumn);
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                nCurrentVolumn = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                AlarmSeekBar.setProgress(nCurrentVolumn);
                return true;
        }
        return false;
    }

    // 키가 떼였을때 이벤트 설정
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        AudioManager mAudioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        SeekBar AlarmSeekBar = (SeekBar) findViewById(R.id.alarm_seekBar);
        int nCurrentVolumn = 0;
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP :
                mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI);
                nCurrentVolumn = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                AlarmSeekBar.setProgress(nCurrentVolumn);
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI);
                nCurrentVolumn = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                AlarmSeekBar.setProgress(nCurrentVolumn);
                return true;
            case KeyEvent.KEYCODE_BACK:
                finish();
                return true;
        }
        return false;
    }
}
