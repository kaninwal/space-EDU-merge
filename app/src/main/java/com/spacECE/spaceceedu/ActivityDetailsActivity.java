package com.spacECE.spaceceedu;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityDetailsActivity extends AppCompatActivity {

    private TextView textViewTitle, textViewDesc, textViewDay;
    private ImageView imageView;
    private Button buttonMarkAsRead;

    final static String TAG = "ActivityDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        textViewTitle = findViewById(R.id.textView_activity_title);
        textViewDesc = findViewById(R.id.textView_activity_desc);
        textViewDay = findViewById(R.id.textView_activity_day);

        ActivityData activityData;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activityData = getIntent().getSerializableExtra("EXTRA_ACTIVITY", ActivityData.class);
        } else {
            activityData = (ActivityData) getIntent().getSerializableExtra("EXTRA_ACTIVITY");
        }

        if(activityData != null) {

            Log.d(TAG, "onCreate: "+activityData.getData().get(0).getActivityDevDomain());
            textViewTitle.setText(activityData.getData().get(0).getActivityName());
            textViewDesc.setText(activityData.getData().get(0).getActivityObjectives());
            textViewDay.setText("Activity No : "+activityData.getData().get(0).getActivityNo());
        } else {
            Log.d(TAG, "onCreate: activityData is null");
        }

    }
}