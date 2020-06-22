package com.example.mygame.util;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.example.mygame.R;
import com.example.mygame.activity.GameActivity;
import com.example.mygame.model.ItemBean;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

public class ImagesUtil {

    /**
     * 切分图片，初始化
     * @param type
     * @param picSelected
     * @param context
     */
    public void createInitBitmaps(int type, Bitmap picSelected, Context context) {
        ItemBean itemBean;
        Bitmap bitmap = null;
        List<Bitmap> bitmapList = new ArrayList<>();
        int itemWidth = picSelected.getWidth() / type;
        int itemHeight = picSelected.getHeight() / type;
        for (int i = 0; i < type; i++) {
            for (int j = 0; j < type; j++) {
                bitmap = Bitmap.createBitmap(
                        picSelected,
                        j * itemWidth,
                        i * itemHeight,
                        itemWidth,
                        itemHeight);
                bitmapList.add(bitmap);
                itemBean = new ItemBean(
                        i * type + j + 1,
                        i * type + j + 1,
                        bitmap
                );
                GameUtil.sItemBeans.add(itemBean);
            }
        }
        GameActivity.mLastBitmap = bitmapList.get(type * type - 1);
        bitmapList.remove(type * type - 1);
        GameUtil.sItemBeans.remove(type*type-1);
        Bitmap bitmapBlank= BitmapFactory.decodeResource(context.getResources(), R.drawable.blank);
        bitmapBlank=Bitmap.createBitmap(bitmapBlank,0,0,itemWidth,itemHeight);
        bitmapList.add(bitmapBlank);
        GameUtil.sItemBeans.add(new ItemBean(type*type,0,bitmapBlank));
        GameUtil.mBlankItemBean=GameUtil.sItemBeans.get(type*type-1);

    }


    /**
     * 调整bitmap大小
     * @param newWidth
     * @param newHeight
     * @param bitmap
     * @return
     */
    public Bitmap resizeBitmap(float newWidth, float newHeight, Bitmap bitmap){
        int bitmapWidth=bitmap.getWidth();
        int bitmapHeight=bitmap.getHeight();
        Matrix matrix=new Matrix();
        matrix.postScale(
                newWidth/bitmapWidth,
                newHeight/bitmapHeight
        );
        Bitmap newBitmap=Bitmap.createBitmap(bitmap,0,0,
                bitmapWidth,bitmapHeight,
                matrix,true);
        return newBitmap;
    }
}
