package com.example.mygame.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mygame.R;
import com.example.mygame.adapter.GridAdapter;
import com.example.mygame.adapter.GridGameAdapter;
import com.example.mygame.model.ItemBean;
import com.example.mygame.util.GameUtil;
import com.example.mygame.util.ImagesUtil;
import com.example.mygame.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity implements View.OnClickListener{
    public static Bitmap mLastBitmap;
    public static int TYPE;

    private String mPicPath;
    private int mResId;
    private Bitmap mSelectBitmap;
    private List<Bitmap> mBitmapList = new ArrayList<>();

    private int stepCount = 0;
    private int timerCount = 0;

    private TextView mTvTimer;
    private TextView mTvStep;

    private GridView mGvPic;
    private Button mBtnOrigin;
    private Button mBtnReset;
    private Button mBtnExit;
    private GridGameAdapter mAdapter;


    private Timer mTimer;
    private TimerTask mTimerTask;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    timerCount++;
                    mTvTimer.setText(" "+timerCount);
                    break;
                default:
                    break;
            }
        }
    };

    private ImageView mImageView;
    private boolean isShowPic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        init();
    }

    private void init() {
        isShowPic=false;
        TYPE = getIntent().getIntExtra("type", 2);
        mPicPath = getIntent().getStringExtra("picPath");
        mResId = getIntent().getIntExtra("bitmapId", 0);
        Bitmap picTemp;
        if (mResId != 0) {
//            选择默认图片时
            picTemp = BitmapFactory.decodeResource(getResources(), mResId);
        } else {
//            选择本地图库图片时
            picTemp = BitmapFactory.decodeFile(mPicPath);
        }
//        调整bitmap大小
        handleBitmap(picTemp);
        initView();

//        生成游戏数据
        generateGame();
        mGvPic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (GameUtil.isMoveable(position)) {
                    GameUtil.swapItems(GameUtil.sItemBeans.get(position), GameUtil.mBlankItemBean);
                    recreateData();
                    mAdapter.notifyDataSetChanged();
                    stepCount++;
                    mTvStep.setText(" "+stepCount);
                    if (GameUtil.isSucceed()) {
                        recreateData();
                        mBitmapList.remove(TYPE * TYPE - 1);
                        mBitmapList.add(mLastBitmap);
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(GameActivity.this, "Congratulations！You succeed！", Toast.LENGTH_SHORT).show();
                        mGvPic.setEnabled(false);
                        mTimer.cancel();
                        mTimerTask.cancel();
                    }
                }
            }
        });
//        GridAdapter adapter=new GridAdapter(thi);
//        mGvPic.setAdapter();

    }

    private void recreateData() {
        mBitmapList.clear();
        for (ItemBean itemBean : GameUtil.sItemBeans) {
            mBitmapList.add(itemBean.getBitmap());
        }
    }

    private void initView() {
        mTvStep = findViewById(R.id.tv_step_count);
        mTvTimer = findViewById(R.id.tv_timer);

        mGvPic = findViewById(R.id.gv_game_pic);
        mGvPic.setNumColumns(TYPE);
        RelativeLayout.LayoutParams gridParams = new RelativeLayout.LayoutParams(
                mSelectBitmap.getWidth(),
                mSelectBitmap.getHeight());
        // 水平居中
        gridParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        // 其他格式属性
        gridParams.addRule(
                RelativeLayout.BELOW,
                R.id.ll_game_info_show);
        // Grid显示
        mGvPic.setLayoutParams(gridParams);
        mGvPic.setHorizontalSpacing(0);
        mGvPic.setVerticalSpacing(0);

        mBtnExit = findViewById(R.id.btn_game_exit);
        mBtnOrigin = findViewById(R.id.btn_game_origin_pic);
        mBtnReset = findViewById(R.id.btn_game_reset);

        mBtnReset.setOnClickListener(this);
        mBtnOrigin.setOnClickListener(this);
        mBtnExit.setOnClickListener(this);

        addImageView();
    }

    private void addImageView() {
        RelativeLayout layout=findViewById(R.id.rl_game);
        mImageView=new ImageView(GameActivity.this);
        mImageView.setImageBitmap(mSelectBitmap);
        int width= (int) (mSelectBitmap.getWidth()*0.9f);
        int height= (int) (mSelectBitmap.getHeight()*0.9f);
        RelativeLayout.LayoutParams layoutParams= new RelativeLayout.LayoutParams(width,height);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mImageView.setLayoutParams(layoutParams);
        layout.addView(mImageView);
        mImageView.setVisibility(View.GONE);
    }

    private void generateGame() {
        new ImagesUtil().createInitBitmaps(TYPE, mSelectBitmap, this);
        GameUtil.getGameGenerator();
        for (ItemBean itemBean : GameUtil.sItemBeans) {
            mBitmapList.add(itemBean.getBitmap());
        }
        mAdapter = new GridGameAdapter(this, mBitmapList);
        mGvPic.setAdapter(mAdapter);

//        计时器
        mTimer = new Timer(true);
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                mHandler.sendMessage(message);
            }
        };
        mTimer.schedule(mTimerTask, 0, 1000);
    }

    private void handleBitmap(Bitmap bitmap) {
        int width = ScreenUtil.getScreenSize(this).widthPixels;
        int height = ScreenUtil.getScreenSize(this).heightPixels;
        mSelectBitmap = new ImagesUtil().resizeBitmap(
                0.8f * width,
                0.7f * height,
                bitmap
        );
    }


    @Override
    protected void onStop() {
        super.onStop();
        GameUtil.sItemBeans.clear();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_game_exit:
                GameActivity.this.finish();
                break;
            case R.id.btn_game_reset:
                reset();
                break;
            case R.id.btn_game_origin_pic:
                showOriginPic();
                break;
        }
    }

    private void showOriginPic() {
        if (isShowPic){
            ObjectAnimator animator=ObjectAnimator.ofFloat(mImageView,"translationY",0,-2);
            animator.setDuration(1000);
            animator.start();
            mImageView.setVisibility(View.GONE);
            isShowPic=false;
        }else {
            ObjectAnimator animator=ObjectAnimator.ofFloat(mImageView,"translationY",-2,0);
            animator.setDuration(1000);
            animator.start();
            mImageView.setVisibility(View.VISIBLE);
            isShowPic=true;
        }
    }

    private void reset() {
        mTvStep.setText("0");
        mTvTimer.setText("0");
        clearAll();
        generateGame();
        mAdapter.notifyDataSetChanged();
        mGvPic.setEnabled(true);
    }


    private void clearAll(){
        GameUtil.sItemBeans.clear();
        mBitmapList.clear();
        timerCount=0;
        stepCount=0;
        mTimer.cancel();
        mTimerTask.cancel();

    }
}
