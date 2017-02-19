package com.charming.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.charming.weather.R;

import java.util.List;

/**
 * Created by CharmingWong on 2016/12/12.
 */
public class CityNameListAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mCityList;
    private boolean isEditing = false;
    private onDeleteCityCallback mOnDeleteCityCallback;

    public CityNameListAdapter(Context context, List<String> cityNames) {
        mContext = context;
        mCityList = cityNames;
    }

    public void setData(List<String> cityList) {
        mCityList = cityList;
    }

    public List<String> getData() {
        return mCityList;
    }

    public void setOnDeleteCityCallback(onDeleteCityCallback onDeleteCityCallback) {
        mOnDeleteCityCallback = onDeleteCityCallback;
    }

    public void setEditing(boolean isEditing) {
        this.isEditing = isEditing;
    }

    public boolean isEditing() {
        return isEditing;
    }

    @Override
    public int getCount() {
        return mCityList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_city_list, null, false);
            holder.cityName = (TextView) convertView.findViewById(R.id.city_name);

            holder.cityName.setText((String) getItem(position));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.cityName.setText((String) getItem(position));
        }
        if (isEditing) {
            View delete = convertView.findViewById(R.id.image_delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnDeleteCityCallback.onDeleteCitySuccess(holder.cityName.getText().toString());
                }
            });
            delete.setVisibility(View.VISIBLE);
        } else {
            convertView.findViewById(R.id.image_delete).setVisibility(View.GONE);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView cityName;
    }

    public interface onDeleteCityCallback {
        public void onDeleteCitySuccess(String cityName);
    }
}
