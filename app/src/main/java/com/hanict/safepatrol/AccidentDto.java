package com.hanict.safepatrol;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by answo on 2017-07-20.
 * 나중에는 파싱할떄 이 모델로 사용.
 */

public class AccidentDto implements ClusterItem {
    String freOcurr_Id;
    String freOcurr_Group;
    String law_Code;
    String spot_Code;
    String freOcurr_Name;
    int cnt; //발생건수
    int hurtPeople; //총 다치거나 죽은 사람
    int deathPeople; //죽은사람
    int middlePeople; //중상자
    int smallPeople;//경상자
    double lat;
    double lon;

    public AccidentDto(String freOcurr_Id, String freOcurr_Group, String law_Code, String spot_Code,String freOcurr_Name,int cnt, int hurtPeople, int deathPeople, int middlePeople, int smallPeople, double lon, double lat) {
        this.freOcurr_Id = freOcurr_Id;
        this.freOcurr_Group = freOcurr_Group;
        this.law_Code = law_Code;
        this.spot_Code = spot_Code;
        this.freOcurr_Name = freOcurr_Name;
        this.cnt = cnt;
        this.hurtPeople = hurtPeople;
        this.deathPeople = deathPeople;
        this.middlePeople = middlePeople;
        this.smallPeople = smallPeople;
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public LatLng getPosition() {
        LatLng l = new LatLng(this.lat,this.lon);
        return l;
    }
}
