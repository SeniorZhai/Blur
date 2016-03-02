#include <jni.h>
#include <android/bitmap.h>

#include "blur.h"

JNIEXPORT void JNICALL
Java_com_seniorzhai_blur_BlurNative_blurPixels(JNIEnv *env, jclass type, jintArray img_, jint w,
                                                   jint h, jint r) {
    jint *img = (*env)->GetIntArrayElements(env, img_, NULL);

    if (img == NULL) {
        return;
    }

    img = blur(img, w, h, r);

    (*env)->ReleaseIntArrayElements(env, img_, img, 0);
}

JNIEXPORT void JNICALL
Java_com_seniorzhai_blur_BlurNative_blurBitmap(JNIEnv *env, jclass type, jobject bitmap,
                                                   jint r) {
    AndroidBitmapInfo info;
    void *pixels;
    int ret;

    if ((ret = AndroidBitmap_getInfo(env, bitmap, &info)) != 0) {
        return;
    }

    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        return;
    }

    if ((ret = AndroidBitmap_lockPixels(env, bitmap, &pixels)) != 0) {
        return;
    }

    int h = info.height;
    int w = info.width;

    pixels = blur((int *) pixels, w, h, r);

    AndroidBitmap_unlockPixels(env, bitmap);
}