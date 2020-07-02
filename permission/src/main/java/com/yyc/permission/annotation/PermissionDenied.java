package com.yyc.permission.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ==========================
 *
 * @author yuanyanchao <a href="mailto:lenglong110@qq.com">Contact me.</a>
 * @date 2019-03-28
 * ==========================
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionDenied {
}
