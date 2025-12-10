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
    Fragment fragment = new LearnOn_List();
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_on_main);

        // Populate Dummy Data to avoid empty list crash/blank screen
        if(Llist.isEmpty()) {
            // Updated constructor calls to match Learn class: 
            // id, title, description, type, mode, duration, price
            Llist.add(new Learn("1", "Parenting", "Learn basics of parenting", "Paid", "Online", "4 Weeks", "1000"));
            Llist.add(new Learn("2", "Child Care", "Learn basics of child care", "Free", "Offline", "2 Days", "0"));
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.LearnOnMain_Frame, fragment).commit();

//        BottomNavigationView
        bottomNavigationView=findViewById(R.id.bottom_navigation_learn);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.allCourse) {
//                          You Just Attach Here Fragment Manager Here Of All Course
//                            getSupportFragmentManager().beginTransaction().replace(R.id.LearnOnMain_Frame, fragment).commit();
                    Toast.makeText(getApplicationContext(), "All Course", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.myCourse) {
//                          You just Attach Here Fragment Manager Here Of My Course
//                            getSupportFragmentManager().beginTransaction().replace(R.id.LearnOnMain_Frame, fragment).commit();
                    Toast.makeText(getApplicationContext(), "My Course", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

    }


}
