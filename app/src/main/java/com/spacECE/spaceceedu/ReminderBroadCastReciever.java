package com.spacECE.spaceceedu;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.spacECE.spaceceedu.Utils.UsefulFunctions;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReminderBroadCastReciever extends BroadcastReceiver {

    private static final String TAG = "ReminderBroadCastReceiver";
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public void onReceive(Context context, Intent intent) {
        // Move all work to background to avoid blocking main thread (potential ANR or System UI lag)
        executor.execute(() -> {
            DBController dbController = DBController.getInstance(context);
            ActivityData lastActivity = dbController.getLastActivity();

            if (lastActivity == null || lastActivity.getData() == null || lastActivity.getData().isEmpty()) {
                Log.d(TAG, "onReceive: No valid activity data found, skipping notification.");
                // Even if no data, we might want to fetch one for next time
                int dayNo = dbController.isNewUser() + 1;
                runGetRecentActivityTask(context, dayNo);
                return;
            }

            int dayNo = dbController.isNewUser() + 1;
            Log.d(TAG, "onReceive: day" + dayNo);
            runGetRecentActivityTask(context, dayNo);

            showNotification(context, lastActivity);
        });
    }

    private void showNotification(Context context, ActivityData lastActivity) {
        String activityNo = lastActivity.getData().get(0).getActivityNo();
        String activityName = lastActivity.getData().get(0).getActivityName();

        Intent repeatingIntent = new Intent(context, ActivityDetailsActivity.class);
        repeatingIntent.putExtra("EXTRA_ACTIVITY", lastActivity);
        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 200, repeatingIntent, flags);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notify")
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pendingIntent)
                .setContentTitle("SpaceActive - Activity " + activityNo)
                .setContentText(activityName)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        try {
            notificationManagerCompat.notify(200, builder.build());
            Log.d(TAG, "onReceive: notification sent");
        } catch (SecurityException e) {
            Log.e(TAG, "Notification permission missing", e);
        }
    }

    private void runGetRecentActivityTask(Context context, int dayNo) {
        try {
            // Note: Using https and the correct domain to avoid UnknownHostException
            JSONObject apiCall = UsefulFunctions.UsingGetAPI("https://hustle-7c68d043.mileswebhosting.com/spacece/api/spaceactive_activities.php?ano=" + dayNo);
            Log.d(TAG, "Object Obtained " + (apiCall != null ? apiCall.toString() : "null"));

            if (apiCall != null) {
                Gson gson = new GsonBuilder().create();
                ActivityData activityData = gson.fromJson(apiCall.toString(), ActivityData.class);
                if (activityData != null && activityData.getData() != null && !activityData.getData().isEmpty()) {
                    DBController dbController = DBController.getInstance(context);
                    dbController.insertRecord(activityData);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching recent activity", e);
        }
    }
}
