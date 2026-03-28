package com.spacECE.spaceceedu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.spacECE.spaceceedu.R;
import com.spacECE.spaceceedu.VideoLibrary.VideoLibrary_Activity;

import org.json.JSONObject;

import java.io.InputStream;

public class SpaceTubeLoaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Intent intent = new Intent(getApplicationContext(), VideoLibrary_Activity.class);
        startActivity(intent);
        finish();
    }

    public static JSONObject loadConfig(Context context) {
        try {
            InputStream is = context.getAssets().open("config.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            return new JSONObject(json);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
