package com.hanict.safepatrol;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class MainActivity extends Activity implements OnMapReadyCallback { //NoTitleBar 테마를 사용할경우 APPCOMPATACTIVITY와 충돌, 그냥 ACTIVITY 상속
    private static final String TAG = "Main_job";



    Context context = this;
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch(v.getId()){

                case R.id.go_setting_button:
                    Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(intent);
                    break;
                case R.id.go_stats_button:

                    Intent stats = new Intent(MainActivity.this, StaticActivity.class);
                    startActivity(stats);
                    break;

            }
        }
    };
    // DB관련 변수(Han)
    private DataBaseManager m_ins = null;
    //맵 관련
    LatLng myLocation;
    GoogleMap mMap;
    final int MY_LOCATION_REQUEST_CODE = 100;
    LocationManager lm;
    ArrayList<AccidentDto> total_data = new ArrayList<>();
    public static ArrayList<AccidentDto> send_data = new ArrayList<>();
    //지도 검색 관련
    List<Address> list = null;
    EditText srch_et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionCheck();
        total_data.addAll(passingData());
        send_data.addAll(total_data);
        findViewById(R.id.search_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater=getLayoutInflater();


                String str = srch_et.getText().toString();
                if(str != null && str !="") {
                    try {
                        Geocoder geocoder = new Geocoder(getApplicationContext());
                        list = geocoder.getFromLocationName(str, 15);
//                        if(list.size()>1){           같은 동이 여러개일 경우를 처리하려고 했으나, 그럴 일이 없는 듯..
//                            Log.d(TAG,"here is list size more than 1");
//                            String[] list_location = new String[list.size()];
//                            for(int i = 0 ; i < list.size() ; i++){
//                                list_location[i] = list.get(i).getFeatureName();
//                            }
//                            final View view =inflater.inflate(R.layout.location_list,null);
//                            ListView lv = (ListView) view.findViewById(R.id.location_list);
//                            ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1, list_location);
//                            lv.setAdapter(adapter);
//                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                    LatLng location = null;
//                                    try {
//                                        location = new LatLng(list.get(position).getLatitude(), list.get(position).getLongitude());
//                                    }catch(Exception e){
//                                        Toast.makeText(context,"주소 위치 받기 에러",Toast.LENGTH_SHORT).show();
//                                    }if(location !=null) {
//                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
//                                    }else{
//                                        Toast.makeText(context,"주소 입력 에러.",Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//
//                            AlertDialog.Builder buider= new AlertDialog.Builder(context); //AlertDialog.Builder 객체 생성
//                            buider.setTitle("지역선택"); //Dialog 제목
//                            buider.setIcon(android.R.drawable.ic_menu_add); //제목옆의 아이콘 이미지(원하는 이미지 설정)
//                            buider.setView(view); //위에서 inflater가 만든 dialogView 객체 세팅 (Customize)
//                            buider.setNegativeButton("취소", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                }
//                            });
//
//                            AlertDialog dialog=buider.create();
//
//                            //Dialog의 바깥쪽을 터치했을 때 Dialog를 없앨지 설정
//                            dialog.setCanceledOnTouchOutside(true);//없어지지 않도록 설정
//
//                            //Dialog 보이기
//                            dialog.show();
//                        }
                        LatLng location = null;
                        try {
                            location = new LatLng(list.get(0).getLatitude(), list.get(0).getLongitude());
                        } catch (Exception e) {
                            Toast.makeText(context, "주소 위치 받기 에러", Toast.LENGTH_SHORT).show();
                        }
                        if (location != null) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
                        } else {
                            Toast.makeText(context, "주소 입력 에러.", Toast.LENGTH_SHORT).show();
                        }

                    }catch(IOException e){
                        Toast.makeText(context,"주소 받기 에러",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        findViewById(R.id.go_setting_button).setOnClickListener(listener);
        findViewById(R.id.go_stats_button).setOnClickListener(listener);
        srch_et= (EditText) findViewById(R.id.search_edit);
        srch_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Enter key Action
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    //엔터키 눌렀을때 처리.

                    return true;
                }
                return false;
            }
        });
        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // 2017년 SeonWooHan 2017년 07월 03일 부분 수정 - 버튼 이벤트 넣기
        Button GoSetting = (Button)findViewById(R.id.go_setting_button);
        GoSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
        Intent locationAlarmService = new Intent(this,LocationalarmService.class);
        if(isServiceRunningCheck()){
            //서비스 이미 실행중.
        }else {
            startService(locationAlarmService);
        }
    }
    private  void permissionCheck(){

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG,"GPS Permission Allow");

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                    100, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);
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
                Toast.makeText(this,"GPS 권한이 허용되었습니다.",Toast.LENGTH_SHORT).show();

            }
            else {
                // Permission was denied. Display an error message.
                Log.e(TAG,"Permission denied");
                Toast.makeText(this,"GPS 권한이 거부되었습니다.",Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }


    //GPS 리스너
    private final LocationListener mLocationListener = new LocationListener() {
        int cnt = 0;
        public void onLocationChanged(Location location) {
            //위치가 바뀌면 여기서 처리
            myLocation = new LatLng(location.getLatitude(),location.getLongitude());
            Log.e(TAG,"changed location");
            if(cnt%2==0)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 18));
            cnt++;
            Log.e(TAG,"Location changed : "+myLocation.latitude+", "+myLocation.longitude);
            Intent intent = new Intent("com.hanict.safepatrol.LOCATION"); //  매번생성에서 보내줘야합니다. 보낼떄마다 새로생성해야됩니다.
            intent.putExtra("Latitude",myLocation.latitude);
            intent.putExtra("Longitude",myLocation.longitude);
            sendBroadcast(intent);
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

    //지도 초기값.
    @Override
    public void onMapReady(final GoogleMap map) {
        mMap = map;
        mMap.setMaxZoomPreference(18);
        mMap.setMinZoomPreference(14);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.setMyLocationEnabled(true);

        setDataInMap();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            Location l = getLastKnownLocation();
            if(l != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(l.getLatitude(), l.getLongitude()), 18));
            }

        }

    }

    private void setDataInMap(){
        if(total_data.size()!=0) {
            new MarkersThread(total_data,mMap,this).start();
        }

    }

    //데이터 파싱
    ArrayList<AccidentDto> passingData() {
        ArrayList<AccidentDto> result = new ArrayList<>();
        List<String[]> data = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(getAssets().open("data.csv"), "EUC-KR")); //한글을 출력하기 위해서는 인코딩 값을 EUC-KR로~, csv의 인코딩 형식 알아놓기?
            data = reader.readAll();
            for (int i = 1; i < data.size(); i++) {
                result.add(new AccidentDto(data.get(i)[0], data.get(i)[1], data.get(i)[2], data.get(i)[3], data.get(i)[4], Integer.parseInt(data.get(i)[5]), Integer.parseInt(data.get(i)[6])
                        , Integer.parseInt(data.get(i)[7]), Integer.parseInt(data.get(i)[8]), Integer.parseInt(data.get(i)[9])
                        , Double.parseDouble(data.get(i)[10]), Double.parseDouble(data.get(i)[11])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        lm.removeUpdates(mLocationListener);


    }

    private Location getLastKnownLocation() {
        lm = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = null;
            try {
                l = lm.getLastKnownLocation(provider);
            }catch(SecurityException e){

            }
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    public boolean isServiceRunningCheck() {
        ActivityManager manager = (ActivityManager) this.getSystemService(Activity.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.hanict.safepatrol.LocationalarmService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
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
