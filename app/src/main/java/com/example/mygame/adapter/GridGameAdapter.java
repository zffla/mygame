package com.example.mygame.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.mygame.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

public class GridGameAdapter extends BaseAdapter {
    private List<Bitmap> mBitmapList;
    private Context mContext;

    public GridGameAdapter(Context context, List<Bitmap> list) {
        mBitmapList = list;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mBitmapList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBitmapList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = null;
        if (convertView == null) {
            imageView = new ImageView(mContext);

            imageView.setLayoutParams(new GridView.LayoutParams(
                    mBitmapList.get(position).getWidth(),
                    mBitmapList.get(position).getHeight()
            ));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        } else {
            imageView = (ImageView) convertView;
        }
//        imageView.setBackgroundColor(Color.BLACK);
        imageView.setImageBitmap(mBitmapList.get(position));
        return imageView;
    }
}

