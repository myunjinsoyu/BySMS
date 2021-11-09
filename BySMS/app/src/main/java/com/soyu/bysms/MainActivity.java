package com.soyu.bysms;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private SQLHelper sql;
    private SQLiteDatabase db;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        context = this;

        sql = new SQLHelper(context, "bysms", null, 1);
        db = sql.getReadableDatabase();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setLogo(getResources().getDrawable(R.mipmap.ic_launcher));
        toolbar.setBackgroundColor(getResources().getColor(R.color.Toolbaritle_background));
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, NumberAddActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        list = (ListView)findViewById(R.id.list);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Cursor c = sql.selectData(db);
        while (c.moveToNext()) {
            String str = c.getString(1)+", "+c.getString(2);
            Log.e("!!!", "str = "+str);
        }
        list.setAdapter(new listAdapter(c));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class listAdapter extends BaseAdapter {

        private Cursor c;
        public listAdapter ( Cursor cursor ) {
            c = cursor;
        }
        @Override
        public int getCount() {
            int count = c.getCount();
            Log.e("!!!", "list count = "+count);
            return count;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if ( view == null ) {
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_item_layout, viewGroup, false);
            }

            c.moveToFirst();
            c.moveToPosition(i);
            String str = c.getString(1)+", "+c.getString(2);
            Log.e("!!!", "adapter ===   str = "+str);

            TextView recv = (TextView)view.findViewById(R.id.recv);
            TextView send = (TextView)view.findViewById(R.id.send);

            recv.setText("문자 받은 번호 : "+c.getString(1));
            send.setText("전달할 번호 : "+c.getString(2));

            return view;
        }
    }
}
