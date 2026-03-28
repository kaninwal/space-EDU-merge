package com.spacECE.spaceceedu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBController extends SQLiteOpenHelper {

    private static DBController instance;
    private static final String TAG = "DBController";
    private static final String DATABASE_NAME = "Activity.db";
    private static final int DATABASE_VERSION = 1;

    public static synchronized DBController getInstance(Context context) {
        if (instance == null) {
            instance = new DBController(context.getApplicationContext());
        }
        return instance;
    }

    public DBController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS ActivityData" +
                "(status TEXT," +
                "id TEXT," +
                "name TEXT," +
                "level TEXT," +
                "dev_domain TEXT," +
                "objectives TEXT," +
                "key_dev TEXT," +
                "material TEXT," +
                "assessment TEXT," +
                "process TEXT," +
                "instructions TEXT," +
                "complete_status TEXT," +
                "result TEXT)";

        db.execSQL(sql);
        Log.d(TAG, "onCreate: Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ActivityData");
        onCreate(db);
    }

    public List<ActivityData> getAll() {
        List<ActivityData> activityDataList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM ActivityData", null);
            while (cursor.moveToNext()) {
                activityDataList.add(getActivityDataFromCursor(cursor));
            }
        } catch (Exception e) {
            Log.e(TAG, "getAll error", e);
        } finally {
            if (cursor != null) cursor.close();
        }
        return activityDataList;
    }

    public ActivityData getLastActivity() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        ActivityData lastActivity = null;
        try {
            cursor = db.rawQuery("SELECT * FROM ActivityData ORDER BY rowid DESC LIMIT 1", null);
            if (cursor.moveToFirst()) {
                lastActivity = getActivityDataFromCursor(cursor);
            }
        } catch (Exception e) {
            Log.e(TAG, "getLastActivity error", e);
        } finally {
            if (cursor != null) cursor.close();
        }
        return lastActivity;
    }

    private ActivityData getActivityDataFromCursor(Cursor cursor) {
        String status = cursor.getString(0);
        String no = cursor.getString(1);
        String name = cursor.getString(2);
        String level = cursor.getString(3);
        String dev_domain = cursor.getString(4);
        String objectives = cursor.getString(5);
        String key_dev = cursor.getString(6);
        String material = cursor.getString(7);
        String assessment = cursor.getString(8);
        String process = cursor.getString(9);
        String instructions = cursor.getString(10);
        String result = cursor.getString(12);

        Data data = new Data(no, name, level, dev_domain, objectives, key_dev, material, assessment, process, instructions);
        List<Data> dataList = new ArrayList<>();
        dataList.add(data);
        return new ActivityData(status, dataList, result);
    }

    public int insertRecord(ActivityData activityData) {
        if (activityData == null || activityData.getData() == null || activityData.getData().isEmpty()) {
            return -1;
        }
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", activityData.getStatus());
        contentValues.put("result", activityData.getResult());
        Data data = activityData.getData().get(0);
        contentValues.put("id", data.getActivityNo());
        contentValues.put("name", data.getActivityName());
        contentValues.put("level", data.getActivityLevel());
        contentValues.put("dev_domain", data.getActivityDevDomain());
        contentValues.put("objectives", data.getActivityObjectives());
        contentValues.put("key_dev", data.getActivityKeyDev());
        contentValues.put("material", data.getActivityMaterial());
        contentValues.put("assessment", data.getActivityAssessment());
        contentValues.put("process", data.getActivityProcess());
        contentValues.put("instructions", data.getActivityInstructions());
        contentValues.put("complete_status", "Pending");

        try {
            return (int) db.insertOrThrow("ActivityData", null, contentValues);
        } catch (SQLiteConstraintException e) {
            Log.e(TAG, "insertRecord error", e);
            return -2;
        } catch (Exception e) {
            Log.e(TAG, "insertRecord unknown error", e);
            return -1;
        }
    }

    public int isNewUser() {
        SQLiteDatabase db = getReadableDatabase();
        int count = 0;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT COUNT(*) FROM ActivityData", null);
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e(TAG, "isNewUser error", e);
        } finally {
            if (cursor != null) cursor.close();
        }
        Log.d(TAG, "isNewUser: " + count);
        return count;
    }
}
