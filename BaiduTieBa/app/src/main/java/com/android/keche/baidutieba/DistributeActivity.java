package com.android.keche.baidutieba;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.keche.baidutieba.serials.FirstPageDistribution;
import com.android.keche.baidutieba.utils.PhotoUtils;
import com.android.keche.baidutieba.utils.ToastUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author 谷恪忱
 * @brief 发帖活动
 * 本次课程的核心功能
 */
public class DistributeActivity extends AppCompatActivity implements View.OnClickListener {

    public static boolean DISTRIBUTED_FLAG = true;

    private static final int MODE_TEXT = 0;         /**< 文本模式 **/

    private static final int MODE_PICTURE = 1;      /**< 拍摄模式 **/

    private static final int MODE_ALBUM = 2;        /**< 相册模式 **/

    private static final int MODE_ONLINE = 3;       /**< 直播模式 **/

    private static final int PADDING = 20;          /**< 内间距 **/

    private static final int CODE_DISTRIBUTE_REQUEST = 0x00;

    private static final int CODE_GALLERY_REQUEST = 0xA0;   /**< 相册请求码 **/

    private static final int CODE_CAMERA_REQUEST = 0xA1;    /**< 拍照请求码 **/

    private static final int CODE_RESULT_REQUEST = 0xA2;    /**< 处理结果请求码 **/

    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;    /**< 拍照权限请求码 **/

    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;   /**< 存储权限请求码 **/

    private static List<ImageView> shareImages = new ArrayList<>();     /**< 分享的图片视图表 **/

    private static List<Bitmap> dealBitmaps = new ArrayList<>();        /**< 处理后的位图，用于填装到shareImages表 **/

    private static List<String> imageURls = new ArrayList<>();

    private static String localPath = "";

    private String currentTimePic = UUID.randomUUID() + ".jpg";

    private File fileUri = new File(Environment
                    .getExternalStorageDirectory().getPath() + "/" + currentTimePic);

    private File fileCropUri = new File(Environment
            .getExternalStorageDirectory().getPath() + "/crop" + currentTimePic);

    private Uri imageUri;   /**< 原图Uri **/

    private Uri cropImageUri;   /**< 裁剪图片Uri **/

    private RelativeLayout distributeFrame;     /**< 分布框架 **/

    private RelativeLayout scrollFrame;     /**< 滑动框架 **/

    private LinearLayout shareGalley;

    private EditText editText;      /**< 编辑文本 **/

    private TextView distribute;    /**< 发布 **/

