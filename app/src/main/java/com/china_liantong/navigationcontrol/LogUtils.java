package com.china_liantong.navigationcontrol;

import android.os.SystemClock;

import java.util.List;

/**
 * v d i w e
 */
public class LogUtils {
    protected static final String TAG = "tag_navi";
    public static final boolean DEBUG = true;
    private static long MARK_TIME = 0;

    /**
     * Mark Current Time for dumpTime()
     */
    public static void markTime() {
        MARK_TIME = SystemClock.elapsedRealtime();
    }

    /**
     * Dumps the consumptive time to the log using LogUtils.d(). \
     *
     * @param s
     *            The message you would like logged.
     */
    public static void dumpTime(String s) {
        d(s + (SystemClock.elapsedRealtime() - MARK_TIME) + "ms");
    }

    /**
     * Print Object that won't care about NullPointerException
     *
     * @param o
     *            The Object you would like to print.
     */
    public static String printObject(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString();
    }

    /**
     * Print List that won't care about NullPointerException
     *
     * @param list
     *            The List you would like to print.
     */
    public static<T extends Object> String printList(List<T> list) {
        if (list == null) {
            return "null";
        }

        StringBuilder builder = new StringBuilder("\n");
        for (Object o : list) {
            builder.append("[").append(o.toString()).append("];\n");
        }
        return builder.toString();
    }

    /**
     * Print Fault Msg if Result is Error
     */
    public static void faultResultLog() {
        android.util.Log.w(TAG, buildMessage("Fault Result"));
    }

    /**
     * Send a VERBOSE log message.
     *
     * @param msg
     *            The message you would like logged.
     */
    public static void v(String msg) {
        if (DEBUG)
            android.util.Log.v(TAG, buildMessage(msg));
    }

    /**
     * Send a VERBOSE log message and log the exception.
     *
     * @param msg
     *            The message you would like logged.
     * @param thr
     *            An exception to log
     */
    public static void v(String msg, Throwable thr) {
        if (DEBUG)
            android.util.Log.v(TAG, buildMessage(msg), thr);
    }

    /**
     * Send a DEBUG log message.
     *
     * @param msg
     */
    public static void d(String msg) {
        if (DEBUG)
            android.util.Log.d(TAG, buildMessage(msg));
    }

    /**
     * Send a DEBUG log message and log the exception.
     *
     * @param msg
     *            The message you would like logged.
     * @param thr
     *            An exception to log
     */
    public static void d(String msg, Throwable thr) {
        if (DEBUG)
            android.util.Log.d(TAG, buildMessage(msg), thr);
    }

    /**
     * Send an INFO log message.
     *
     * @param msg
     *            The message you would like logged.
     */
    public static void i(String msg) {
        if (DEBUG)
            android.util.Log.i(TAG, buildMessage(msg));
    }

    /**
     * Send a INFO log message and log the exception.
     *
     * @param msg
     *            The message you would like logged.
     * @param thr
     *            An exception to log
     */
    public static void i(String msg, Throwable thr) {
        if (DEBUG)
            android.util.Log.i(TAG, buildMessage(msg), thr);
    }

    /**
     * Send an ERROR log message.
     *
     * @param msg
     *            The message you would like logged.
     */
    public static void e(String msg) {
        if (DEBUG)
            android.util.Log.e(TAG, buildMessage(msg));
    }

    /**
     * Send a WARN log message
     *
     * @param msg
     *            The message you would like logged.
     */
    public static void w(String msg) {
        if (DEBUG)
            android.util.Log.w(TAG, buildMessage(msg));
    }

    /**
     * Send a WARN log message and log the exception.
     *
     * @param msg
     *            The message you would like logged.
     * @param thr
     *            An exception to log
     */
    public static void w(String msg, Throwable thr) {
        if (DEBUG)
            android.util.Log.w(TAG, buildMessage(msg), thr);
    }

    /**
     * Send an empty WARN log message and log the exception.
     *
     * @param thr
     *            An exception to log
     */
    public static void w(Throwable thr) {
        if (DEBUG)
            android.util.Log.w(TAG, buildMessage(""), thr);
    }

    /**
     * Send an ERROR log message and log the exception.
     *
     * @param msg
     *            The message you would like logged.
     * @param thr
     *            An exception to log
     */
    public static void e(String msg, Throwable thr) {
        if (DEBUG)
            android.util.Log.e(TAG, buildMessage(msg), thr);
    }

    /**
     * Building Message
     *
     * @param msg
     *            The message you would like logged.
     * @return Message String
     */
    protected static String buildMessage(String msg) {
        StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];

        return new StringBuilder().append(caller.getClassName()).append(".")
                .append(caller.getMethodName()).append("(): ")
                .append(msg).toString();
    }
}
