package com.zoovisitors.pl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.zoovisitors.R;

public class EnclosureListActivity extends AppCompatActivity {

    private RecyclerView recycleView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enclosure_list);

        recycleView = (RecyclerView) findViewById(R.id.enclosure_recycle);
        layoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapter(this);
        recycleView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
//        if(id == R.id.action_settings)
//            return true;
        return super.onOptionsItemSelected(item);
    }
}