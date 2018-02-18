package com.hanict.safepatrol;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by suh15 on 2017-07-03.
 * 사진 업로드 관련 사이트 http://jeongchul.tistory.com/287
 */

public class Report extends Activity {
    private static final int PICK_FORM_CAM = 0;
    private static final int PICK_FORM_ALB = 1;
    private static final int CROP_FORM_IMA = 2;

    private Uri mlmageCaptureUri;
    private String absoultePath;

    private boolean SetDateButton = false;
    private boolean SetTimeButton = false;
    private String[] ReportList = {"무단횡단", "보행자", "스쿨존", "자전거"};

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode != RESULT_OK)
            return;
        switch(requestCode) {
            case PICK_FORM_ALB: {
                mlmageCaptureUri = data.getData();
                Log.d("smartWheel", mlmageCaptureUri.getPath().toString());
            }
            case PICK_FORM_CAM: {
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mlmageCaptureUri, "image/*");

                //CROP할 이미지를 300 * 300으로 저장
                intent.putExtra("outputX", 300);
                intent.putExtra("outputY", 300);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FORM_IMA);
                break;
            }
            case CROP_FORM_IMA: {
                if (resultCode != RESULT_OK) {
                    return;
                }
                final Bundle extras = data.getExtras();
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                        "/smartWheel" + System.currentTimeMillis() + ".jpg";
                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    ImageView ReportImageview = (ImageView) findViewById(R.id.report_Imageview);
                    ReportImageview.setImageBitmap(photo);
                    String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/smartWheel";
                    File directory_smartWheel = new File(dirPath);

                    if (!directory_smartWheel.exists())
                        directory_smartWheel.mkdir();
                    File copyFile = new File(filePath);
                    BufferedOutputStream out = null;
                    try {
                        copyFile.createNewFile();
                        out = new BufferedOutputStream(new FileOutputStream(copyFile));
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(copyFile)));
                        out.flush();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    absoultePath = filePath;
                    break;
                }
                File f = new File(mlmageCaptureUri.getPath());
                if(f.exists()) {
                    f.delete();
                }
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // 뒤로 가는 이미지 버튼 클릭시 해당 레이아웃을 종료 이벤트 설정
        ImageButton ReportReverseButton = (ImageButton)findViewById(R.id.report_reverse_button);
        ReportReverseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 사고 유형 Spinner 설정
        Spinner ReportSpinner = (Spinner) findViewById (R.id.report_spinner);   //xml에 선언한 스피너를 id값으로 불러옴
        ArrayAdapter<String> list;  //문자열 어댑터 선언
        list = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ReportList); //어댑터 객체를 생성하고 보여질 아이템 리소스와 문자열 지정
        ReportSpinner.setAdapter(list); //스피너에 adapter 연결

        //발생 날짜 설정 버튼 이벤트 설정
        Button ReportDateButton = (Button)findViewById(R.id.report_Date_button);
        ReportDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button ReportDateButton = (Button)findViewById(R.id.report_Date_button);
                Button ReportTimeButton = (Button)findViewById(R.id.report_time_button);
                DatePicker ReportDatepicker = (DatePicker)findViewById(R.id.report_datePicker);
                TimePicker ReportTimepicker = (TimePicker)findViewById(R.id.report_timePicker);
                if(SetDateButton==false) {
                    ReportDateButton.setText("날짜 설정 △");
                    ReportTimeButton.setText("시간 설정 ▽");
                    ReportDatepicker.setVisibility(View.VISIBLE);
                    ReportTimepicker.setVisibility(View.GONE);
                    SetDateButton = true;
                    SetTimeButton = false;
                } else {
                    ReportDateButton.setText("날짜 설정 ▽");
                    ReportDatepicker.setVisibility(View.GONE);
                    SetDateButton = false;
                }
            }
        });

        // 발생 시간 설정 버튼 이벤트 설정
        Button ReportTimeButton = (Button)findViewById(R.id.report_time_button);
        ReportTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button ReportDateButton = (Button)findViewById(R.id.report_Date_button);
                Button ReportTimeButton = (Button)findViewById(R.id.report_time_button);
                DatePicker ReportDatepicker = (DatePicker)findViewById(R.id.report_datePicker);
                TimePicker ReportTimepicker = (TimePicker)findViewById(R.id.report_timePicker);
                if(SetTimeButton==false) {
                    ReportTimeButton.setText("시간 설정 △");
                    ReportDateButton.setText("날짜 설정 ▽");
                    ReportTimepicker.setVisibility(View.VISIBLE);
                    ReportDatepicker.setVisibility(View.GONE);
                    SetTimeButton = true;
                    SetDateButton = false;
                } else {
                    ReportTimeButton.setText("시간 설정 ▽");
                    ReportTimepicker.setVisibility(View.GONE);
                    SetTimeButton = false;
                }
            }
        });

        // 지도에서 찾기 버튼 이벤트(추가예정)

        // 갤러리에서 이미지 불러오기(추가예정)
        Button ReportImageButton = (Button)findViewById(R.id.report_imagebutton);
        ReportImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FORM_ALB);
            }
        });
        // 카메라에서 이미지 불러오기
        Button ReportCamButton = (Button)findViewById(R.id.report_cambutton);
        ReportCamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 임시로 경로를 생성
                String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
                mlmageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mlmageCaptureUri);
                startActivityForResult(intent, PICK_FORM_CAM);
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
