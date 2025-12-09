package com.spacECE.spaceceedu;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.spacECE.spaceceedu.Utils.UsefulFunctions;
import org.json.JSONObject;

public class ReminderBroadCastReciever extends BroadcastReceiver {

    final String TAG = "ReminderBroadCastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        DBController dbController = new DBController(context);
        ActivityData lastActivity = dbController.getLastActivity();

        // More robust check: ensure lastActivity and its data are not null or empty
        if (lastActivity == null || lastActivity.getData() == null || lastActivity.getData().isEmpty()) {
            Log.d(TAG, "onReceive: No valid activity data found, skipping notification.");
            return;
        }

        int dayNo = dbController.isNewUser();
        dayNo++;

        Log.d(TAG, "onReceive: day" + dayNo);
        GetRecentActivity getRecentActivity = new GetRecentActivity(dayNo);
        getRecentActivity.execute();

        String activityNo = lastActivity.getData().get(0).getActivityNo();
        String activityName = lastActivity.getData().get(0).getActivityName();

        Intent repeatingIntent = new Intent(context, ActivityDetailsActivity.class);
        repeatingIntent.putExtra("EXTRA_ACTIVITY", lastActivity);
        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        int flags = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            flags = PendingIntent.FLAG_IMMUTABLE;
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 200, repeatingIntent, flags);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notify")
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pendingIntent)
                .setContentTitle("SpaceActive - Activity " + activityNo)
                .setContentText(activityName)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(200, builder.build());
        Log.d(TAG, "onReceive:notification sent ");
    }

    class GetRecentActivity extends AsyncTask<String, Void, JSONObject> {

        final private JSONObject[] apiCall = {null};
        int dayNo;

        GetRecentActivity(int dayNo) {
            this.dayNo = dayNo;
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            try {
                apiCall[0] = UsefulFunctions.UsingGetAPI("http://educationfoundation.space/spacece/api/spaceactive_activities.php?ano=" + dayNo);
                Log.d(TAG, "Object Obtained " + (apiCall[0] != null ? apiCall[0].toString() : "null"));

                if (apiCall[0] != null) {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    ActivityData activityData = gson.fromJson(apiCall[0].toString(), ActivityData.class);
                    if (activityData != null && activityData.getData() != null && !activityData.getData().isEmpty()) {
                        ActivitiesListActivity.InsertDataIntoSqlite(activityData);
                    }
                }
            } catch (RuntimeException runtimeException) {
                Log.d(TAG, "RUNTIME EXCEPTION:::, Server did not respond");
            }
            return null;
        }
    }
}
