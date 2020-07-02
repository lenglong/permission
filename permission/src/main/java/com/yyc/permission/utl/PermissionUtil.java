package com.yyc.permission.utl;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.collection.SimpleArrayMap;
import androidx.core.app.ActivityCompat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * ==========================
 *
 * @author yuanyanchao <a href="mailto:lenglong110@qq.com">Contact me.</a>
 * @date 2019-03-28
 * ==========================
 */
public class PermissionUtil {

    private static SimpleArrayMap<String,Integer> MIN_SDK_PERMISSIONS;

    static {
        MIN_SDK_PERMISSIONS = new SimpleArrayMap<>(8);
        MIN_SDK_PERMISSIONS.put("com.android.voicemail.permission.ADD_VOICEMAIL",14);
        MIN_SDK_PERMISSIONS.put("android.permission.BODY_SENSORS",20);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_CALL_LOG",16);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_EXTERNAL_STORAGE",16);
        MIN_SDK_PERMISSIONS.put("android.permission.USE_SIP",9);
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_CALL_LOG",16);
        MIN_SDK_PERMISSIONS.put("android.permission.SYSTEM_ALERT_WINDOW",23);
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_SETTINGS",23);
    }

    /**
     * 判断所有权限是否都同意了
     * @param context
     * @param permissions
     * @return
     */
    public static boolean hasSelfPermissions(Context context,String... permissions){
        for (String permission : permissions) {
            if( permissionExists(permission) && !hasSelfPermission(context,permission)){
                return false;
            }
        }
        return true;
    }

    /**
     * 判断单个权限是否授权
     * @param context
     * @param permission
     * @return
     */
    private static boolean hasSelfPermission(Context context,String permission){
        return ActivityCompat.checkSelfPermission(context, permission)== PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 判断权限是否存在
     * @param permission
     * @return
     */
    private static boolean permissionExists(String permission){
        Integer minVersion = MIN_SDK_PERMISSIONS.get(permission);
        return minVersion==null || Build.VERSION.SDK_INT>=minVersion;
    }

    /**
     * 检查所有权限是否都授权了
     * @param grantResults
     * @return
     */
    public static boolean verifyPermissions(int...grantResults){
        if(grantResults==null || grantResults.length==0){
            return false;
        }
        for (int grantResult : grantResults) {
            if(grantResult != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    /**
     * 检查所给权限List，被设置不再提醒时，是否需要给提示
     * @param activity
     * @param permissions
     * @return
     */
    public static boolean shouldShowRequestPermissionRationale(Activity activity,String...permissions){
        for (String permission : permissions) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity,permission)){
                return true;
            }
        }
        return false;
    }

    /**
     * 反射调用某个有指定注解的方法
     * @param object
     * @param annotationClass
     * @param requestCode
     */
    public static void invokeAnnotation(Object object,Class annotationClass,int requestCode){
        //获取上下文的类型
        Class<?> aClass = object.getClass();
        //获取类型中的方法
        Method[] declaredMethods = aClass.getDeclaredMethods();
        if(declaredMethods.length==0){
            return;
        }
        for (Method declaredMethod : declaredMethods) {
            //判断方法是否有anntationClass的注解
            if(declaredMethod.isAnnotationPresent(annotationClass)){
                //判断有且仅有一个参数
                Class<?>[] parameterTypes = declaredMethod.getParameterTypes();
                if(parameterTypes.length==1){
//                    if(parameterTypes[0]!=null && int.class.equals(parameterTypes[0].getClass())){
                        declaredMethod.setAccessible(true);
                        try {
                            declaredMethod.invoke(object,requestCode);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
//                    }
                }
            }
        }
    }





}
