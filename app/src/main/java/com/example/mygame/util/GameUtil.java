package com.example.mygame.util;

import android.graphics.Bitmap;

import com.example.mygame.activity.GameActivity;
import com.example.mygame.model.ItemBean;

import java.util.ArrayList;
import java.util.List;

public class GameUtil {
    public static List<ItemBean> sItemBeans = new ArrayList<>();
    public static ItemBean mBlankItemBean = new ItemBean();


    /**
     * 打乱图片顺序
     * 1.首先随机交互index
     * 2.然后计算倒置和
     * 3.最后基于计算结果判断是否有解
     */
    public static void getGameGenerator() {
        int index = 0;
        for (int i = 0; i < sItemBeans.size(); i++) {
            index = (int) (Math.random() * GameActivity.TYPE * GameActivity.TYPE);
            swapItems(sItemBeans.get(index), mBlankItemBean);
        }
        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < sItemBeans.size(); i++) {
            data.add(sItemBeans.get(i).getBitmapId());
        }
        if (canSolve(data)) {
            return;
        } else {
            getGameGenerator();
        }
    }


    /**
     * 判断是否有解
     *
     * @param data
     * @return
     */
    private static boolean canSolve(List<Integer> data) {
        int blankId = GameUtil.mBlankItemBean.getItemId();
        if (sItemBeans.size() % 2 == 1) {
            return getInversions(data) % 2 == 0;
        } else {
            if (((blankId - 1) / GameActivity.TYPE) % 2 == 1) {
//                从下往上数位于奇数行
                return getInversions(data) % 2 == 0;
            } else {
                return getInversions(data) % 2 == 1;
            }
        }
    }


    /**
     * 计算倒置和
     *
     * @param data
     * @return
     */
    private static int getInversions(List<Integer> data) {
        int sum = 0;
        for (int i = 0; i < data.size(); i++) {
            int count = 0;
            for (int j = i + 1; j < data.size(); j++) {
                if (data.get(j) != 0 && data.get(j) < data.get(i)) {
                    count++;
                }
            }
            sum += count;
        }
        return sum;
    }


    /**
     * 交换空格与指定item的位置
     *
     * @param itemBean
     * @param mBlankItemBean
     */
    public static void swapItems(ItemBean itemBean, ItemBean mBlankItemBean) {
        ItemBean tempItem = new ItemBean();
        tempItem.setBitmapId(itemBean.getBitmapId());
        tempItem.setBitmap(itemBean.getBitmap());

        itemBean.setBitmapId(mBlankItemBean.getBitmapId());
        itemBean.setBitmap(mBlankItemBean.getBitmap());

        mBlankItemBean.setBitmapId(tempItem.getBitmapId());
        mBlankItemBean.setBitmap(tempItem.getBitmap());

        GameUtil.mBlankItemBean = itemBean;
    }


    /**
     * 判断点击当前位置是否可移动
     *
     * @param position
     * @return
     */
    public static boolean isMoveable(int position) {
        int type = GameActivity.TYPE;
        int blankId = GameUtil.mBlankItemBean.getItemId() - 1;
//        不同行相差type
        if (Math.abs(blankId - position) == type) {
            return true;
        }
//        相同行相差1
        if (blankId / type == position / type && Math.abs(blankId - position) == 1) {
            return true;
        }
        return false;
    }


    /**
     * 判断是否拼图成功
     * @return
     */
    public static boolean isSucceed(){
        for (ItemBean itemBean:GameUtil.sItemBeans){
            if (itemBean.getBitmapId()!=0 &&itemBean.getBitmapId()==itemBean.getItemId()){
                continue;
            }else if (itemBean.getBitmapId()==0&&itemBean.getItemId()==GameActivity.TYPE*GameActivity.TYPE){
                continue;
            }else {
                return false;
            }
        }
        return true;
    }
}
