package com.charming.weather.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by CharmingWong on 2016/12/10.
 */

public class BitmapCache implements ImageLoader.ImageCache, Closeable {

    private static final String TAG = "BitmapCache";

    private DiskLruCache mDiskLruCache;
    private LruCache<String, Bitmap> mLruCache;

    public BitmapCache(Context context) {
        mLruCache = new LruCache<>(8 * 1024 * 1024);
        try {
            File cacheDir = ApplicationUtil.getDiskCacheDir(context, "bitmap");
            mDiskLruCache = DiskLruCache.open(cacheDir, ApplicationUtil.getAppVersion(context), 1, 50 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Bitmap getBitmap(String url) {
        String key = HashKeyUtil.generateHashKey(url);
        Bitmap bitmap = mLruCache.get(key);
        if (bitmap != null) {
            Log.i(TAG, "getBitmap: load bitmap from memory cache");
            return bitmap;
        }
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
            if (snapshot != null) {
                Log.i(TAG, "getBitmap: load bitmap from disk cache");
                bitmap = BitmapFactory.decodeStream(snapshot.getInputStream(0));
                snapshot.close();
                mLruCache.put(key, bitmap);
                return bitmap;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        String key = HashKeyUtil.generateHashKey(url);
        //写入内存缓存
        mLruCache.put(key, bitmap);
        OutputStream out = null;
        DiskLruCache.Editor editor = null;
        try {
            //写入磁盘缓存
            editor = mDiskLruCache.edit(key);
            out = editor.newOutputStream(0);
            byte[] bmp = BitmapUtil.bitmapToByteArray(bitmap);
            if (bmp != null) {
                out.write(bmp);
                Log.i(TAG, "putBitmap: wrote bitmap to disk cache successfully");
            }
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
            if (editor != null) {
                try {
                    editor.abort();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            Log.i(TAG, "putBitmap: failed to wrote bitmap to disk cache");
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void close() throws IOException {
        mDiskLruCache.close();
    }
}
