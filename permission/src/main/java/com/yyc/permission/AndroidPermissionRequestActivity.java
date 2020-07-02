package com.yyc.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.yyc.permission.aop.IPermission;
import com.yyc.permission.utl.PermissionUtil;

/**
 * ==========================
 *
 * @author yuanyanchao <a href="mailto:lenglong110@qq.com">Contact me.</a>
 * @date 2019-03-28
 * ==========================
 */
public class AndroidPermissionRequestActivity extends Activity {
    private static String PERMISSIONS = "permissions";
    private static String REQUESTCODE = "request_code";
    private static IPermission mIPermission;

    public static void startActiviy(Context context,String[] permissions,int requestCode,IPermission iPermission) {
        Log.e("MainActivity", "startActiviy:  PermissionsActivity " );
        mIPermission = iPermission;
        Intent intent = new Intent(context,AndroidPermissionRequestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle args = new Bundle();
        args.putStringArray(PERMISSIONS,permissions);
        args.putInt(REQUESTCODE,requestCode);
        intent.putExtras(args);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("MainActivity", "onCreate:  PermissionsActivity " );
        Bundle bundle = getIntent().getExtras();
        String[] permissions = bundle.getStringArray(PERMISSIONS);
        int requestCode = bundle.getInt(REQUESTCODE,0);

        requestPermission(permissions,requestCode);
    }

    private void requestPermission(String[] permissions, int requestCode) {
        if(PermissionUtil.hasSelfPermissions(this,permissions)){
            Log.e("MainActivity", "requestPermission:  PermissionsActivity if" );
            //所有权限都通过了
            mIPermission.onPermissionGrand();
            finish();
        }else{
            Log.e("MainActivity", "requestPermission:  PermissionsActivity else" );
            //还没有授权，获取授权
            ActivityCompat.requestPermissions(this,permissions, requestCode);

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(PermissionUtil.verifyPermissions(grantResults)){
            //所有权限都允许了 跳回切入点出口
            mIPermission.onPermissionGrand();
        }else{
            if(PermissionUtil.shouldShowRequestPermissionRationale(this,permissions)){
                //权限取消
                mIPermission.onPermissionCancel(requestCode);
            }else{
                //权限被拒绝
                mIPermission.onPermissionDenied(requestCode);
            }
        }
        finish();
    }
}
