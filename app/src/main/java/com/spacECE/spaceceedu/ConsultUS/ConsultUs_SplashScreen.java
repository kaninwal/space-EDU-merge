package com.spacECE.spaceceedu.ConsultUS;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.JsonObject;
import com.spacECE.spaceceedu.MainActivity;
import com.spacECE.spaceceedu.R;
import com.spacECE.spaceceedu.api.ApiClient;
import com.spacECE.spaceceedu.api.ApiService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsultUs_SplashScreen extends AppCompatActivity {

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        apiService = ApiClient.getClient().create(ApiService.class);
        loadCategories();
    }

    private void loadCategories() {
        apiService.getCategories("all").enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        Consultant_Main.categoryList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ConsultantCategory newCategory = new ConsultantCategory((String) jsonArray.get(i), "nice");
                            Consultant_Main.categoryList.add(newCategory);
                        }
                        Intent intent = new Intent(ConsultUs_SplashScreen.this, Consultant_Main.class);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showErrorDialog();
                    }
                } else {
                    showErrorDialog();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("API_CALL", "Failed", t);
                showErrorDialog();
            }
        });
    }

    private void showErrorDialog() {
        new AlertDialog.Builder(ConsultUs_SplashScreen.this)
                .setTitle("Failed to load data")
                .setMessage("There was an error fetching data from the server. Please check your internet connection and try again.")
                .setPositiveButton("Retry", (dialog, which) -> loadCategories())
                .setNegativeButton("Cancel", (dialog, which) -> {
                    Intent intent = new Intent(ConsultUs_SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}