package com.reforms.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TargetQuery {

    /** Batch size ignore value */
    final int BATCH_IGNORE_SIZE = -1;
    /** Type ignore value */
    final int TYPE_IGNORE_SIZE = -100000;

    /** Select query */
    final int QT_AUTO = -1;

    /** Select query */
    final int QT_SELECT = 0;

    /** Insert query */
    final int QT_INSERT = 1;

    /** Update query */
    final int QT_UPDATE = 2;

    /** Delete query */
    final int QT_DELETE = 3;

    /** Store procedure call query */
    final int QT_CALL = 4;

    /** Тип sql-запроса */
    int type() default QT_AUTO;

    /** SQL-запрос */
    String query() default "";

    /** SQL-запрос - short style, if nothing except query */
    String value() default "";

    int batchSize() default BATCH_IGNORE_SIZE;

    /** Тип sql-запроса */
    int returnType() default TYPE_IGNORE_SIZE;

    /** SQL-запрос */
    Class<?> orm() default Object.class;

}
