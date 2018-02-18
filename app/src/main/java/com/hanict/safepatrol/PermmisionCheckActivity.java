package com.hanict.safepatrol;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Created by answo on 2017-07-02.
 */

public class PermmisionCheckActivity extends Activity {
    // DB관련 변수(Han)
    private DataBaseManager m_ins = null;

    final String TAG = "Permmision_Activity";
    final int MY_LOCATION_REQUEST_CODE = 100;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        permissionCheck();

    }
    private  void permissionCheck(){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG,"GPS Permission Allow");
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
            finish();


        } else {
            // Show rationale and request permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_LOCATION_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if ( permissions.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG,"Permission Allow");
                passingData();
                //Toast.makeText(this, "GPS 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this,MainActivity.class);
                startActivity(i);
                finish();
            }
            else {
                // Permission was denied. Display an error message.
                Log.e(TAG,"Permission denied");
                Toast.makeText(this,"GPS 권한이 거부되었습니다.",Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }
    //데이터 파싱 및 파싱 위치정보 DB 저장 [실행초기 렉발생] (Han)
    void passingData() {
        List<String[]> data = new ArrayList<>();
        try {
            m_ins = new DataBaseManager(getApplication());
            CSVReader reader = new CSVReader(new InputStreamReader(getAssets().open("data.csv"), "EUC-KR")); //한글을 출력하기 위해서는 인코딩 값을 EUC-KR로~, csv의 인코딩 형식 알아놓기?
            data = reader.readAll();
            for (int i = 1; i < data.size(); i++) {
                m_ins.InsertCSV(Double.parseDouble(data.get(i)[10]), Double.parseDouble(data.get(i)[11]), Double.parseDouble(data.get(i)[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
