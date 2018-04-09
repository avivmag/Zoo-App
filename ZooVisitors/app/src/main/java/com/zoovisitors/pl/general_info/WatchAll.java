package com.zoovisitors.pl.general_info;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.zoovisitors.R;

public class WatchAll extends AppCompatActivity {

    private String[] allNewsFeed;
    private Bundle newsFeedBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_all);
        newsFeedBundle = getIntent().getExtras();
        allNewsFeed = (String[]) newsFeedBundle.getSerializable("newsFeed");
        ListView allNewsFeedList = (ListView)findViewById(R.id.allNewsFeedList);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.all_news_feed_item, R.id.newFeedItem, allNewsFeed);
        allNewsFeedList.setAdapter(arrayAdapter);
    }
}
