package com.hanict.safepatrol;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

/**
 * Created by answo on 2017-08-08.
 */

public class MarkersThread extends Thread {
    final String TAG = "markerThread";
    private ArrayList<AccidentDto> mlist = new ArrayList<AccidentDto>();
    private Handler handler;

    public MarkersThread(ArrayList<AccidentDto> AccidentList, GoogleMap map, Context context) {
        mlist = AccidentList;

        handler = new Handler(new MarkersHandler(context, map));

    }

    @Override
    public void run() {
        if (mlist.size() != 0) {

            Log.d(TAG,"Add start");
            for(int i = 0 ; i < mlist.size() ; i++){
                Message msg = handler.obtainMessage();
                msg.obj = mlist.get(i);
                handler.sendMessage(msg);
                if(i == mlist.size()-1){
                    handler.sendEmptyMessage(444);
                }
            }
            Log.d(TAG,"Add success");
        }

    }
}

class MarkersHandler implements Handler.Callback {
    final String TAG = "markerThread";
    private GoogleMap mMap;
    ClusterManager<AccidentDto> mClusterManager;
    Context context;

    AccidentDto clickedItem;

    public MarkersHandler(Context context, GoogleMap map) {
        mMap = map;
        this.context = context;
        mClusterManager = new ClusterManager<AccidentDto>(context, mMap);
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<AccidentDto>(){
            @Override
            public boolean onClusterItemClick(AccidentDto item) {
                clickedItem = item;
                CameraUpdate center = CameraUpdateFactory.newLatLngZoom(new LatLng(item.lat,item.lon),18);
                mMap.moveCamera(center);
                return false;
            }
        });
        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setInfoWindowAdapter(new CustomMarkInfo());
        mMap.setOnMarkerClickListener(mClusterManager);


    }
    @Override
    public boolean handleMessage(Message msg) {

        AccidentDto m = (AccidentDto) msg.obj;
        if (m != null) {
            mClusterManager.addItem(m);
        }
        if(msg.what==444){
            mClusterManager.setRenderer(new CustomMarkRendered(context, mMap, mClusterManager)); //마지막일때만 렌더링해주면 되겠다^^.

        }
        return true;
    }
    public class CustomMarkInfo implements GoogleMap.InfoWindowAdapter {
        View mView;
        TextView tv;


        public CustomMarkInfo(){

            this.mView = (View)((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker_info,null);
            tv = (TextView) mView.findViewById(R.id.info_text);

        }
        @Override
        public View getInfoWindow(Marker marker) {
            if(clickedItem !=null) {
                tv.setText("주소 : "+clickedItem.freOcurr_Name+"\n"
                        +"사고발생 : "+clickedItem.cnt+"건"+"\n"
                        +"사망자수 : "+clickedItem.deathPeople+"명");
            }
            else{
                Toast.makeText(context,"marker item error",Toast.LENGTH_SHORT).show();
            }
            return mView;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return  mView;
        }


    }
}