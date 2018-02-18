package com.hanict.safepatrol;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by Shin on 2017-06-23.
 */

public class SettingActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        /* // 사고 다발 구역 신고 버튼 클릭시 해당 레이아웃으로 이동 코드 - 서버연동이 되지 않아 추후 적용예정
        ImageButton Report = (ImageButton)findViewById(R.id.report_button);
        Report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, Report.class);
                startActivity(intent);
            }
        }); */
        // 출퇴근 시간 설정 버튼 클릭시 해당 레이아웃으로 이동 코드
        ImageButton Commuting = (ImageButton)findViewById(R.id.commuting_button);
        Commuting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, commuting.class);
                startActivity(intent);
            }
        });
        // 알림음 설정 버튼 클릭시 해당 레이아웃으로 이동 코드
        ImageButton Alarm = (ImageButton)findViewById(R.id.alarm_button);
        Alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, alarm.class);
                startActivity(intent);
            }
        });
        ImageButton Information_button = (ImageButton)findViewById(R.id.Infomation_button);
        Information_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, InformationActivity.class);
                startActivity(intent);
            }
        });

        // 뒤로 가는 이미지 버튼 클릭시 해당 레이아웃을 종료
        ImageButton ReverseButton = (ImageButton)findViewById(R.id.reverse_button);
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
