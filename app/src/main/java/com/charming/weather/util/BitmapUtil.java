package com.charming.weather.util;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * Created by CharmingWong on 2016/12/10.
 */

public class BitmapUtil {


    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream baos;
        try {
            baos = new ByteArrayOutputStream();
            if (bitmap != null) {
                boolean isCompressed = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                if (isCompressed) {
                    return baos.toByteArray();
                }
                return null;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
