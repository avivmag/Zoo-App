package com.zoovisitors.pl.personalStories;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.zoovisitors.GlobalVariables;
import com.zoovisitors.R;
import com.zoovisitors.backend.Animal;
import com.zoovisitors.bl.callbacks.GetObjectInterface;
import com.zoovisitors.pl.BaseActivity;

public class PersonalStoriesActivity extends BaseActivity {

    private RecyclerView recycleView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
//    private Animal.PersonalStories[] personalStories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_stories);

        recycleView = (RecyclerView) findViewById(R.id.personal_animal_recycler);
//        layoutManager = new LinearLayoutManager(GlobalVariables.appCompatActivity, LinearLayoutManager.HORIZONTAL, false);
        layoutManager = new GridLayoutManager(this, 2);
        recycleView.setLayoutManager(layoutManager);


        GlobalVariables.bl.getPersonalStories(new GetObjectInterface() {
            @Override
            public void onSuccess(Object response) {
//                personalStories = ()
                adapter = new PersonalStoriesRecyclerAdapter((Animal.PersonalStories[]) response);
                recycleView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Object response) {
                ((TextView) findViewById(R.id.personal_text_no_data)).setText((String) response);
            }
        });




//        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
//        recyclerView.setLayoutManager(mLayoutManager);

    }
}
