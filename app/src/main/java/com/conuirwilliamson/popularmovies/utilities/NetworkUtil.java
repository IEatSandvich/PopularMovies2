package com.conuirwilliamson.popularmovies.utilities;

import java.io.IOException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by conuirwilliamson on 29/03/2017.
 */

public class NetworkUtil {

    private static final OkHttpClient client = new OkHttpClient();

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        return client.newCall(request).execute().body().string();
    }
}

