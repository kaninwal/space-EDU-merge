package com.spacECE.spaceceedu.LibForSmall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.spacECE.spaceceedu.R;

import java.util.ArrayList;

public class Library_main extends AppCompatActivity {

    public static ArrayList<books> list = new ArrayList<>();

    BottomNavigationView bottomNavigationView;
    FloatingActionButton fabAddBook;

    Fragment allbooks_fragment = new library_list();
    Fragment mybooks_fragment = new library_myBooks_list();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_main);

        bottomNavigationView = findViewById(R.id.bottomAppBar);
        fabAddBook = findViewById(R.id.floatingActionBtnBottom);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.book_framelayout, allbooks_fragment).commit();
        }

        fabAddBook.setOnClickListener(v -> {
            Intent intent = new Intent(Library_main.this, AddBook.class);
            startActivity(intent);
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menuHome) {
                getSupportFragmentManager().beginTransaction().replace(R.id.book_framelayout, allbooks_fragment).commit();
                return true;
            } else if (itemId == R.id.menuBook) {
                getSupportFragmentManager().beginTransaction().replace(R.id.book_framelayout, mybooks_fragment).commit();
                return true;
            } else if (itemId == R.id.menuChat) {
                startActivity(new Intent(getApplicationContext(), ChatUS.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });

    }
}