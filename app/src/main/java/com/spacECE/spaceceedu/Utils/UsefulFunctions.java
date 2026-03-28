package com.spacECE.spaceceedu.Utils;

import android.os.Looper;
import android.util.Log;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class UsefulFunctions {

    private static OkHttpClient client;

    public static synchronized OkHttpClient getOkHttpClient() {
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();
        }
        return client;
    }

    public static String cleanJsonResponse(String response) {
        if (response == null) return null;
        String clean = response.trim();
        int jsonStart = clean.indexOf("{");
        int jsonEnd = clean.lastIndexOf("}");
        if (jsonStart != -1 && jsonEnd != -1 && jsonEnd > jsonStart) {
            return clean.substring(jsonStart, jsonEnd + 1);
        }
        return clean;
    }

    public static JSONObject UsingGetAPI(String inputURL) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Log.e("UsefulFunctions", "NETWORK CALL ON MAIN THREAD: " + inputURL);
        }
        
        // Normalize URL to use the working domain and https
        String finalUrl = inputURL;
        if (inputURL.contains("educationfoundation.space") || inputURL.contains("spacefoundation.in")) {
            finalUrl = inputURL.replace("http://educationfoundation.space", "https://hustle-7c68d043.mileswebhosting.com")
                               .replace("http://spacefoundation.in", "https://hustle-7c68d043.mileswebhosting.com")
                               .replace("http://", "https://");
        }

        Log.d("UsefulFunctions", "Requesting: " + finalUrl);
        JSONObject jsonObject = null;

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        try (Response response = getOkHttpClient().newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String resp = cleanJsonResponse(response.body().string());
                jsonObject = new JSONObject(resp);
            } else {
                Log.e("UsefulFunctions", "Response Unsuccessful: " + response.code());
            }
        } catch (IOException | JSONException e) {
            Log.e("UsefulFunctions", "API Error: " + e.getMessage());
        }

        return jsonObject;
    }

    public static class DateFunc {

        public static Date StringToDate(String date) throws ParseException {
            if (date == null || date.isEmpty()) return new Date();
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        }

        public static Date StringToTime(String date) throws ParseException {
            if (date == null || date.isEmpty()) return new Date();
            return new SimpleDateFormat("HH:mm:ss").parse(date);
        }

        public static String DateObjectToDate(Date date) {
            if (date == null) return "";
            return new SimpleDateFormat("MMM/dd").format(date);
        }

        public static String DateObjectToTime(Date date) {
            if (date == null) return "";
            return new SimpleDateFormat("HH:mm").format(date);
        }

        public static String DateObjectToDay(Date date) {
            if (date == null) return "";
            return new SimpleDateFormat("EEEE").format(date);
        }
    }
}
