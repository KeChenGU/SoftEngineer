package com.android.keche.baidutieba.othters;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.keche.baidutieba.MainActivity;
import com.android.keche.baidutieba.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.load.resource.drawable.GlideDrawable;
//import com.bumptech.glide.request.animation.GlideAnimation;
//import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;

public class SelectDemoActivity extends AppCompatActivity implements View.OnClickListener {

    private SystemPictureSelector pictureSelector;
    private String savedPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + String.valueOf(System.currentTimeMillis()) + ".png";
    private Button button;
    private FrameLayout container;
    private ImageView imageView;
    private static final int REQUEST_TAKE_PHOTO_PERMISSION = 1112;
    private static final int REQUEST_WR_EXTERNAL_PERMISSION = 1111;

    public static final String TAG = "SelectDemoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_demo);
        initView();
        isGrantExternalRW(this);
    }

    private void initView() {

        button = findViewById(R.id.btn_select);
        //container = findViewById(R.id.image_container);
        imageView = findViewById(R.id.image_selected);
        imageView.setOnClickListener(this);
        button.setOnClickListener(this);

        SystemPictureSelector.Builder builder = new SystemPictureSelector.Builder(this);
        builder.isCropped(true)
                .setCropSize(3, 2)
                .setOutputSize(1200, 1200)
                .setOutputPath(savedPath)
                .setOnSelectListener(new SystemPictureSelector.OnSystemPictureSelectListener() {
                    @Override
                    public void onSelectedSuccess(File file) {

                        Uri uri = Uri.fromFile(file);
                        Log.e(TAG, "onSelectedSuccess: "+uri.toString() );

//                        Glide.clear(imageView);
//                        Glide.with(SelectDemoActivity.this)
//                                .load(file)
//                                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                                .into(new SimpleTarget<GlideDrawable>() {
//                                    @Override
//                                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                                        imageView.setImageDrawable(resource);
//                                    }
//                                });
                        imageView.setImageBitmap(null);
                        Glide.with(SelectDemoActivity.this)
                                .load(file)
                                .into(imageView);

                    }

                    @Override
                    public void onSelectedMessage(String msg) {

                        Toast.makeText(SelectDemoActivity.this, msg, Toast.LENGTH_LONG).show();
                    }
                });
        pictureSelector = builder.create();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        pictureSelector.bindingActivityForResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.image_selected:

                showSelectDialog();
                break;

            case R.id.btn_select:

                showSelectDialog();
                break;
        }
    }


    private void showSelectDialog(){
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        View selectView = LayoutInflater.from(this).inflate(R.layout.dialog_layout,null);
        TextView bt_camera = selectView.findViewById(R.id.btn_by_camera);
        TextView bt_gallery = selectView.findViewById(R.id.btn_by_gallery);
        TextView bt_cancel = selectView.findViewById(R.id.btn_cancel);
        dialog.setContentView(selectView);
        /**
         * 解决bsd显示不全的情况
         */
        View parent = (View) selectView.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        selectView.measure(0,0);
        behavior.setPeekHeight(selectView.getMeasuredHeight());

        bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                if (ContextCompat.checkSelfPermission(SelectDemoActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请摄像头权限
                    ActivityCompat.requestPermissions(SelectDemoActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            REQUEST_TAKE_PHOTO_PERMISSION);

                } else {
                    pictureSelector.getSystemPhotoByCamera();
                }
            }
        });

        bt_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                pictureSelector.getSystemPhotoByGallery();
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_WR_EXTERNAL_PERMISSION) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        //授权成功后的逻辑
                        Toast.makeText(SelectDemoActivity.this, "成功获取读取权限！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SelectDemoActivity.this,"您拒绝了权限，该功能不可用",Toast.LENGTH_SHORT).show();
                    }
                }

                if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        //授权成功后的逻辑
                        Toast.makeText(SelectDemoActivity.this, "成功获取写入权限权限！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SelectDemoActivity.this,"您拒绝了权限，该功能不可用",Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }
        if (requestCode == REQUEST_TAKE_PHOTO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //申请成功，可以拍照
                if (!isGrantExternalRW(SelectDemoActivity.this)) {
                    return;
                }
                pictureSelector.getSystemPhotoByCamera();
            } else {
                Toast.makeText(SelectDemoActivity.this,"您拒绝了权限，该功能不可用\n可在应用设置里授权拍照哦",Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || (activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))) {
            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
            }, REQUEST_WR_EXTERNAL_PERMISSION);
            return false;
        }
        return true;
    }

}
