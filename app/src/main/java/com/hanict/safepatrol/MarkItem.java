package com.hanict.safepatrol;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by answo on 2017-06-27.
 */

public class MarkItem implements ClusterItem{
    double lat;
    double lon;
    int level;

    public MarkItem(double lat, double lon, int level)
    { this.lat = lat; this.lon = lon; this.level = level;  }

    @Override
    public LatLng getPosition() {
        LatLng l = new LatLng(this.lat,this.lon);
        return l;
    }
}
