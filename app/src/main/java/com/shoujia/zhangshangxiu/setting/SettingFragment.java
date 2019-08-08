package com.shoujia.zhangshangxiu.setting;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.base.BaseFragment;
import com.shoujia.zhangshangxiu.db.DBManager;
import com.shoujia.zhangshangxiu.dialog.LoginOutDialog;
import com.shoujia.zhangshangxiu.dialog.WaitProgressDialog;
import com.shoujia.zhangshangxiu.home.HomeService;
import com.shoujia.zhangshangxiu.home.help.HomeDataHelper;
import com.shoujia.zhangshangxiu.login.PhoneLoginActivity;
import com.shoujia.zhangshangxiu.web.MainActivity;
import com.shoujia.zhangshangxiu.web.NetTool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/23 0023.
 *
 */
public class SettingFragment extends BaseFragment implements View.OnClickListener{

    private View mView;
    private LinearLayout update_data,logout,check_update;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = View.inflate(getActivity(), R.layout.fragment_setting, null);
        initView();

        return mView;
    }

    private void initView() {
        update_data = mView.findViewById(R.id.update_data);
        logout = mView.findViewById(R.id.logout);
        check_update = mView.findViewById(R.id.check_update);
        update_data.setOnClickListener(this);
        logout.setOnClickListener(this);
        check_update.setOnClickListener(this);
    }

    public void updateUIThread(Message msg) {
        int msgInt = msg.what;
        switch (msgInt){
            case 4:
                down();
                break;
            case 5:
                pBar.setProgress(progressInt);
                break;
            case 6:
                Toast.makeText(getContext(), "文件下载失败", Toast.LENGTH_LONG).show();
                break;
            case 11:
                showUpdateDialog(update_log, download_url);
                break;
                default:
                    break;
        }

    }


    private View findViewById(int id){
        return mView.findViewById(id);
    }

    @Override
    public void onClick(View v) {
         HomeDataHelper helper = new HomeDataHelper(getActivity());
        switch (v.getId()){
            case R.id.update_data:
                showDialog(getContext());
                helper.getFirstIconList(new HomeDataHelper.UpdateDataListener() {
                    @Override
                    public void onSuccess() {
                        dismissDialog();
                       // DBManager.getInstanse(getActivity()).close();
                        toastMsg = "更新项目数据成功";
                        mHandler.sendEmptyMessage(TOAST_MSG);
                    }
                });
                helper.getPersonRepairList(new HomeDataHelper.UpdateDataListener() {
                    @Override
                    public void onSuccess() {
                        dismissDialog();
                       // DBManager.getInstanse(getActivity()).close();
                        toastMsg = "更新修理数据成功";
                        mHandler.sendEmptyMessage(TOAST_MSG);
                    }
                });
                helper.getPartsList(new HomeDataHelper.UpdateDataListener() {
                    @Override
                    public void onSuccess() {
                        dismissDialog();
                       // DBManager.getInstanse(getActivity()).close();
                        toastMsg = "更新配件数据成功";
                        mHandler.sendEmptyMessage(TOAST_MSG);
                    }
                });
                helper.getSecondIconList(new HomeDataHelper.UpdateDataListener() {
                    @Override
                    public void onSuccess() {
                        dismissDialog();
                       // DBManager.getInstanse(getActivity()).close();
                        //toastMsg = "更新项目库数据成功";
                        //mHandler.sendEmptyMessage(TOAST_MSG);
                    }
                });

                Intent intent = new Intent(getActivity(), HomeService.class);
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                getActivity().startService(intent);


                break;
            case R.id.logout:
                LoginOutDialog dialog = new LoginOutDialog(getContext());
                dialog.setOnClickListener(new LoginOutDialog.OnClickListener() {
                    @Override
                    public void rightBtnClick() {
                        HomeDataHelper helper2 = new HomeDataHelper(getActivity());
                        helper2.loginOut(new HomeDataHelper.InsertDataListener() {
                            @Override
                            public void onSuccess() {
                                getActivity().startActivity(new Intent(  getActivity(),PhoneLoginActivity.class));
                                getActivity().finish();
                            }

                            @Override
                            public void onFail() {
                                getActivity().startActivity(new Intent(  getActivity(),PhoneLoginActivity.class));
                                getActivity().finish();
                            }
                        });
                    }
                });
                dialog.show();

                break;
            case R.id.check_update:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        checkVesion();
                    }
                }).start();

            break;

            default:
                break;
        }

    }
    private String update_log,download_url;

    public void checkVesion() {
        NetTool netTool = new NetTool();
        Map<String, Object> jsonMap = netTool.getVesionInfoFromServer();
        if (jsonMap != null) {
            String stateStr = jsonMap.get("state") != null ? (String) jsonMap
                    .get("state") : "";
            String updateStr = jsonMap.get("online_update") != null ? (String) jsonMap
                    .get("online_update") : "";
             download_url = jsonMap.get("download_url") != null ? (String) jsonMap
                    .get("download_url") : "";
            if (stateStr.equals("ok") && updateStr.equals("YES")
                    && !download_url.equals("")) {
                String versionServer = jsonMap.get("version") != null ? (String) jsonMap
                        .get("version") : "";
                if (isNeedUpdate(versionServer)) {
                     update_log = jsonMap.get("update_log") != null ? (String) jsonMap
                            .get("update_log") : "";

                    mHandler.sendEmptyMessage(11);

                } else {
                    toastMsg = "已是最新版本，无需更新";
                    mHandler.sendEmptyMessage(TOAST_MSG);
                }
            } else {
                if (stateStr.equals("ok")) {
                    toastMsg = jsonMap.get("msg") != null ? (String) jsonMap
                            .get("msg") : "";
                    mHandler.sendEmptyMessage(TOAST_MSG);
                } else {
                    toastMsg = "服务异常";
                    mHandler.sendEmptyMessage(TOAST_MSG);
                }

            }
        } else {
            toastMsg = "已是最新版本，无需更新";
            mHandler.sendEmptyMessage(TOAST_MSG);
        }
    }

    private boolean isNeedUpdate(String version) {
        String oldVersion = getVersion();
        if (oldVersion.contains(".") && version.contains(".")) {
            oldVersion = oldVersion.replaceAll("\\.", "");
            version = version.replaceAll("\\.", "");
            long oldVersionLong = Long.parseLong(oldVersion);
            long versionLong = Long.parseLong(version);
            return versionLong > oldVersionLong;
        } else {
            return false;
        }

    }

    // 获取当前版本的版本号
    private String getVersion() {
        try {
            PackageManager packageManager = getActivity().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    getActivity().getPackageName(), 0);

            Log.d("TAK", "packageInfo.versionName" + packageInfo.versionName);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";

        }
    }

    String apkPath = "";
    // 弹出升级框
    private void showUpdateDialog(String tipMsg,
                                  final String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        apkPath = url;
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("更新版本");
        builder.setMessage(tipMsg);
        builder.setCancelable(false);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    downFile(apkPath);
                } else {
                    Toast.makeText(getContext(), "SD卡不可用，请插入SD卡",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }

        });
        builder.create().show();
    }
    String saveDir = "";
    ProgressDialog pBar;
    private void downFile(final String url) {
        Log.d("TSK", "url" + apkPath);
        pBar = new ProgressDialog(getContext()); // 进度条，在下载的时候实时更新进度，提高用户友好度
        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pBar.setTitle("正在下载");
        pBar.setMessage("请稍候...");
        pBar.setProgress(0);
        pBar.show();
        final String videoUrl = apkPath;
        saveDir = Environment.getExternalStorageDirectory() + "/abc";
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                download(videoUrl);
            }
        }).start();

    }
    private String SDPath = "/mnt/sdcard/zsx/";
    int progressInt = 0;
    public void download(final String url) {
        File file = new File(SDPath + "zhangshangxiu.apk");
        if (file.exists()) {
            file.delete();
        }

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onResponse(Call arg0, Response response) {
                    long total = 0;
                    int current = 0;
                    boolean isUploading = false;
                    int len = 0;
                    InputStream is = null;
                    byte[] buf = new byte[2048];
                    FileOutputStream fos = null;

                    File fileDir = new File(saveDir);

                    try {
                        if (!fileDir.exists() || !fileDir.isDirectory()) {
                            fileDir.mkdirs();
                        }
                        is = response.body().byteStream();
                        total = response.body().contentLength();

                        File file = new File(saveDir, "zhangshangxiu.apk");
                        fos = new FileOutputStream(file);
                        long sum = 0;
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                            sum += len;
                            int progress = (int) (sum * 1.0f / total * 100);
                            Message msg = new Message();
                            // 下载中
                            progressInt = progress;
                            mHandler.sendEmptyMessage(5);

                        }
                        fos.flush();
                        Message msg = new Message();

                        fos.flush();
                        // 下载完成
                        mHandler.sendEmptyMessage(4);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mHandler.sendEmptyMessage(6);
                    }
                }

                @Override
                public void onFailure(Call arg0, IOException arg1) {
                    mHandler.sendEmptyMessage(6);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(6);
        }
    }

   private void down() {
        mHandler.post(new Runnable() {
            public void run() {
                pBar.cancel();
                update();
            }
        });
    }

   private void update() {
        String fileName = saveDir + "/zhangshangxiu.apk";
        File apkfile = new File(fileName);
        if (apkfile.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= 24) { // 适配安卓7.0
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri apkFileUri = FileProvider.getUriForFile(
                        getContext().getApplicationContext(),
                        getContext().getPackageName() + ".fileprovider", apkfile);
                intent.setDataAndType(apkFileUri,
                        "application/vnd.android.package-archive");
            } else {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(
                        Uri.parse("file://" + apkfile.toString()),
                        "application/vnd.android.package-archive");// File.toString()会返回路径信息
            }
            startActivity(intent);
        }

    }
}
