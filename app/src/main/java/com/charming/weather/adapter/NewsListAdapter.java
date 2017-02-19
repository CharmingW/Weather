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
import com.charming.weather.entity_news.Tngou;
import com.charming.weather.util.BitmapCache;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 56223 on 2016/11/18.
 */

public class NewsListAdapter extends BaseAdapter {

    private static final String URL_PREFIX = "http://tnfs.tngou.net/image";
    private static final String TAG = "NewsListAdapter";

    private Context mContext;
    private Map<String, Object> mData;
    private ImageLoader mImageLoader;

    public NewsListAdapter(Context context) {
        mContext = context;
        mImageLoader = new ImageLoader(Volley.newRequestQueue(mContext), new BitmapCache(mContext));
    }

    public void setData(Map<String, Object> data) {
        mData = data;
    }

    @Override
    public int getCount() {
        if (mData != null) {
            return ((List<Tngou>) mData.get("tngou")).size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        List<Tngou> tngous = (List<Tngou>) mData.get("tngou");
        Tngou tngou = tngous.get(position);
        return tngou;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG, "getView: " + position);
        Tngou tngou = (Tngou) getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_news_list_item, null);
            holder.titleText = (TextView) convertView.findViewById(R.id.news_item_title);
            holder.sourceText = (TextView) convertView.findViewById(R.id.news_item_source);
            holder.descriptionText = (TextView) convertView.findViewById(R.id.news_item_description);
            holder.timeText = (TextView) convertView.findViewById(R.id.news_item_time);
            holder.image = (ImageView) convertView.findViewById(R.id.news_item_image);
            holder.titleText.setText(tngou.getTitle());
            holder.sourceText.setText(tngou.getKeywords());
            holder.descriptionText.setText(tngou.getDescription());
            holder.timeText.setText(
                    new SimpleDateFormat(mContext.getString(R.string.common_date_format))
                            .format(new Date(tngou.getTime()))
            );
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.titleText.setText(tngou.getTitle());
            holder.sourceText.setText(tngou.getKeywords());
            holder.descriptionText.setText(tngou.getDescription());
            holder.timeText.setText(new SimpleDateFormat("MM月dd日 HH:mm").format(new Date(tngou.getTime())));
        }

        String imageUrl = tngou.getImg();
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(
                holder.image,
                R.drawable.ic_preload,
                android.R.drawable.ic_delete
        );
        mImageLoader.get(URL_PREFIX + imageUrl, listener);
        return convertView;
    }

    private class ViewHolder {
        TextView titleText, sourceText, descriptionText, timeText;
        ImageView image;
    }

}
