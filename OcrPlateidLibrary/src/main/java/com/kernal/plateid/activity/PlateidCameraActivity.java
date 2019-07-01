package com.kernal.plateid.activity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.kernal.plateid.CoreSetup;
import com.kernal.plateid.R;
import com.kernal.plateid.controller.CameraManager;
import com.kernal.plateid.controller.CommonTools;
import com.kernal.plateid.controller.ScreenRotation;
import com.kernal.plateid.view.PlateidSurfaceView;
import com.kernal.plateid.view.VerticalSeekBar;
import com.kernal.plateid.view.ViewfinderView;

/***
 * @author user
 * @date 201812-20
 * 预览拍照识别Activity
 */
public class PlateidCameraActivity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private PlateidSurfaceView mSurfaceView;
    private ImageView backBtn, flashBtn, takePicBtn;
    private SeekBar seekBar;
    private VerticalSeekBar verticalSeekBar;
    private int recordProgress = 0;
    private Point srcPoint;
    private CommonTools commonTools;
    ScreenRotation screenRotation;
    ViewfinderView myView;
    RelativeLayout.LayoutParams prameLayoutPm;
    private CoreSetup coreSetup;
    private CameraManager cameraManager;
    @SuppressLint("InlinedApi")
    static final String[] PERMISSION = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA};
    private static final int PERMISSION_REQUESTCODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        commonTools = new CommonTools();
        srcPoint = commonTools.getScreenSize(this);
        Intent intent = getIntent();
        coreSetup = (CoreSetup) intent.getSerializableExtra("coreSetup");
    }

    @Override
    protected void onResume() {
        super.onResume();
        commonTools.hiddenVirtualButtons(getWindow().getDecorView());
         //动态授权申请
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //没有授权
            ActivityCompat.requestPermissions(this, PERMISSION, PERMISSION_REQUESTCODE);
        } else {
            //已经授权
            cameraManager = new CameraManager(this);
            initView();
            screenRotation = new ScreenRotation(this, mHandler);
        }
    }

    @SuppressLint("WrongConstant")
    public void initView() {
        //预览画面布局
        //必须放在onResume中，不然会出现Home键之后，再回到该APP，黑屏
        mSurfaceView = new PlateidSurfaceView(this, srcPoint, cameraManager, coreSetup);
        //识别框布局
        myView = new ViewfinderView(this, srcPoint, true);
        //返回按钮图标布局
        backBtn = new ImageView(this);
        backBtn.setImageResource(R.mipmap.back_btn);
        backBtn.setOnClickListener(this);
        RelativeLayout relativeLayout = new RelativeLayout(this);
        prameLayoutPm = new RelativeLayout.LayoutParams((int) (srcPoint.x * 0.12), (int) (srcPoint.x * 0.12));
        prameLayoutPm.leftMargin = (int) (srcPoint.x * 0.07);
        prameLayoutPm.topMargin = (int) (srcPoint.x * 0.07);
        backBtn.setLayoutParams(prameLayoutPm);
        relativeLayout.addView(backBtn);

        //闪光灯图标布局
        flashBtn = new ImageView(this);
        flashBtn.setImageResource(R.mipmap.flashbtn_on);
        flashBtn.setOnClickListener(this);
        prameLayoutPm = new RelativeLayout.LayoutParams((int) (srcPoint.x * 0.12), (int) (srcPoint.x * 0.12));
        prameLayoutPm.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        prameLayoutPm.rightMargin = (int) (srcPoint.x * 0.07);
        prameLayoutPm.topMargin = (int) (srcPoint.x * 0.07);
        flashBtn.setLayoutParams(prameLayoutPm);
        relativeLayout.addView(flashBtn);

        //拍照按钮图标布局
        takePicBtn = new ImageView(this);
        takePicBtn.setImageResource(R.mipmap.tack_pic_btn);
        takePicBtn.setOnClickListener(this);
        prameLayoutPm = new RelativeLayout.LayoutParams((int) (srcPoint.x * 0.15), (int) (srcPoint.x * 0.15));
        prameLayoutPm.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        prameLayoutPm.addRule(RelativeLayout.CENTER_HORIZONTAL);
        prameLayoutPm.bottomMargin = (int) (srcPoint.y * 0.07);
        takePicBtn.setLayoutParams(prameLayoutPm);
        relativeLayout.addView(takePicBtn);

        //竖屏变焦滑动
        seekBar = new SeekBar(this);
        seekBar.setThumb(getResources().getDrawable(R.drawable.seekbar_thump));
        //设置进度条颜色、样式
        seekBar.getProgressDrawable().setColorFilter(Color.parseColor("#2babac"), PorterDuff.Mode.SRC_ATOP);
        seekBar.setThumbOffset(0);
        seekBar.setOnSeekBarChangeListener(this);
        prameLayoutPm = new RelativeLayout.LayoutParams((int) (srcPoint.x * 0.9), RelativeLayout.LayoutParams.WRAP_CONTENT);
        prameLayoutPm.addRule(RelativeLayout.CENTER_HORIZONTAL);
        prameLayoutPm.topMargin = (int) (srcPoint.y * 0.7);
        seekBar.setLayoutParams(prameLayoutPm);
        relativeLayout.addView(seekBar);

        //横屏变焦滑动
        verticalSeekBar = new VerticalSeekBar(this);
        verticalSeekBar.setThumb(getResources().getDrawable(R.drawable.seekbar_thump));
        //设置进度条颜色、样式
        verticalSeekBar.getProgressDrawable().setColorFilter(Color.parseColor("#2babac"), PorterDuff.Mode.SRC_ATOP);
        verticalSeekBar.setThumbOffset(0);
        verticalSeekBar.setOnSeekBarChangeListener(this);
        prameLayoutPm = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, (int) (srcPoint.y * 0.58));
        prameLayoutPm.leftMargin = (int) (srcPoint.x * 0.1);
        prameLayoutPm.topMargin = (int) (srcPoint.y * 0.2);
        verticalSeekBar.setLayoutParams(prameLayoutPm);
        verticalSeekBar.setVisibility(View.GONE);
        relativeLayout.addView(verticalSeekBar);

        addContentView(mSurfaceView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addContentView(myView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addContentView(relativeLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        if (!coreSetup.takePicMode) {
            takePicBtn.setVisibility(View.GONE);
        }
    }

    /**
     * 拍照缩放动画
     */
    public void startAnim() {
        mHandler.sendEmptyMessage(1);
    }

    /***
     * 获取到结果
     * @param activity 实体类
     * @param recogResult 识别结果
     * @param savePicturePath 保存图片路径
     */
    public void getResultFinish(Activity activity,String[] recogResult, int screenDirection, String savePicturePath) {
        Intent intent = new Intent();
        intent.putExtra("RecogResult", recogResult);
        intent.putExtra("savePicturePath", savePicturePath);
        intent.putExtra("screenDirection",screenDirection);
        activity.setResult(RESULT_OK, intent);
        activity.finish();

    }


    @Override
    public void onClick(View v) {
        if (v == backBtn) {
            //返回键点击事件
            this.finish();
        } else if (v == flashBtn) {
            //闪光灯键点击事件
            mSurfaceView.controlFlash();
        } else if (v == takePicBtn) {
            //拍照按钮点击事件
            mSurfaceView.isTakePicOnclick(true);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            this.finish();
            return true;
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraManager != null) {
            cameraManager.removeCamera();
        }
        if (screenRotation != null) {
            screenRotation.recoverySensorManager();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        recordProgress = progress;
        cameraManager.setFocusLength((int) (cameraManager.getFocus() * progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                if (screenRotation.rotateTop) {
                    rotateAnimation(0, 0);
                    changView(true);
                } else if (screenRotation.rotateLeft) {
                    rotateAnimation(90, 90);
                    changView(false);
                } else if (screenRotation.rotateRight) {
                    rotateAnimation(-90, -90);
                    changView(false);
                } else if (screenRotation.rotateBottom) {
                    rotateAnimation(180, 0);
                    changView(true);
                }
                mSurfaceView.screenRotationChange(screenRotation.rotateLeft, screenRotation.rotateRight, screenRotation.rotateTop, screenRotation.rotateBottom);
            } else if (msg.what == 1) {
                //缩放动画
                commonTools.myViewScaleAnimation(myView);
            }
        }
    };

    /***
     * 旋转动画
     * @param toDegrees 旋转角度
     * 旋转的结束角度
     * @param toDegrees2 拍照按钮旋转角度
     *
     */
    public void rotateAnimation(int toDegrees, int toDegrees2) {
        flashBtn.animate().rotation(toDegrees).setDuration(500).start();
        backBtn.animate().rotation(toDegrees).setDuration(500).start();
        takePicBtn.animate().rotation(toDegrees2).setDuration(500).start();
    }

    /**
     * 改变屏幕布局  根据横竖屏状态修改布局
     *
     * @param isPortrait 横屏还是竖屏
     */
    public void changView(boolean isPortrait) {
        changeState(isPortrait);
        if (screenRotation.rotateLeft) {
            seekBar.setVisibility(View.GONE);
            verticalSeekBar.setVisibility(View.VISIBLE);
            prameLayoutPm.leftMargin = (srcPoint.x / 10);
            verticalSeekBar.setLayoutParams(prameLayoutPm);
            verticalSeekBar.setProgress(recordProgress);
            //控件翻转180度
            ObjectAnimator.ofFloat(verticalSeekBar, "rotationX", 0, 180).setDuration(0).start();
        } else if (screenRotation.rotateRight) {
            seekBar.setVisibility(View.GONE);
            verticalSeekBar.setVisibility(View.VISIBLE);
            prameLayoutPm.leftMargin = (srcPoint.x * 4 / 5);
            verticalSeekBar.setLayoutParams(prameLayoutPm);
            verticalSeekBar.setProgress(recordProgress);
        } else {
            seekBar.setVisibility(View.VISIBLE);
            verticalSeekBar.setVisibility(View.GONE);
            seekBar.setProgress(recordProgress);
        }

    }

    /**
     * 改变状态  即根据重力感应获取的当前旋转状态，以便计算识别区域
     */
    public void changeState(boolean isPortrait) {
        if (myView != null) {
            ((ViewGroup) myView.getParent()).removeView(myView);
            myView = new ViewfinderView(this, srcPoint, isPortrait);
            addContentView(myView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUESTCODE:
                boolean permissionsPASS = true;
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        //有任何一项权限没有通过，都会视为拒绝
                        permissionsPASS = false;
                    }
                }
                if (grantResults.length > 0 && permissionsPASS) {
                    //用户点击了同意授权
                    cameraManager = new CameraManager(this);
                    initView();
                    screenRotation = new ScreenRotation(this, mHandler);
                } else {
                    //用户拒绝了授权
                    System.out.println("拒绝了权限");
                    Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
                    this.finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //在桌面返回或者锁屏开启后，mSurfaceView会重复加载，所以要先移除该控件
        if(mSurfaceView != null){
            ((ViewGroup) mSurfaceView.getParent()).removeView(mSurfaceView);
        }
    }
}
