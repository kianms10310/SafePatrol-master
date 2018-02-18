package com.hanict.safepatrol;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by answo on 2017-07-02.
 */

public class CustomMarkRendered extends DefaultClusterRenderer<AccidentDto> {
    View view;
    ImageView img;
    Context context;
    GoogleMap mMap;
    GoogleMap.OnMarkerClickListener listener = new GoogleMap.OnMarkerClickListener() {

        @Override
        public boolean onMarkerClick(Marker marker) {
            CameraUpdate center = CameraUpdateFactory.newLatLngZoom(marker.getPosition(),18);
            mMap.moveCamera(center);
            marker.showInfoWindow();

            return true;
        }

    };
    public CustomMarkRendered(Context context, GoogleMap map,
                              ClusterManager<AccidentDto> clusterManager) {
        super(context, map, clusterManager);
        view = LayoutInflater.from(context).inflate(R.layout.marker, null);
        img = (ImageView) view.findViewById(R.id.map_mark);
        this.context = context;
        mMap = map;
        // mMap.setOnMarkerClickListener(listener);



    }

    @Override
    protected void onBeforeClusterItemRendered(AccidentDto item, MarkerOptions markerOptions) {
        if(item.cnt<4){
            img.setImageResource(R.drawable.clean);
        }
        else if(item.cnt>=4 && item.cnt <9 ){
            img.setImageResource(R.drawable.caution);
        }
        else{
            img.setImageResource(R.drawable.warning);
        }
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(context,view)));
    }








    private Bitmap createDrawableFromView(Context context, View view) {
        //뷰를 비트맵으로 변환시키는 작업이래. 마크를 하나의 뷰에다가 만든담에 그걸 비트맵이미지로 변환시켜서 마크에 추가
        //컨텍스트, 마크 작업한 View
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }



    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
