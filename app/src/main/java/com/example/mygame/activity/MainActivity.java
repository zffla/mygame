package com.example.mygame.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mygame.R;
import com.example.mygame.adapter.GridAdapter;
import com.example.mygame.util.ScreenUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static String IMAGE_TYPE = "image/*";
    private static int RESULT_IMAGE = 1;
    private static int RESULT_CAMERA = 2;

    private static String TEMP_IMAGE_PATH;
    private GridView mGridView;
    private List<Bitmap> mBitmapList;
    private int[] mBitmapId;
    private int mType = 2;
    private Spinner mSpChoose;

    private static final String[] mChooseType = new String[]{"2 x 2", "3 x 3", "4 x 4"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        TEMP_IMAGE_PATH = getExternalCacheDir() + "/temp.jpg";
        mBitmapList = new ArrayList<>();
        mGridView = findViewById(R.id.gv_main_pic_list);
        mBitmapList = getBitmapList();
        mGridView.setAdapter(new GridAdapter(MainActivity.this, mBitmapList));
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mBitmapList.size() - 1) {
//                    从本地图库中选择图片或者调用相机拍照
//                    Toast.makeText(MainActivity.this,"you clicked the camera",Toast.LENGTH_SHORT).show();
                    chooseFromPhoto();
                } else {
                    chooseFromDefault(position);
                }
            }
        });

        mSpChoose = findViewById(R.id.sp_choose);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mChooseType);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpChoose.setAdapter(adapter);
        mSpChoose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mType = position + 2;
//                Toast.makeText(MainActivity.this, "you clicked the " + mType, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpChoose.setVisibility(View.VISIBLE);
    }

    private void chooseFromDefault(int position) {
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        intent.putExtra("bitmapId", mBitmapId[position]);
        intent.putExtra("type", mType);
        startActivity(intent);
    }

    private void chooseFromPhoto() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("选择");
        dialog.setItems(new String[]{"本地图库", "相机拍照"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri photoUri;
                if (which == 0) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    } else {
                        openAlbum();
                    }

                } else if (which == 1) {
                    File file = new File(TEMP_IMAGE_PATH);
                    try {
                        if (file.exists()) {
                            file.delete();
                        }
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (Build.VERSION.SDK_INT >= 24) {
                        photoUri = FileProvider.getUriForFile(MainActivity.this,
                                "com.example.mygame.fileprovider", file);
                    } else {
                        photoUri = Uri.fromFile(file);
                    }
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(intent, RESULT_CAMERA);
                }
            }
        });
        dialog.create().show();
    }

    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(IMAGE_TYPE);
        startActivityForResult(intent, RESULT_IMAGE);
    }


//    private void showPopup(View view) {
//        int density = (int) ScreenUtil.getDeviceDensity(this);
////        显示PopupView
//        mPopupWindow = new PopupWindow(mPopupView, 200 * density, 50 * density);
//        mPopupWindow.setFocusable(true);
//        mPopupWindow.setOutsideTouchable(true);
////        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
////        获取位置
//        int[] location = new int[2];
//        view.getLocationOnScreen(location);
//        mPopupWindow.showAtLocation(
//                view,
//                Gravity.NO_GRAVITY,
//                location[0] - 40 * density,
//                location[1] + 30 * density
//        );
//    }

    private List<Bitmap> getBitmapList() {
        List<Bitmap> bitmapList = new ArrayList<>();
        mBitmapId = new int[]{
                R.drawable.pic1, R.drawable.pic2,
                R.drawable.pic3, R.drawable.pic4,
                R.drawable.pic5, R.drawable.pic6,
                R.drawable.pic7, R.drawable.pic8,
                R.drawable.pic9, R.drawable.pic10,
                R.drawable.pic11, R.drawable.pic12,
                R.drawable.pic13, R.drawable.pic14,
                R.drawable.pic15, R.drawable.camera
        };
        Bitmap[] bitmaps = new Bitmap[mBitmapId.length];
        for (int i = 0; i < bitmaps.length; i++) {
            bitmaps[i] = BitmapFactory.decodeResource(getResources(), mBitmapId[i]);
            bitmapList.add(bitmaps[i]);
        }
        return bitmapList;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_IMAGE && data != null) {
                String imagePath = null;
                if (Build.VERSION.SDK_INT >= 19) {
                    Uri uri = data.getData();
                    if (DocumentsContract.isDocumentUri(this, uri)) {
                        String docId = DocumentsContract.getDocumentId(uri);
                        if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                            String id = docId.split(":")[1];
                            String selection = MediaStore.Images.Media._ID + "=" + id;
                            imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                        } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                            Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://" +
                                    "downloads/public_downloads"), Long.valueOf(docId));
                            imagePath = getImagePath(contentUri, null);
                        }
                    } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                        imagePath = getImagePath(uri, null);
                    } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                        imagePath = uri.getPath();
                    }

                } else {
                    Uri uri = data.getData();
                    imagePath = getImagePath(uri, null);
                }

                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("picPath", imagePath);
                intent.putExtra("type", mType);
                startActivity(intent);
            } else if (requestCode == RESULT_CAMERA) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("picPath", TEMP_IMAGE_PATH);
                intent.putExtra("type", mType);
                startActivity(intent);
            }
        }
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "you denied the permission.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.tv_type_2:
//                mType=2;
//                mTvChoose.setText("2 x 2");
//                break;
//            case R.id.tv_type_3:
//                mType=3;
//                mTvChoose.setText("3 x 3");
//                break;
//            case R.id.tv_type_4:
//                mType=4;
//                mTvChoose.setText("4 x 4");
//                break;
//        }
//        mPopupWindow.dismiss();
//        Toast.makeText(this,"you clicked the tv choose",Toast.LENGTH_SHORT).show();
//    }
}
