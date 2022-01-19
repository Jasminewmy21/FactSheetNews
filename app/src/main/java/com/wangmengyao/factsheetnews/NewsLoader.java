package com.wangmengyao.factsheetnews;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private static final String LOG_TAG = NewsLoader.class.getName();

    private String mUrl;

    /**
     * 构建新 {@link NewsLoader}
     *
     * @param context 上下文
     * @param url 要从中请求数据的url
     */
    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    /**
     * 重写onStartLoading()方法来调用forceLoad()，这是实际触发loadInBackground()方法执行的必要步骤。
     * https://www.codenong.com/43863328/
     */
    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "Info: NewsLoader - onStartLoading().");

        forceLoad();
    }

    /**
     * This is on the background thread.
     *
     * @return 返回查询需要的结果List<News>
     */
    @Override
    public List<News> loadInBackground() {
        Log.i(LOG_TAG, "Info: NewsLoader - loadInBackground().");

        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response and extract a list of data.
        return QueryUtils.fetchNewsDate(mUrl);
    }
}
