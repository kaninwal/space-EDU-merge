package com.spacECE.spaceceedu.VideoLibrary;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.spacECE.spaceceedu.Authentication.UserLocalStore;
import com.spacECE.spaceceedu.R;
import com.spacECE.spaceceedu.Utils.UsefulFunctions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class VideoLibrary_Activity extends AppCompatActivity {

    public final static boolean[] ArrayDownloadCOMPLETED = {false};
    public static ArrayList<Topic> topicList = new ArrayList<>();
    public static ArrayList<Topic> recentTopicList = new ArrayList<>();
    public static ArrayList<Topic> trendingTopicList = new ArrayList<>();
    public static ArrayList<Topic> paidTopicList = new ArrayList<>();
    public static ArrayList<Topic> freeTopicList = new ArrayList<>();
    
    UserLocalStore userLocalStore;

    NavigationBarView.OnItemSelectedListener VL_navListener =
            new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    int itemId = item.getItemId();
                    if (itemId == R.id.videolibrary_nav_free) {
                        selectedFragment = new VideoLibrary_Free();
                    } else if (itemId == R.id.videolibrary_nav_paid) {
                        selectedFragment = new VideoLibrary_Premium();
                    } else if (itemId == R.id.videolibrary_nav_trending) {
                        selectedFragment = new VideoLibrary_trending_Fragment();
                    }

                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.VideoLibrary_Fragment_layout,
                                selectedFragment).commit();
                    }
                    return true;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_library);

        userLocalStore = new UserLocalStore(getApplicationContext());
        fetchDataInParallel();

        BottomNavigationView videoLibraryBottomNav = findViewById(R.id.VideoLibrary_Bottom_Navigation);
        videoLibraryBottomNav.setOnItemSelectedListener(VL_navListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.VideoLibrary_Fragment_layout,
                    new VideoLibrary_Free()).commit();
        }
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

    private void fetchDataInParallel() {
        CompletableFuture<Void> futureAll = CompletableFuture.runAsync(() -> fetchVideos("SPACETUBE_ALL", topicList));

        CompletableFuture.allOf(futureAll).thenRun(() -> {
            ArrayDownloadCOMPLETED[0] = true;
            Log.i("VideoLibrary_Activity", "All data downloaded. Topic list size: " + topicList.size());
            
            // Re-sync Fragments if already attached
            runOnUiThread(() -> {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.VideoLibrary_Fragment_layout);
                if (currentFragment instanceof VideoLibrary_Free) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.VideoLibrary_Fragment_layout, new VideoLibrary_Free()).commitAllowingStateLoss();
                }
            });
        });
    }

    private void fetchVideos(String urlKey, ArrayList<Topic> topicList) {
        try {
            JSONObject config = loadConfig(getApplicationContext());
            
            String accountId = "1";
            if (userLocalStore != null && userLocalStore.getLoggedInAccount() != null) {
                accountId = userLocalStore.getLoggedInAccount().getAccount_id();
            }

            if (config != null) {
                String baseUrl = config.getString("BASE_URL");
                String apiUrl = config.getString(urlKey);
                JSONObject apiCall = UsefulFunctions.UsingGetAPI(baseUrl + apiUrl + "?uid=" + accountId + "&type=all");
                if (apiCall != null) {
                    JSONArray jsonArray = apiCall.optJSONArray("data");

                    if (jsonArray != null) {
                        ArrayList<Topic> newTopics = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject response_element = jsonArray.getJSONObject(i);
                            Topic newTopic = new Topic(response_element.optString("status"),
                                    response_element.optString("title"),
                                    response_element.optString("v_id"),
                                    response_element.optString("filter"),
                                    response_element.optString("length"),
                                    response_element.optString("v_url"),
                                    response_element.optString("v_date"),
                                    response_element.optString("v_uni_no"),
                                    response_element.optString("v_desc", response_element.optString("desc")),
                                    response_element.optString("cntlike"),
                                    response_element.optString("cntdislike"),
                                    response_element.optString("views"),
                                    response_element.optString("cntcomment"));
                            newTopics.add(newTopic);
                        }
                        
                        synchronized (VideoLibrary_Activity.topicList) {
                            VideoLibrary_Activity.topicList.clear();
                            VideoLibrary_Activity.topicList.addAll(newTopics);
                        }
                        
                        // Populate sub-lists
                        synchronized (VideoLibrary_Activity.paidTopicList) {
                            VideoLibrary_Activity.paidTopicList.clear();
                            for (Topic t : newTopics) {
                                if (t.getStatus() != null && t.getStatus().equalsIgnoreCase("created")) VideoLibrary_Activity.paidTopicList.add(t);
                            }
                        }
                        synchronized (VideoLibrary_Activity.freeTopicList) {
                            VideoLibrary_Activity.freeTopicList.clear();
                            for (Topic t : newTopics) {
                                if (t.getStatus() == null || !t.getStatus().equalsIgnoreCase("created")) VideoLibrary_Activity.freeTopicList.add(t);
                            }
                        }
                        
                        // Trending/Recent handled by Splash but also synced here for safety
                        if (apiCall.has("data_recent")) {
                            JSONArray recentArr = apiCall.getJSONArray("data_recent");
                            synchronized (VideoLibrary_Activity.recentTopicList) {
                                VideoLibrary_Activity.recentTopicList.clear();
                                for (int i = 0; i < recentArr.length(); i++) {
                                    VideoLibrary_Activity.recentTopicList.add(parseTopic(recentArr.getJSONObject(i)));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("VideoLibrary_Activity", "Fetch Error: " + e.getMessage());
        }
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
}
