package com.wangmengyao.factsheetnews;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    private static final String LOG_TAG = NewsAdapter.class.getName();

    private static final String Date_SEPARATOR = "T";
    private static final String Date_END = "Z";

    public NewsAdapter(Context context, List<News> newsItemList) {
        super(context, 0, newsItemList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_item, parent, false);
        }

        News currentNews = getItem(position);

        TextView topic = (TextView) listItemView.findViewById(R.id.topic_text_view);
        topic.setText(currentNews.getTopic());

        GradientDrawable circle = (GradientDrawable) topic.getBackground();
        circle.setColor(getTopicColor(currentNews.getTopic()));

        TextView title = (TextView) listItemView.findViewById(R.id.title_text_view);
        title.setText(currentNews.getTitle());

        TextView origin = (TextView) listItemView.findViewById(R.id.origin_text_view);
        origin.setText(currentNews.getOrigin());

        String[] parts = currentNews.getDate().replace(Date_END, "").split(Date_SEPARATOR);

        TextView date = (TextView) listItemView.findViewById(R.id.date_text_view);
        date.setText(parts[0]);

        TextView time = (TextView) listItemView.findViewById(R.id.time_text_view);
        time.setText(parts[1]);

        return listItemView;
    }

    private int getTopicColor(String topic) {
        int colorResourceId;
        if (topic.equals(String.valueOf(R.string.topic_article))) {
            colorResourceId = R.color.topic_article;
        } else if (topic.equals(String.valueOf(R.string.topic_liveblog))) {
            colorResourceId = R.color.topic_liveblog;
        } else {
            colorResourceId = R.color.topic_default;
        }
        return colorResourceId;
    }
}
