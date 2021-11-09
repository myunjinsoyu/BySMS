package com.soyu.bysms;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

public class IntroActivity extends AppCompatActivity {

    private Handler h;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_intro);
        context = this;

        if ( checkSMSPermissions() ) {
            moveMainpage();
        }
    }

    private void moveMainpage() {
        SQLHelper sql = new SQLHelper(context, "bysms", null, 1);

        h= new Handler();
        h.postDelayed(mrun, 2000);
    }

    private boolean checkSMSPermissions() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        if ( permissionCheck == PackageManager.PERMISSION_GRANTED ) {
            Toast.makeText(this, "SMS RECEIVE 권한 있음", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(this, "SMS RECEIVE 권한 없음", Toast.LENGTH_SHORT).show();

            if ( ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS ) ) {
                Toast.makeText(this, "SMS RECEIVE 권한 설명 필요함", Toast.LENGTH_SHORT).show();
            }

            String[] permissions = { Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS };
            ActivityCompat.requestPermissions(this, permissions, 1);
            return false;
        }
    }

    Runnable mrun = new Runnable(){
        @Override
        public void run(){
            Intent i = new Intent(context, MainActivity.class);
            startActivity(i);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if ( requestCode == 1 ) {
            for ( int i = 0; i < permissions.length; i++ ) {
                if ( grantResults[i] == PackageManager.PERMISSION_GRANTED ) {
                    Toast.makeText(this, permissions[i]+" 권한이 승인됨", Toast.LENGTH_SHORT).show();
                    moveMainpage();
                } else {
                    Toast.makeText(this, permissions[i]+" 권한이 승인되지 않음", Toast.LENGTH_SHORT).show();
                    System.exit(0);
                }
            }
        }
    }
}
