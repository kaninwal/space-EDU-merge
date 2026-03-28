package com.spacECE.spaceceedu.VideoLibrary;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.spacECE.spaceceedu.Authentication.UserLocalStore;
import com.spacECE.spaceceedu.MainActivity;
import com.spacECE.spaceceedu.R;
import com.spacECE.spaceceedu.Utils.UsefulFunctions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VideoLibrary_Activity_SplashScreen extends AppCompatActivity {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        userLocalStore = new UserLocalStore(getApplicationContext());
        loadVideoData();
    }

    private void loadVideoData() {
        executor.execute(() -> {
            try {
                JSONObject config = VideoLibrary_Activity.loadConfig(getApplicationContext());
                String baseUrl = "https://hustle-7c68d043.mileswebhosting.com/spacece/";
                String apiUrl = "SpacTube/api_all.php";
                
                if (config != null) {
                    baseUrl = config.optString("BASE_URL", baseUrl);
                    apiUrl = config.optString("SPACETUBE_ALL", apiUrl);
                }

                String accountId = "1";
                if (userLocalStore != null && userLocalStore.getLoggedInAccount() != null) {
                    accountId = userLocalStore.getLoggedInAccount().getAccount_id();
                }
                String encodedId = URLEncoder.encode(accountId, "UTF-8");

                String url = baseUrl + apiUrl + "?uid=" + encodedId + "&type=all";
                Log.d("VideoSplash", "Fetching from: " + url);
                
                JSONObject apiCall = UsefulFunctions.UsingGetAPI(url);

                if (apiCall != null && (apiCall.has("data") || apiCall.has("status"))) {
                    parseVideoData(apiCall);
                    runOnUiThread(() -> {
                        VideoLibrary_Activity.ArrayDownloadCOMPLETED[0] = true;
                        Intent intent = new Intent(getApplicationContext(), VideoLibrary_Activity.class);
                        startActivity(intent);
                        finish();
                    });
                } else {
                    Log.e("VideoSplash", "API returned null or no data. Response: " + (apiCall != null ? apiCall.toString() : "null"));
                    runOnUiThread(this::showErrorDialog);
                }
            } catch (Exception e) {
                Log.e("VideoSplash", "Error loading data", e);
                runOnUiThread(this::showErrorDialog);
            }
        });
    }

    private void parseVideoData(JSONObject apiCall) throws JSONException {
        JSONArray jsonArray = apiCall.optJSONArray("data");
        if (jsonArray == null) jsonArray = new JSONArray();
        
        ArrayList<Topic> allTopics = new ArrayList<>();
        ArrayList<Topic> paidTopics = new ArrayList<>();
        ArrayList<Topic> freeTopics = new ArrayList<>();
        ArrayList<Topic> recentTopics = new ArrayList<>();
        ArrayList<Topic> trendingTopics = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            Topic newTopic = parseTopic(jsonArray.getJSONObject(i));
            allTopics.add(newTopic);
            if (newTopic.getStatus() != null && newTopic.getStatus().equalsIgnoreCase("created")) {
                paidTopics.add(newTopic);
            } else {
                freeTopics.add(newTopic);
            }
        }

        if (apiCall.has("data_recent")) {
            JSONArray recentJsonArray = apiCall.optJSONArray("data_recent");
            if (recentJsonArray != null) {
                for (int i = 0; i < recentJsonArray.length(); i++) {
                    recentTopics.add(parseTopic(recentJsonArray.getJSONObject(i)));
                }
            }
        }

        if (apiCall.has("data_trending")) {
            JSONArray trendingJsonArray = apiCall.optJSONArray("data_trending");
            if (trendingJsonArray != null) {
                for (int i = 0; i < trendingJsonArray.length(); i++) {
                    trendingTopics.add(parseTopic(trendingJsonArray.getJSONObject(i)));
                }
            }
        }

        // Update static lists safely
        VideoLibrary_Activity.topicList = allTopics;
        VideoLibrary_Activity.paidTopicList = paidTopics;
        VideoLibrary_Activity.freeTopicList = freeTopics;
        VideoLibrary_Activity.recentTopicList = recentTopics;
        VideoLibrary_Activity.trendingTopicList = trendingTopics;
    }

    private Topic parseTopic(JSONObject obj) {
        return new Topic(obj.optString("status"), obj.optString("title"),
                obj.optString("v_id"), obj.optString("filter"),
                obj.optString("length"), obj.optString("v_url"),
                obj.optString("v_date"), obj.optString("v_uni_no"),
                obj.optString("v_desc", obj.optString("desc")),
                obj.optString("cntlike"),
                obj.optString("cntdislike"), obj.optString("views"),
                obj.optString("cntcomment"));
    }

    private void showErrorDialog() {
        if (isFinishing()) return;
        new AlertDialog.Builder(this)
                .setTitle("Load Error")
                .setMessage("Unable to fetch video data. Please check your connection.")
                .setPositiveButton("Retry", (dialog, which) -> loadVideoData())
                .setNegativeButton("Cancel", (dialog, which) -> {
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                })
                .setCancelable(false)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy() ;
        executor.shutdown();
    }
}
