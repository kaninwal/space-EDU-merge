package com.spacECE.spaceceedu.LibForSmall;

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
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class library_splash_screen extends AppCompatActivity {

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_splash_screen);
        apiService = ApiClient.getClient().create(ApiService.class);
        loadProducts();
    }

    private void loadProducts() {
        apiService.getAllProducts().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        Library_main.list = new ArrayList<>();
                        for (int i = 0; i < Objects.requireNonNull(jsonArray).length(); i++) {
                            JSONObject response_element = new JSONObject(String.valueOf(jsonArray.getJSONObject(i)));
                            books temp = new books(response_element.getString("product_id"), response_element.getString("product_title"),
                                    response_element.getString("product_price"), response_element.getString("product_keywords"),
                                    response_element.getString("product_image"), response_element.getString("product_desc"),
                                    response_element.getString("product_brand"), response_element.getString("rent_price"),
                                    response_element.getString("exchange_price"), response_element.getString("deposit"));
                            Library_main.list.add(temp);
                        }
                        Intent intent = new Intent(library_splash_screen.this, Library_main.class);
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
        new AlertDialog.Builder(library_splash_screen.this)
                .setTitle("Failed to load data")
                .setMessage("There was an error fetching data from the server. Please check your internet connection and try again.")
                .setPositiveButton("Retry", (dialog, which) -> loadProducts())
                .setNegativeButton("Cancel", (dialog, which) -> {
                    Intent intent = new Intent(library_splash_screen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}