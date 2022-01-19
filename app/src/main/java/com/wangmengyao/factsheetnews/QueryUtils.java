package com.wangmengyao.factsheetnews;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getName();

    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 10000;
    private static final String REQUEST_METHOD = "GET";

    private QueryUtils() {
    }

    public static List<News> fetchNewsDate(String requestUrl) {
        Log.i(LOG_TAG, "Info: QueryUtils - fetchNewsDate()");

        // Create url object.
        URL url = createUri(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpResponse(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error: fail to close InputStream.", e);
        }

        return extractFeatureFromJson(jsonResponse);
    }

    private static List<News> extractFeatureFromJson(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        List<News> newsList = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONObject response = root.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject currentNews = results.getJSONObject(i);

                String topic = currentNews.getString("type");
                String origin = currentNews.getString("sectionName");
                String date = currentNews.getString("webPublicationDate");
                String title = currentNews.getString("webTitle");
                String url = currentNews.getString("webUrl");

                News news = new News(title, topic, date, origin, url);
                newsList.add(news);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error: the parse fails or doesn't yield a JSONObject.", e);
        }
        return newsList;
    }

    private static String makeHttpResponse(URL url) throws IOException {
        if (url == null) {
            return null;
        }

        String jsonResponse = null;
        HttpsURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setRequestMethod(REQUEST_METHOD);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                //不能用getResponseMessage()
                // 这个方法返回的是
                // HTTP/1.0 200 OK
                // HTTP/1.0 404 Not Found
//                jsonResponse = urlConnection.getResponseMessage();
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error: response code = " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error: fail to retrieve JSON response.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    private static URL createUri(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating url", e);
        }
        return url;
    }
}
