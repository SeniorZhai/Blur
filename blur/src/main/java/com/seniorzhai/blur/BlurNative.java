package com.seniorzhai.blur;

import android.graphics.Bitmap;

/**
 * Created by zhai on 16/3/1.
 */
public class BlurNative {
    static {
        System.loadLibrary("blurNative");
    }

    public static native void blurPixels(int[] img, int w, int h, int r);

    public static native void blurBitmap(Bitmap bitmap, int r);
}
