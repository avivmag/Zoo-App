package com.zoovisitors.pl.personalStories;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.zoovisitors.R;
import com.zoovisitors.backend.Animal;

public class PersonalStoriesActivity extends AppCompatActivity {

    private RecyclerView recycleViewAnim;
    private RecyclerView.LayoutManager layoutManagerAnim;
    private RecyclerView.Adapter adapterAnim;
    private String[] animalsNames;
    private Animal[] animals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_stories);

    }
}
