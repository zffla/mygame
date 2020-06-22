package com.example.mygame.model;


import android.graphics.Bitmap;

public class ItemBean {

    private int mItemId;
    private int mBitmapId;
    private Bitmap mBitmap;

    public ItemBean(int itemId,int bitmapId,Bitmap bitmap){
        mItemId=itemId;
        mBitmapId=bitmapId;
        mBitmap=bitmap;
    }

    public ItemBean(){

    }

    public int getItemId() {
        return mItemId;
    }

    public void setItemId(int itemId) {
        mItemId = itemId;
    }

    public int getBitmapId() {
        return mBitmapId;
    }

    public void setBitmapId(int bitmapId) {
        mBitmapId = bitmapId;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }
}
