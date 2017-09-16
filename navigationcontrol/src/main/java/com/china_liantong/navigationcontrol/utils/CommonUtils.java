package com.china_liantong.navigationcontrol.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Randal on 2017-09-12.
 */

public class CommonUtils {
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    public static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1;
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }
}
