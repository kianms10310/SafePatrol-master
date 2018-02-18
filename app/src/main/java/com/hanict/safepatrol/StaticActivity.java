package com.hanict.safepatrol;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class StaticActivity extends Activity {
    ArrayList<AccidentDto> total_data = new ArrayList<>();
    int count[] = new int[4];
    @Override
    // 사고 구역 신고 레이아웃(서버연동이 안됨)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_activity);
        total_data.addAll(MainActivity.send_data);

        arrangeData();
        BarChart chart = (BarChart) findViewById(R.id.chart);

        ArrayList<BarEntry> values = new ArrayList<>();
        values.add(new BarEntry((float) count[0],0));
        values.add(new BarEntry((float) count[1],1));
        values.add(new BarEntry((float) count[2],2));
        values.add(new BarEntry((float) count[3],3));
        ArrayList<String> BarEntryLabels = new ArrayList<>();
        BarEntryLabels.add("무단횡단");
        BarEntryLabels.add("보행자");
        BarEntryLabels.add("스쿨존");
        BarEntryLabels.add("자전거");
        BarDataSet barDataSet = new BarDataSet(values,"사고구분");
        barDataSet.setValueTextSize(30);
        BarData barData = new BarData(BarEntryLabels,barDataSet);
        barData.setValueTextSize(30);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.setData(barData);
        chart.getXAxis().setTextSize(12);
        chart.setClickable(false);
        chart.setFocusable(false);
        chart.setDescription("");
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);

        chart.animateY(3000);
    }

    void arrangeData(){
        for(int i = 0; i < total_data.size() ; i++){
            switch(total_data.get(i).freOcurr_Group){
                case "2016052": //무단횡단
                    count[0]++;
                    break;
                case "2016046": //보행자
                    count[1]++;
                    break;
                case "2016040": //스쿨존
                    count[2]++;
                    break;
                case "2016047": //자전거
                    count[3]++;
                    break;
            }
        }
    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

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
