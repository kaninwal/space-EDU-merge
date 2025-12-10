package com.spacECE.spaceceedu.LibForSmall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.spacECE.spaceceedu.R;

import java.util.ArrayList;

public class Library_main extends AppCompatActivity {

    public static ArrayList<books> list = new ArrayList<>();

    BottomNavigationView bottomNavigationView;

    Allbooks_fragment allbooks_fragment = new Allbooks_fragment();
    Mybooks_fragment mybooks_fragment = new Mybooks_fragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,allbooks_fragment).commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.allbooks) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, allbooks_fragment).commit();
                return true;
            } else if (itemId == R.id.mybooks) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, mybooks_fragment).commit();
                return true;
            } else if (itemId == R.id.chat) {
                startActivity(new Intent(getApplicationContext(), ChatUS.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });

    }
}