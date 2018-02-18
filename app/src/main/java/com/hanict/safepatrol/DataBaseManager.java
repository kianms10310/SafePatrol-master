package com.hanict.safepatrol;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

/* Creator : SeonWooHAN
 * 1. Time Table Structure
 * 			Name			Type
 * 	start_time_hour_1	INT
 * 	start_time_Minute_1 INT
 * 	end_time_hour_1		INT
 * 	end_time_Minute_1	INT
 * 	start_time_hour_2	INT
 * 	start_time_Minute_2	INT
 * 	end_time_hour_2		INT
 * 	end_time_Minute_2	INT
 * 	2. Alarm Table Structure
 * 			Name			type
 * 	Alarm_Name			VARCHAR(50)
 * 	Alarm_Use			VARCHAR(6)
 *	User_Alarm_Use		VARCHAR(2)
 *	3. CSV Table Structure
 *	longitude	double	(위도)
 *	laitude		double	(경도)
 *	freocurr	double	(사고유형)
 */

public class DataBaseManager extends SQLiteOpenHelper{
	private static final int VERSION = 1;
	private static final String DB_NAME = "alarm.db";

	public DataBaseManager(Context context) {
		super(context, DB_NAME, null,VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase DB) {
	//	if (DB == null) {
			DB.execSQL("Create Table IF NOT EXISTS TIME (start_time_hour_1 int, start_time_Minute_1 int, end_time_hour_1 int, end_time_Minute_1 int, start_time_hour_2 int, start_time_Minute_2 int, end_time_hour_2 int, end_time_Minute_2 int) ");
			DB.execSQL("Create Table IF NOT EXISTS ALARM (Alarm_Name VARCHAR(10), Alarm_Use VARCHAR(6),User_Alarm_Use VARCHAR(2))");
			DB.execSQL("Create Table IF NOT EXISTS CSV (longitude double, laitude double,freocurr double)");
			//Time, Alarm, CSV Table 만드는 작업
			DB.execSQL("insert into TIME values(0,0,0,0,0,0,0,0)");
			//Time Table에 기본 자료 삽입
			DB.execSQL("insert into ALARM values('Unauthorized_crossing','true','0')"); //ALARM Table에 무단횡단 기본 자료 삽입
			DB.execSQL("insert into ALARM values('pedestrian','true','0')"); //ALARM Table에 보행자 기본 자료 삽입
			DB.execSQL("insert into ALARM values('School_Zone','true','0')"); //ALARM Table에 스쿨존 기본 자료 삽입
			DB.execSQL("insert into ALARM values('bicycle','true','0')"); //ALARM Table에 보행자 기본 자료 삽입
	//	}
	}

	public void TimeAppend(int Hour, int Minute, int Signal) throws Exception{ // 출퇴근 관련 데이터 베이스에 데이터를 저장하는 함수
		SQLiteDatabase db = getWritableDatabase();
		switch (Signal) {
			case 0:
				db.execSQL("Update TIME SET start_time_hour_1 = " + Hour);
				db.execSQL("Update TIME SET start_time_Minute_1 = " + Minute);
				break;
			case 1:
				db.execSQL("Update TIME SET end_time_hour_1 = " + Hour);
				db.execSQL("Update TIME SET end_time_Minute_1 = " + Minute);
				break;
			case 2:
				db.execSQL("Update TIME SET start_time_hour_2 = " + Hour );
				db.execSQL("Update TIME SET start_time_Minute_2 = " + Minute);
				break;
			case 3:
				db.execSQL("Update TIME SET end_time_hour_2 = " + Hour);
				db.execSQL("Update TIME SET end_time_Minute_2 = " + Minute);
				break;
		}
	} // Time Table에 자료를 수정하는 함수

	public HashMap<String, Integer> TimeSelect() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("Select * From TIME", null);
		HashMap<String, Integer> Items = null;
		if (cursor.moveToFirst()) {
			do {
				Items = new HashMap<String, Integer>();
				Items.put("start_time_hour_1", cursor.getInt(0));
				Items.put("start_time_Minute_1", cursor.getInt(1));
				Items.put("end_time_hour_1", cursor.getInt(2));
				Items.put("end_time_Minute_1", cursor.getInt(3));
				Items.put("start_time_hour_2", cursor.getInt(4));
				Items.put("start_time_Minute_2", cursor.getInt(5));
				Items.put("end_time_hour_2", cursor.getInt(6));
				Items.put("end_time_Minute_2", cursor.getInt(7));
			} while (cursor.moveToNext());
		}
		cursor.close();
		return Items;
	} // Time Table을 조회하여 HashMap형식으로 return하는 함수

