package com.spacECE.spaceceedu.VideoLibrary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.spacECE.spaceceedu.MainActivity;
import com.spacECE.spaceceedu.R;
import com.spacECE.spaceceedu.api.ApiClient;
import com.spacECE.spaceceedu.api.ApiService;

import java.net.URLEncoder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopicActivity extends AppCompatActivity {

    private YouTubePlayerView youTubePlayerView;

    private Button b_likeVideo;
    private Button b_dislikeVideo;
    private Button b_share;
    private Button b_comment;

    private TextView tv_title;
    private TextView tv_like;
    private TextView tv_dislike;
    private TextView tv_views;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        apiService = ApiClient.getClient().create(ApiService.class);

        TextView discrip_view = findViewById(R.id.Topic_TextView_Description);
        b_comment = findViewById(R.id.Topics_Button_Comment);
        b_share = findViewById(R.id.Topic_Button_Share);
        b_likeVideo = findViewById(R.id.Topic_Button_LikeVideo);
        b_dislikeVideo = findViewById(R.id.Topic_Button_DislikeVideo);
        tv_dislike = findViewById(R.id.Topic_TextView_dislikeCount);
        tv_like = findViewById(R.id.Topic_TextView_likeCount);
        tv_views = findViewById(R.id.Topic_TextView_viewCount);
        tv_title = findViewById(R.id.Topic_TextView_Title);

        String name = "No topic";
        String discription = "No ID";
        String v_url = "Video ID missing";
        String v_id = "Unknown";
        String like_count = "Unknown";
        String dislike_count = "Unknown";
        String views = "unknown";

        //Getting Values from prev activity:
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("topic_name");
            discription = extras.getString("discrp");
            v_url = extras.getString("v_url");
            v_id = extras.getString("v_id");
            like_count = extras.getString("like_count");
            dislike_count = extras.getString("dislike_count");
            views = extras.getString("views");
        }

        discrip_view.setText(discription);
        tv_like.setText(like_count + " Likes");
        tv_dislike.setText(dislike_count + " Dislikes");
        tv_views.setText(views + " Views");
        tv_title.setText(name);

        //YouTube VideoPLayer:
        youTubePlayerView = findViewById(R.id.YoutubePlayerView);
        getLifecycle().addObserver(youTubePlayerView);

        String finalV_url = v_url;
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(finalV_url, 0);
            }
        });

        String finalV_id = v_id;
        b_likeVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.ACCOUNT != null) {
                    apiService.likeVideo(MainActivity.ACCOUNT.getuId(), finalV_id).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Liked", Toast.LENGTH_SHORT).show();
                                Log.i("API_CALL_LIKE", response.toString());
                            } else {
                                Toast.makeText(getApplicationContext(), "Already Liked", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Like Failed", Toast.LENGTH_SHORT).show();
                            Log.e("API_CALL_LIKE", "Failed", t);
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Please Login to like the video", Toast.LENGTH_SHORT).show();
                }
            }
        });

        b_dislikeVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.ACCOUNT != null) {
                    apiService.dislikeVideo(finalV_id, MainActivity.ACCOUNT.getuId()).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Disliked", Toast.LENGTH_SHORT).show();
                                Log.i("API_CALL_DISLIKE", response.toString());
                            } else {
                                Toast.makeText(getApplicationContext(), "Already Disliked", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Dislike Failed", Toast.LENGTH_SHORT).show();
                            Log.e("API_CALL_DISLIKE", "Failed", t);
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Please Login to dislike the video", Toast.LENGTH_SHORT).show();
                }

            }
        });

        String finalName = name;
        b_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                myIntent.putExtra(Intent.EXTRA_SUBJECT, "SpaceTube");
                myIntent.putExtra(Intent.EXTRA_TEXT, "Hey!, check this out this video on " + finalName + " by SpacECE: https://www.youtube.com/watch?v=" + finalV_url);
                startActivity(Intent.createChooser(myIntent, "Share Using"));
            }
        });

        b_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.ACCOUNT != null) {
                    EditText commentText = findViewById(R.id.Topic_EditText_Comment);
                    String comment = commentText.getText().toString();
                    if (!comment.isEmpty()) {
                        try {
                            String encodedComment = URLEncoder.encode(comment, "UTF-8");
                            apiService.commentOnVideo(MainActivity.ACCOUNT.getuId(), finalV_id, encodedComment).enqueue(new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Comment Posted", Toast.LENGTH_SHORT).show();
                                        commentText.setText("");
                                        Log.i("API_CALL_COMMENT", response.toString());
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Comment Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<JsonObject> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(), "Comment Failed", Toast.LENGTH_SHORT).show();
                                    Log.e("API_CALL_COMMENT", "Failed", t);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please Login to comment", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
