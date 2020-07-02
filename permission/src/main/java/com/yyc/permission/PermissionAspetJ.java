package com.yyc.permission;

import android.content.Context;
import android.os.Build;

import com.yyc.permission.annotation.PermissionCancel;
import com.yyc.permission.annotation.PermissionDenied;
import com.yyc.permission.annotation.PermissionNeed;
import com.yyc.permission.aop.IPermission;
import com.yyc.permission.utl.PermissionUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * ==========================
 * 定义切面类
 * @author yuanyanchao <a href="mailto:lenglong110@qq.com">Contact me.</a>
 * @date 2019-03-28
 * ==========================
 */
@Aspect
public class PermissionAspetJ {

    @Pointcut("execution(@com.yyc.permission.annotation.PermissionNeed * * (..)) && @annotation(permission)")
    public void requestAndroidPermission(PermissionNeed permission){

    }

    //两种写法 Around参数就是Pointcut，一般使用第二种
    //1.直接把Pointcut的参数传过来
    //@Pointcut("execution(@com.yyc.permission.annotation.PermissionNeed * *(..)) && @annotation(permission)")
    //2.把标有Pointcut注解的方法拿过来用
    @Around("requestAndroidPermission(permission)")
    public void aroundJoinPoint(final ProceedingJoinPoint joinPoint, PermissionNeed permission){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            try {
                joinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        //执行代码的地方 在哪个类标有PermissionNeed  joinPoint就包含哪个类的信息  getThis方法能得到这个对象
        final Object aThis = joinPoint.getThis();
        Context context = ((Context) aThis);

        AndroidPermissionRequestActivity.startActiviy(context, permission.value(),
                permission.requestCode(), new IPermission() {
                    @Override
                    public void onPermissionGrand() {
                        try {
                            joinPoint.proceed();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }

                    @Override
                    public void onPermissionCancel(int requestCode) {
                        PermissionUtil.invokeAnnotation(aThis, PermissionCancel.class,requestCode);
                    }

                    @Override
                    public void onPermissionDenied(int requestCode) {
                        PermissionUtil.invokeAnnotation(aThis, PermissionDenied.class,requestCode);

                    }
                });

    }

}
