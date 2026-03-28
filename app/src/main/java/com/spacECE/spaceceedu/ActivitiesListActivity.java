package com.spacECE.spaceceedu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.spacECE.spaceceedu.api.ApiClient;
import com.spacECE.spaceceedu.api.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitiesListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private DBController dbController;
    List<ActivityData> activityDataList = new ArrayList<>();
    ListView listViewActivityData;
    ActivityAdapter activityAdapter;
    final static String TAG = "ActivitiesListActivity";
    private ApiService apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_list);

        dbController = new DBController(this);
        apiService = ApiClient.getClient().create(ApiService.class);
        listViewActivityData = findViewById(R.id.list_activity);

        setDataFromSQLite();
        fetchUsingRetrofit();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ActivityData activityData = (ActivityData) activityAdapter.getItem(position);
        Intent intent = new Intent(getApplicationContext(), ActivityDetailsActivity.class);
        intent.putExtra("EXTRA_ACTIVITY", activityData);
        startActivity(intent);
    }

    public void fetchUsingRetrofit(){
        apiService.getActivities().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ActivitiesListActivity.this, "Fetched", Toast.LENGTH_SHORT).show();

                    // The API returns a JsonObject, we need to parse it to ActivityData or list
                    // Based on previous code, let's assume it's a list or similar structure
                    // For now, let's try to update the list if possible.
                    // This part depends on the exact JSON structure.
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void setDataFromSQLite(){
        activityDataList = dbController.getAll();
        if(activityDataList != null && !activityDataList.isEmpty()) {
            activityAdapter = new ActivityAdapter(this, activityDataList);
            listViewActivityData.setAdapter(activityAdapter);
            listViewActivityData.setOnItemClickListener(this);
        }
    }
}