	public HashMap<Integer, String> SignalViolationSelect(int DBName) {
		SQLiteDatabase db = getReadableDatabase();
		HashMap<Integer, String> Items = null;
		Cursor cursor;
		switch(DBName) {
			case 1: // 무단횡단 관련 Alarm Table 조회
				cursor = db.rawQuery("Select * From ALARM Where Alarm_Name = 'Unauthorized_crossing'", null);
				if (cursor.moveToFirst()) {
					do {
						Items = new HashMap<Integer, String>();
						Items.put(1, cursor.getString(1));
						Items.put(2, cursor.getString(2));
					} while (cursor.moveToNext());
				}
				cursor.close();
				return Items;
			case 2: // 스쿨존 관련 Alarm Table 조회
				cursor = db.rawQuery("Select * From ALARM Where Alarm_Name = 'pedestrian'", null);
				if (cursor.moveToFirst()) {
					do {
						Items = new HashMap<Integer, String>();
						Items.put(1, cursor.getString(1));
						Items.put(2, cursor.getString(2));
					} while (cursor.moveToNext());
				}
				cursor.close();
				return Items;
			case 3: // 자전거 관련 Alarm Table 조회
				cursor = db.rawQuery("Select * From ALARM Where Alarm_Name = 'School_Zone'", null);
				if (cursor.moveToFirst()) {
					do {
						Items = new HashMap<Integer, String>();
						Items.put(1, cursor.getString(1));
						Items.put(2, cursor.getString(2));
					} while (cursor.moveToNext());
				}
				cursor.close();
				return Items;
			case 4: // 보행자 관련 Alarm Table 조회
				cursor = db.rawQuery("Select * From ALARM Where Alarm_Name = 'bicycle'", null);
				if (cursor.moveToFirst()) {
					do {
						Items = new HashMap<Integer, String>();
						Items.put(1, cursor.getString(1));
						Items.put(2, cursor.getString(2));
					} while (cursor.moveToNext());
				}
				cursor.close();
				return Items;
		}
		return null;
	} // Alarm Table를 조회하야 HashMap형식으로 return하는 함수

	public void SignalViolationAppend(String str, int Signal, int DBName) {
		SQLiteDatabase db = getWritableDatabase();
		switch(DBName) {
			case 1:
				switch (Signal) {
					case 0:
						db.execSQL("Update ALARM SET Alarm_Use = '" + str + "' Where Alarm_Name = 'Unauthorized_crossing'");
						break;
					case 1:
						db.execSQL("Update ALARM SET User_Alarm_Use = '" + str + "' Where Alarm_Name = 'Unauthorized_crossing'");
						break;
				}
				break;
			case 2:
				switch (Signal) {
					case 0:
						db.execSQL("Update ALARM SET Alarm_Use = '" + str + "' Where Alarm_Name = 'pedestrian'");
						break;
					case 1:
						db.execSQL("Update ALARM SET User_Alarm_Use = '" + str + "' Where Alarm_Name = 'pedestrian'");
						break;
				}
				break;
			case 3:
				switch (Signal) {
					case 0:
						db.execSQL("Update ALARM SET Alarm_Use = '" + str + "' Where Alarm_Name = 'School_Zone'");
						break;
					case 1:
						db.execSQL("Update ALARM SET User_Alarm_Use = '" + str + "' Where Alarm_Name = 'School_Zone'");
						break;
				}
				break;
			case 4:
				switch (Signal) {
					case 0:
						db.execSQL("Update ALARM SET Alarm_Use = '" + str + "' Where Alarm_Name = 'bicycle'");
						break;
					case 1:
						db.execSQL("Update ALARM SET User_Alarm_Use = '" + str + "' Where Alarm_Name = 'bicycle'");
						break;
				}
				break;
		}
	} // Time Table에 자료를 수정하는 함수

	// 테스트용 Tag 기록자
	private static final String TAG = "DataBaseManger";

	public void InsertCSV(double longitude, double laitude, double freocurr) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("insert into CSV values(" + longitude + "," + laitude + "," + freocurr + ")");
		// Log.d(TAG,longitude + "," + laitude + "," +freocurr);
	} // CSV Table에 자료를 삽입하는 함수

	public HashMap<Integer, Double> ReturenCSV(double longitude, double laitude) {
		SQLiteDatabase db = getReadableDatabase();
		double maxlongitude = longitude + 0.0005;
		double minlongitude = longitude - 0.0005;
		double maxlaitude = laitude + 0.0005;
		double minlaitude = laitude - 0.0005;
		Cursor cursor = db.rawQuery("Select * From CSV WHERE longitude >= " + minlongitude +
				" AND longitude <= " + maxlongitude +
				" AND laitude >= " + minlaitude +
				" AND laitude <= " + maxlaitude, null);
		HashMap<Integer, Double> Items = new HashMap<Integer, Double>();
		int count = 1;
		if (cursor.moveToFirst()) {
			do {
				Items.put(count, cursor.getDouble(0));
				count = count + 1;
				Items.put(count, cursor.getDouble(1));
				count = count + 1;
				Items.put(count, cursor.getDouble(2));
				count = count + 1;
			} while (cursor.moveToNext());
		}
		Items.put(0, (double) count);
		Log.d(TAG, String.valueOf(count/3));
		cursor.close();
		return Items;
	} // CSV Table를 조회하야 HashMap형식으로 return하는 함수

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

	}
}
