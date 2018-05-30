package com.zoovisitors.pl.general_info;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.zoovisitors.R;
import com.zoovisitors.backend.WallFeed;
import com.zoovisitors.pl.BaseActivity;
import com.zoovisitors.pl.customViews.TextViewRegularText;
import com.zoovisitors.pl.customViews.TextViewTitle;

public class WatchAll extends BaseActivity {

    private WallFeed[] allNewsFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_all);

        //calculate the window size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.8));

        //get all the news feed
        allNewsFeed = (WallFeed[]) getIntent().getExtras().getSerializable("NewsFeed");

        LinearLayout allNewsFeedList = findViewById(R.id.allNewsFeedList);

        for (WallFeed feed: allNewsFeed) {
            //initiate the feed layout
            LinearLayout feedLayout = new LinearLayout(getBaseContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            feedLayout.setBackground(getResources().getDrawable(R.drawable.up_down_border));

            feedLayout.setLayoutParams(params);
            feedLayout.setOrientation(LinearLayout.VERTICAL);

            //initiate the title TextView
            TextViewTitle titleText = new TextViewTitle(getBaseContext(), View.TEXT_ALIGNMENT_TEXT_START);
            titleText.setText(feed.getTitle());

            //initiate the text TextView
            TextViewRegularText regularText = new TextViewRegularText(getBaseContext(), View.TEXT_ALIGNMENT_TEXT_START);
            regularText.setText(feed.getInfo());

            feedLayout.addView(titleText);
            feedLayout.addView(regularText);

            allNewsFeedList.addView(feedLayout);
        }
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.all_news_feed_item, R.id.newFeedItem, allNewsFeed);
//        allNewsFeedList.setAdapter(arrayAdapter);
    }
}
