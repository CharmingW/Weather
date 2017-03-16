package com.charming.weather.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.charming.weather.R;
import com.charming.weather.entity.news.Data;
import com.charming.weather.entity.news.Result;
import com.charming.weather.util.BitmapCache;

import java.util.List;
import java.util.Map;

/**
 * Created by 56223 on 2016/11/18.
 */

public class NewsListAdapter extends BaseAdapter {

    private static final String TAG = "NewsListAdapter";
    private BitmapCache mBitmapcache;
    private Context mContext;
    private List<Data> mData;
    private ImageLoader mImageLoader;

    public NewsListAdapter(Context context) {
        mContext = context;
    }

    public Object getItem(int position) {
        return mData.get(position);
    }

    public void setBitmapCache(BitmapCache cache) {
        mBitmapcache = cache;
        mImageLoader = new ImageLoader(Volley.newRequestQueue(mContext), mBitmapcache);
    }

    public void setData(Map<String, Object> data) {
        Result result = (Result) data.get("result");
        mData = result.getData();
    }

    public int getCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i("NewsListAdapter", "getView: " + position);
        Data data = (Data) getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.news_list_item, null);
            holder.titleText = (TextView) convertView.findViewById(R.id.news_item_title);
            holder.sourceText = (TextView) convertView.findViewById(R.id.news_item_source);
            holder.timeText = (TextView) convertView.findViewById(R.id.news_item_time);
            holder.image1 = (ImageView) convertView.findViewById(R.id.news_item_image1);
            holder.image2 = (ImageView) convertView.findViewById(R.id.news_item_image2);
            holder.image3 = (ImageView) convertView.findViewById(R.id.news_item_image3);
            holder.titleText.setText(data.getTitle());
            holder.sourceText.setText(data.getAuthor_name());
            holder.timeText.setText(data.getDate());
            convertView.setTag(holder);
        } else {
            holder = (NewsListAdapter.ViewHolder) convertView.getTag();
            holder.titleText.setText(data.getTitle());
            holder.sourceText.setText(data.getAuthor_name());
            holder.timeText.setText(data.getDate());
        }
        if (data.getThumbnail_pic_s() != null) {
            holder.image1.setVisibility(View.VISIBLE);
            ImageLoader.ImageListener listener1 = ImageLoader.getImageListener(holder.image1, R.drawable.ic_preload, R.drawable.ic_preload);
            mImageLoader.get(data.getThumbnail_pic_s(), listener1);
        } else {
            holder.image1.setVisibility(View.INVISIBLE);
        }
        if (data.getThumbnail_pic_s02() != null) {
            holder.image2.setVisibility(View.VISIBLE);
            ImageLoader.ImageListener listener2 = ImageLoader.getImageListener(holder.image2, R.drawable.ic_preload, R.drawable.ic_preload);
            mImageLoader.get(data.getThumbnail_pic_s02(), listener2);
        } else {
            holder.image2.setVisibility(View.INVISIBLE);
        }
        if (data.getThumbnail_pic_s03() != null) {
            holder.image3.setVisibility(View.VISIBLE);
            ImageLoader.ImageListener listener3 = ImageLoader.getImageListener(holder.image3, R.drawable.ic_preload, R.drawable.ic_preload);
            mImageLoader.get(data.getThumbnail_pic_s03(), listener3);
        } else {
            holder.image3.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView image1;
        ImageView image2;
        ImageView image3;
        TextView sourceText;
        TextView timeText;
        TextView titleText;
    }


}
