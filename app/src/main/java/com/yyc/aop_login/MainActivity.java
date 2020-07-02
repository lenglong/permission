package com.yyc.aop_login;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.yyc.permission.annotation.PermissionCancel;
import com.yyc.permission.annotation.PermissionDenied;
import com.yyc.permission.annotation.PermissionNeed;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: ");
                reqPermission();
            }
        });

    }

    @PermissionNeed(value = {Manifest.permission.ACCESS_FINE_LOCATION},requestCode = 11)
    public void reqPermission(){
        Log.e(TAG, "reqPermission: hello AspetJ");
    }
    @PermissionCancel()
    public void permissionCancel(int requestCode){
        Log.e(TAG, "reqPermission: permissionCancel "+requestCode);
    }
    @PermissionDenied()
    public void permissionDenied(int requestCode){
        Log.e(TAG, "reqPermission: permissionDenied "+requestCode);
    }
}
