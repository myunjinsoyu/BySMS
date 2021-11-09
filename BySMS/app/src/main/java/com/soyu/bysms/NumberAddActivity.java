package com.soyu.bysms;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NumberAddActivity extends AppCompatActivity {

    private Context context;
    private LinearLayout layout_send_number;
    private LinearLayout add_number_button_layout;
    private int add_number_count = 1;
    private List<View> layouts = new ArrayList<View>();
    private List<EditText> edittexts = new ArrayList<EditText>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_add);
        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.title_add_number));
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setLogo(getResources().getDrawable(R.mipmap.ic_launcher));
        toolbar.setBackgroundColor(getResources().getColor(R.color.Toolbaritle_background));

        View view = getToolbarLogoIcon(toolbar);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        setSupportActionBar(toolbar);

        layout_send_number = (LinearLayout)findViewById(R.id.layout_send_number);
        add_number_button_layout = (LinearLayout)findViewById(R.id.add_number_button_layout);

        addNumberLayout(layout_send_number);
        Button add = (Button)findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNumberLayout(layout_send_number);
                if ( add_number_count > 5 ) {
                    add_number_button_layout.setVisibility(View.GONE);
                }


            }
        });

        Button save = (Button)findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recv_number = ((EditText)findViewById(R.id.recv_number)).getText().toString();
                Log.e("!!!", "recv_number = "+recv_number);

                Log.e("!!!", "edittexts count = "+edittexts.size());
                String sendNum = "";
                for( EditText tv : edittexts ) {
                    sendNum = sendNum + tv.getText().toString() +", ";
                }
                sendNum = sendNum.substring(0, sendNum.length()-2);
                Log.e("!!!", "sendNumber = "+sendNum);

                SQLHelper sql = new SQLHelper(context, "bysms", null, 1);
                SQLiteDatabase db = sql.getWritableDatabase();
                sql.insertData(db, recv_number, sendNum);

                Toast.makeText(context, getResources().getText(R.string.save_ok).toString(), Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
    }

    private void addNumberLayout(LinearLayout layout_send_number) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.add_number_layout, null, false);
        TextView text = (TextView)layout.findViewById(R.id.index);
        text.setText(""+add_number_count);
        add_number_count ++;
        layouts.add(layout);
        edittexts.add((EditText)layout.findViewById(R.id.index_text));
        layout_send_number.addView(layout);
    }

    public static View getToolbarLogoIcon(Toolbar toolbar){
        boolean hadContentDescription = android.text.TextUtils.isEmpty(toolbar.getLogoDescription());
        String contentDescription = String.valueOf(!hadContentDescription ? toolbar.getLogoDescription() : "logoContentDescription");
        toolbar.setLogoDescription(contentDescription);
        ArrayList<View> potentialViews = new ArrayList<View>();
        toolbar.findViewsWithText(potentialViews,contentDescription, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        View logoIcon = null;
        if(potentialViews.size() > 0){
            logoIcon = potentialViews.get(0);
        }
        if(hadContentDescription)
            toolbar.setLogoDescription(null);
        return logoIcon;
    }
}
