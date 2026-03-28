package com.spacECE.spaceceedu.LearnOnApp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.spacECE.spaceceedu.MainActivity;
import com.spacECE.spaceceedu.R;
import com.spacECE.spaceceedu.Utils.UsefulFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class LearnOn_Main extends AppCompatActivity {

    public static ArrayList<Learn> Llist = new ArrayList<>();
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_on_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.LearnOnMain_Frame, new LearnOn_List()).commit();
        }

        bottomNavigationView=findViewById(R.id.bottom_navigation_learn);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.allCourse) {
                getSupportFragmentManager().beginTransaction().replace(R.id.LearnOnMain_Frame, new LearnOn_List()).commit();
                return true;
            } else if (itemId == R.id.myCourse) {
                // Placeholder for My Courses - typically filtered list or separate fragment
                Toast.makeText(getApplicationContext(), "My Courses feature coming soon!", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

    }


}
