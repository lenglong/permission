package com.yyc.permission.aop;

/**
 * ==========================
 *
 * @author yuanyanchao <a href="mailto:lenglong110@qq.com">Contact me.</a>
 * @date 2019-03-28
 * ==========================
 */
public interface IPermission {
    void onPermissionGrand();
    void onPermissionCancel(int requestCode);
    void onPermissionDenied(int requestCode);
}
