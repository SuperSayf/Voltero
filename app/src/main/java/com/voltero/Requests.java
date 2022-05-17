package com.voltero;

import android.app.Activity;
import android.content.ContentValues;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Requests
{
    final private static String ERROR_MSG = "Unable to connect to server";
    final private static String URL_BASE = "https://lamp.ms.wits.ac.za/~s2430888/"; // All scripts are accessed from this base URL

    public static void request(Activity activity, String filename, ContentValues params, RequestHandler handler)
    {
        HttpUrl.Builder URL = Objects.requireNonNull(HttpUrl.parse(URL_BASE + filename + ".php")).newBuilder();

        for (String key : params.keySet()) // For each query
            URL.addQueryParameter(key, params.getAsString(key)); // Adding query

        String strURL = URL.build().toString();
        Request req = new Request.Builder().url(strURL).build();
        final OkHttpClient client = new OkHttpClient();

        client.newCall(req).enqueue(new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                Toast.makeText(activity, ERROR_MSG, Toast.LENGTH_SHORT).show(); // Show error message
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                try
                {
                    handler.processResponse(Objects.requireNonNull(response.body()).string());
                } catch (JSONException ignored) {}
            }
        });
    }

    public static void showMessage(Activity activity, String message)
    {
        activity.runOnUiThread(() -> Toast.makeText(activity, message, Toast.LENGTH_SHORT).show());
    }
}
