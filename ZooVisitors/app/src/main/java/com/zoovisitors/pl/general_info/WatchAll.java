package com.zoovisitors.pl.general_info;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zoovisitors.GlobalVariables;
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
        getWindow().setLayout((int) (width * 0.85), (int) (height * 0.9));

        //get all the news feed
        allNewsFeed = (WallFeed[]) getIntent().getExtras().getSerializable("NewsFeed");

        LinearLayout allNewsFeedList = findViewById(R.id.allNewsFeedList);

        TextView newsFeedTitle = findViewById(R.id.wall_feed_popup_title);
        newsFeedTitle.setPaintFlags(newsFeedTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        newsFeedTitle.setTypeface(null, Typeface.BOLD);

        for (WallFeed feed: allNewsFeed) {
            //initiate the feed layout
            LinearLayout feedLayout = new LinearLayout(getBaseContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            feedLayout.setBackground(getResources().getDrawable(R.drawable.dashed_bottom_line));
            if (GlobalVariables.language == 1 || GlobalVariables.language == 3){
                feedLayout.setGravity(Gravity.RIGHT);
            }

            feedLayout.setLayoutParams(params);
            feedLayout.setOrientation(LinearLayout.VERTICAL);

            //initiate the title
            TextViewTitle titleText = new TextViewTitle(getBaseContext(), View.TEXT_ALIGNMENT_TEXT_START);
            titleText.setText(feed.getTitle());

            //initiate the date
            TextView dateText = new TextView(getBaseContext());
            dateText.setTextColor(getResources().getColor(R.color.black));
            dateText.setTextSize(12);
            dateText.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
            dateText.setIncludeFontPadding(false);

            String text = "["+ feed.getCreated().subSequence(0,10) + "]";
            dateText.setText(text);


            //initiate the text TextView
            TextViewRegularText regularText = new TextViewRegularText(getBaseContext(), View.TEXT_ALIGNMENT_GRAVITY);
            regularText.setText(feed.getInfo());

            feedLayout.addView(titleText);
            feedLayout.addView(dateText);
            feedLayout.addView(regularText);

            allNewsFeedList.addView(feedLayout);
        }
    }
}
