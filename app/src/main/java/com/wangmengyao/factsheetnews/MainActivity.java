package com.wangmengyao.factsheetnews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final String LOG_TAG = MainActivity.class.getName();

    private static final String NEWS_REQUEST_URL = "https://content.guardianapis.com/search?q=debates&api-key=test";

    private static final int NEWS_LOADER_ID = 1;

    private NewsAdapter mNewsAdapter;

    private View mProgressBar;

    private TextView mEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "Info: MainActivity - onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmptyTextView = findViewById(R.id.empty_text_view);
        mProgressBar = findViewById(R.id.progress_bar);
        ListView newsListView = (ListView) findViewById(R.id.news_list);
        newsListView.setEmptyView(mEmptyTextView);

        mNewsAdapter = new NewsAdapter(this, new ArrayList<News>());
        newsListView.setAdapter(mNewsAdapter);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                News currentNews = mNewsAdapter.getItem(position);

                Uri newsUri = Uri.parse(currentNews.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                startActivity(websiteIntent);
            }
        });

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();

            Log.i(LOG_TAG, "Info: loaderManager.initLoader.");
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mEmptyTextView.setText(R.string.no_internet_connection);
        }
    }

    /**
     * ???????????? onCreateLoader()????????????LoaderManager??????????????????????????????ID???loader???????????????,???????????????????????????
     *
     * @param id   LoaderManager??????Id???loader???NEWS_LOADER_ID = 1
     * @param args ???????????????
     * @return ???????????? List<News>??? Loader
     */
    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "Info: onCreateLoader().");

        return new NewsLoader(this, NEWS_REQUEST_URL);
    }

    /**
     * ???????????? onLoadFinished(), ?????????????????????????????? AsyncTask??? onPostExecute()?????????????????????????????????
     * ?????????????????????????????????UI,???????????????????????????????????????
     *
     * @param loader   ?????????
     * @param newsList ?????????List<News>???????????????
     */
    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsList) {
        Log.i(LOG_TAG, "Info: onLoadFinished().");

        mProgressBar.setVisibility(View.GONE);
        mEmptyTextView.setText(R.string.no_news);
        mNewsAdapter.clear();

        if (newsList != null && !newsList.isEmpty()) {
            mNewsAdapter.addAll(newsList);
        }
    }

    /**
     * ???????????? onLoaderReset()???????????????????????????loader?????????????????????
     * ????????????Loader???????????????
     *
     * @param loader ?????????
     */
    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        Log.i(LOG_TAG, "Info: onLoaderReset().");

        mNewsAdapter.clear();
    }
}