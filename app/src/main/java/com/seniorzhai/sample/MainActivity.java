package com.seniorzhai.sample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.seniorzhai.blur.Blur;

public class MainActivity extends AppCompatActivity {
    private Bitmap mBitmap;
    private ImageView mImageJava, mImageJniPixels, mImageJniBitmap, mImageRenderBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avater);
        mImageJava = (ImageView) findViewById(R.id.image_blur_java);
        mImageJniPixels = (ImageView) findViewById(R.id.image_blur_jni_pixels);
        mImageJniBitmap = (ImageView) findViewById(R.id.image_blur_jni_bitmap);
        mImageRenderBitmap = (ImageView) findViewById(R.id.image_blur_render_bitmap);
        applyBlur();
    }

    private void applyBlur() {
        // First clear
        clearDrawable();

        // Run Thread
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Blur Time: ");
                    for (int i = 1; i < 5; i++) {
                        sb.append(blur(i)).append(" ");
                    }

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            mTime.setText(sb.toString());
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private long blur(int i) {
        Log.d("---", "111");

        ImageView view = null;
        if (i == 1)
            view = mImageJava;
        else if (i == 2)
            view = mImageJniPixels;
        else if (i == 3)
            view = mImageJniBitmap;
        else if (i == 4)
            view = mImageRenderBitmap;

        long startMs = System.currentTimeMillis();

        // Is Compress
        float radius = 20;
        Bitmap overlay = mBitmap;

        // Java
        if (i == 1)
            overlay = Blur.blur(overlay, (int) radius, false);
            // Bitmap JNI Native
        else if (i == 2)
            overlay = Blur.blurNatively(overlay, (int) radius, false);
            // Pixels JNI Native
        else if (i == 3)
            overlay = Blur.blurNativelyPixels(overlay, (int) radius, false);
        else if (i == 4)
            overlay = Blur.blurRenderScript(this, overlay, (int) radius, false);

        // Show
        showDrawable(view, overlay);

        return System.currentTimeMillis() - startMs;
    }

    private void clearDrawable() {
        mImageJava.setImageBitmap(null);
        mImageJniPixels.setImageBitmap(null);
        mImageJniBitmap.setImageBitmap(null);
        mImageRenderBitmap.setImageBitmap(null);
    }

    private void showDrawable(final ImageView view, final Bitmap overlay) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.setImageBitmap(overlay);
            }
        });
    }
}
