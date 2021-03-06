/**
 * Generated by smali2java 1.0.0.558
 * Copyright (C) 2013 Hensence.com
 */

package com.charming.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.charming.weather.R;

public class NewsTypeAdapter extends BaseAdapter {
    private Context mContext;
    private String[] mNewsType;

    public NewsTypeAdapter(Context context) {
        mContext = context;
        mNewsType = mContext.getResources().getStringArray(R.array.news_channel);
    }

    public int getCount() {
        return mNewsType.length;
    }

    public Object getItem(int position) {
        return mNewsType[position];
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.news_type_item, parent, false);
            holder.newsIcon = (ImageView) convertView.findViewById(R.id.news_icon);
            holder.newsType = (TextView) convertView.findViewById(R.id.type_text);
            convertView.setTag(holder);
        } else {
            holder = (NewsTypeAdapter.ViewHolder) convertView.getTag();
        }
        holder.newsIcon.setImageResource(getIconIdFromName((String) getItem(position)));
        holder.newsType.setText((String) getItem(position));
        return convertView;
    }

    private int getIconIdFromName(String name) {
        switch (name) {
            case "头条":
                return R.drawable.ic_headline;
            case "社会":
                return R.drawable.ic_society;
            case "科技":
                return R.drawable.ic_technology;
            case "体育":
                return R.drawable.ic_sport;
            case "娱乐":
                return R.drawable.ic_fun;
            case "时尚":
                return R.drawable.ic_fashion;
            case "军事":
                return R.drawable.ic_military;
            case "国内":
                return R.drawable.ic_china;
            case "国际":
                return R.drawable.ic_internation;
            default:
                return 0;
        }
    }

    class ViewHolder {
        ImageView newsIcon;
        TextView newsType;
    }
}