    private ImageView demoImage;    /**< 分享图片框架 **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distribute);
        if (getIntent() != null) {
            if (getIntent().getBooleanExtra("camera", false)) {
                autoObtainCameraPermission();
            }
        }
        distributeFrame = (RelativeLayout) findViewById(R.id.distribute_layout);
        scrollFrame = (RelativeLayout) findViewById(R.id.scroll_layout);
        shareGalley = (LinearLayout) findViewById(R.id.dist_image_galley);
        editText = (EditText) findViewById(R.id.dist_edit_text);
        distribute = (TextView) findViewById(R.id.dist_text_dist);
        demoImage = (ImageView) findViewById(R.id.add_photo_frame);
        distribute.setOnClickListener(this);
        demoImage.setOnClickListener(this);
        //shareImages.add(demoImage);
        setDistributeAutoScroll();

    }

    /**
     * @brief 检查设备是否存在SDCard的工具方法
     * @retval true 若有SD卡
     * @retval false 没有SD卡
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dist_text_dist:
                Intent intent = new Intent(this, MainActivity.class);
                String content = editText.getText().toString();
                if (content != null && !content.equals("")) {
                    //Bitmap userIcon = ImageDealUtil.drawable2Bitmap(this.getResources().getDrawable(R.drawable.distribute));
                    //List<Bitmap> bitmaps = new ArrayList<>();
                    //bitmaps.add(ImageDealUtil.drawable2Bitmap(this.getResources().getDrawable(R.drawable.album)));
                    //bitmaps.add(ImageDealUtil.drawable2Bitmap(this.getResources().getDrawable(R.drawable.smssdk_arrow_right)));
//                    List<String> imageStrings = new ArrayList<>();
//                    imageStrings.add(
//                            ImageDealUtil.convertIconToString(
//                                ImageDealUtil.drawable2Bitmap(this.getResources().getDrawable(R.drawable.album)
//                                )
//                            )
//                    );
//                    imageStrings.add(
//                            ImageDealUtil.convertIconToString(
//                                    ImageDealUtil.drawable2Bitmap(this.getResources().getDrawable(R.drawable.smssdk_arrow_right)
//                                    )
//                            )
//                    )
                    DISTRIBUTED_FLAG = false;
                    if (MainActivity.user == null) {
                        Toast.makeText(DistributeActivity.this, "请先登录！", Toast.LENGTH_SHORT).show();
                        this.finish();
                        return;
                    }
                    String userName = MainActivity.user == null ? "123456" : MainActivity.user.getName();
                    String iconUrl = MainActivity.userEx == null ? "userIcon/face_happy.png" : MainActivity.userEx.getIconUrl();
                    String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
                    //List<String> imageURLs = new ArrayList<>();
                    //imageURLs.add(localPath);
                    intent.putExtra("Distribution",
                            new FirstPageDistribution(
                                    date,
                                    null,
                                    "",
                                    content,
                                    userName,
                                    // userIcon,
                                    iconUrl,
                                    0,
                                    0,
                                    0,
                                    imageURls//new ArrayList<String>()//imageStrings
                                    ));
                    startActivityForResult(intent, CODE_DISTRIBUTE_REQUEST);
                    this.finish();
                } else {
                    ToastUtils.showShort(this, "输入内容不能为空！");
                }
                imageURls.clear();
                break;
            case R.id.add_photo_frame:
                autoObtainStoragePermission();
                break;
        }
    }

    /**
     * @brief 处理请求权限结果
     * @param requestCode 请求码
     * @param permissions 权限
     * @param grantResults 授权结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            //调用系统相机申请拍照权限回调
            case CAMERA_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(DistributeActivity.this,
                                    "com.android.keche.baidutieba", fileUri);//通过FileProvider创建一个content类型的Uri
                        PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        ToastUtils.showShort(this, "设备没有SD卡！");
                    }
                } else {

                    ToastUtils.showShort(this, "请允许打开相机！！");
                }
                break;
            }
            //调用系统相册申请Sdcard权限回调
            case STORAGE_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                } else {

                    ToastUtils.showShort(this, "请允许打开操作SDCard！！");
                }
                break;
            default:
        }
    }

    /**
     * @brief 用于处理输入法键盘弹出时按钮被遮住情况
     * @note 内部使用View的scrollTo方法
     */
    private void setDistributeAutoScroll() {
        distributeFrame.getViewTreeObserver().addOnGlobalLayoutListener (
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int screenHeight = getWindow().getDecorView().getHeight();
                        Rect rect = new Rect();
                        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
//                    Log.d("Test1:", screenHeight + " :: " + rect.bottom);
                        if (screenHeight - rect.bottom > 100) {
//                        Log.d("Test2:", rect.left + ":" + rect.top
//                        + ":" + rect.right + ":" + rect.bottom);
//                        int[] location = new int[2];
//                        int[] slocation = new int[2];
//                        distributeFrame.getLocationInWindow(location);
//                        scrollFrame.getLocationInWindow(slocation);
//                        Log.d("Test3:", location[0] + " | " + location[1] + " : " + slocation[0] + " | " + slocation[1]);
                            scrollFrame.scrollTo(0, (screenHeight - rect.bottom) - 50); // 相对于自身
                            // scrollFrame.scrollBy();
                        } else {
                            scrollFrame.scrollTo(0, 0);
                        }

                    }
                }
        );
    }

    private static final int OUTPUT_X = 560;    /**< 输出图像宽 **/
    private static final int OUTPUT_Y = 560;    /**< 输出图像高 **/

    /**
     * @brief 处理
     * @param requestCode 请求码
     * @param resultCode 结果码
     * @param data 回调结果数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //拍照完成回调
                case CODE_CAMERA_REQUEST:
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri,
                            1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    //url = PhotoUtils.getPath(this, data.getData());
                    localPath = cropImageUri.getPath();
                    break;
                //访问相册完成回调
                case CODE_GALLERY_REQUEST:
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            newUri = FileProvider.getUriForFile(this,
                                    "com.android.keche.baidutieba", new File(newUri.getPath()));
                            Log.d("url_path:", newUri.getPath());
                            Log.d("url_path2:", PhotoUtils.getPath(this, data.getData()));
                            Log.d("url_path3:", data.getData()+"");
                            localPath = PhotoUtils.getPath(this, data.getData()).substring(8);
//                            url1 = newUri.getPath();
//                            url2 = PhotoUtils.getPath(this, data.getData());
//                            url3 = data.getData() + "";
//                            ToastUtils.showLong(this, "url1" + newUri.getPath());
//                            ToastUtils.showLong(this, "url1" + PhotoUtils.getPath(this, data.getData()));
//                            ToastUtils.showLong(this, data.getData() + "");

                        } else {
                            //ToastUtils.showShort(this, fileCropUri.getPath());
                            Log.d("file_path", fileCropUri.getPath());
                            Log.d("file_path_parse", PhotoUtils.getPath(this, data.getData()));
                            //url = PhotoUtils.getPath(this, data.getData()).substring(8);
                            Log.d("file_path_uri", data.getData() + "");
                            localPath = PhotoUtils.getPath(this, data.getData()).substring(8);

                        }
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri,
                                1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    } else {
                        ToastUtils.showShort(this, "设备没有SD卡！");
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    //                    if (data != null) {
//                        Log.d("", PhotoUtils.getPath(this, data.getData()));
//                        //Log.d("pic_real_path", data.getData() + "");
//                    }
//                    Log.d("pic_path", cropImageUri.getPath());
                    //Log.d("pic_path_real", PhotoUtils.getPath(this, cropImageUri));
                    //url = cropImageUri.getPath();
                    //Log.d("pic_path", url);
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    if (bitmap != null) {
                        if (!dealBitmaps.contains(demoImage)) {
                            showImages(demoImage, bitmap);
                            //dealBitmaps.add(bitmap);
                            imageURls.add(localPath);
                            addImageView();
                        }
                    }
                    break;
                default:
            }
        }
    }


    /**
     * @brief 自动获取相机权限
     */
    private void autoObtainCameraPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ToastUtils.showShort(this, "您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                    CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (hasSdcard()) {
                imageUri = Uri.fromFile(fileUri);
                //通过FileProvider创建一个content类型的Uri
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUri = FileProvider.getUriForFile(DistributeActivity.this,
                            "com.android.keche.baidutieba", fileUri);
                }
                //url = imageUri.getPath();
                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
            } else {
                ToastUtils.showShort(this, "设备没有SD卡！");
            }
        }
    }

    /**
     * @brief 自动获取sd卡权限
     * 即访问相册的存储权限
     */
    private void autoObtainStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);

        } else {
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
        }

    }

    /**
     * @brief 将从拍摄或从相册选择的图片显示在分享框上
     * @param bitmap 位图
     *
     */
    private void showImages(ImageView imageFrame, Bitmap bitmap) {
        imageFrame.setImageBitmap(bitmap);
    }

    private void addImageView() {
        ImageView imageView = new ImageView(shareGalley.getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(80 * TypedValue.COMPLEX_UNIT_SP,
                80 * TypedValue.COMPLEX_UNIT_SP);
        layoutParams.setMargins(0, 0,  10 * TypedValue.COMPLEX_UNIT_SP,  0);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(R.drawable.photo_frame);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoObtainStoragePermission();
            }
        });
        shareGalley.addView(imageView);
    }

}
