package com.shoujia.zhangshangxiu.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.kernal.plateid.CoreSetup;
import com.kernal.plateid.activity.PlateidCameraActivity;
import com.shoujia.zhangshangxiu.R;
import com.shoujia.zhangshangxiu.home.adapter.HomeCarInfoAdapter;
import com.shoujia.zhangshangxiu.base.BaseFragment;
import com.shoujia.zhangshangxiu.db.DBManager;
import com.shoujia.zhangshangxiu.dialog.ChooseProvinceDialog;
import com.shoujia.zhangshangxiu.dialog.CommonTipDialog;
import com.shoujia.zhangshangxiu.entity.CarInfo;
import com.shoujia.zhangshangxiu.entity.FirstIconInfo;
import com.shoujia.zhangshangxiu.entity.ReciveInfo;
import com.shoujia.zhangshangxiu.entity.RepairInfo;
import com.shoujia.zhangshangxiu.entity.SecondIconInfo;
import com.shoujia.zhangshangxiu.home.help.HomeDataHelper;
import com.shoujia.zhangshangxiu.http.IGetDataListener;
import com.shoujia.zhangshangxiu.order.ProjectOrderActivity;
import com.shoujia.zhangshangxiu.project.ProjectActivity;
import com.shoujia.zhangshangxiu.search.SearchListActivity;
import com.shoujia.zhangshangxiu.util.Constance;
import com.shoujia.zhangshangxiu.util.SharePreferenceManager;
import com.shoujia.zhangshangxiu.util.Util;
import com.shoujia.zhangshangxiu.view.CustomDatePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/23 0023.
 *
 */
public class HomeZsxFragment extends BaseFragment implements View.OnClickListener{

    private View mView;
    SharePreferenceManager sp;
    private TextView tv_cp_area,tv_base_info,tv_more_info,jieche_btn,yjcsj,tv_guanzhu;
    private LinearLayout base_info,more_info;
    private EditText et_province_cp,chejiahao,chexing,gonglishu,songxiuren,shoujihao,chezhu,tuijianren,guzhangmiaoshu,yaoshipaihao,beizhu;
    private ImageView take_photo_car,search_car;
    private EditText search_name;
    ListView mListView;
    PopupWindow mPopupWindow;
    boolean isOtherPage;
    CommonTipDialog mTipDialog;
    private HomeCarInfoAdapter homeCarInfoAdapter;

    private List<CarInfo> carInfoList;
    private List<CarInfo> showCarInfoList;
    HomeDataHelper mHomeDataHelper;
    private boolean isCpClick;
    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = View.inflate(getActivity(), R.layout.fragment_home_zsx, null);
        sp = new SharePreferenceManager(getContext());
        carInfoList = new ArrayList<>();
        initView();
        initData();
        initPopWindow();
        return mView;
    }

    private void initView() {
        tv_cp_area = (TextView) findViewById(R.id.tv_cp_area);
        yjcsj = (TextView) findViewById(R.id.yjcsj);
        base_info = (LinearLayout) findViewById(R.id.base_info);
        more_info = (LinearLayout) findViewById(R.id.more_info);
        tv_cp_area = (TextView) findViewById(R.id.tv_cp_area);
        tv_base_info = (TextView) findViewById(R.id.tv_base_info);
        tv_more_info = (TextView) findViewById(R.id.tv_more_info);
        jieche_btn = (TextView) findViewById(R.id.jieche_btn);
        tv_guanzhu = (TextView) findViewById(R.id.tv_guanzhu);
        et_province_cp = (EditText) findViewById(R.id.et_province_cp);
        chejiahao = (EditText) findViewById(R.id.chejiahao);
        chexing = (EditText) findViewById(R.id.chexing);
        gonglishu = (EditText) findViewById(R.id.gonglishu);
        songxiuren = (EditText) findViewById(R.id.songxiuren);
        shoujihao = (EditText) findViewById(R.id.shoujihao);
        chezhu = (EditText) findViewById(R.id.chezhu);
        tuijianren = (EditText) findViewById(R.id.tuijianren);
        guzhangmiaoshu = (EditText) findViewById(R.id.guzhangmiaoshu);
        yaoshipaihao = (EditText) findViewById(R.id.yaoshipaihao);
        beizhu = (EditText) findViewById(R.id.beizhu);
        search_name = (EditText) findViewById(R.id.search_name);
        take_photo_car = (ImageView) findViewById(R.id.take_photo_car);
        search_car = (ImageView) findViewById(R.id.search_car);
        tv_cp_area.setOnClickListener(this);
        tv_base_info.setOnClickListener(this);
        tv_more_info.setOnClickListener(this);
        take_photo_car.setOnClickListener(this);
        search_car.setOnClickListener(this);
        jieche_btn.setOnClickListener(this);
        tv_guanzhu.setOnClickListener(this);
        et_province_cp.addTextChangedListener(new MyTextWatcher());
       /* et_province_cp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    if(mPopupWindow!=null && et_province_cp.getText()!=null&&!TextUtils.isEmpty( et_province_cp.getText().toString().trim())){
                        et_province_cp.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    mPopupWindow.showAsDropDown(et_province_cp);
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        },150);

                    }
                }
            }
        });*/
        yjcsj.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        sp.putString(Constance.YUWANGONG,"");
        sp.putString(Constance.GONGLISHU,"");
        sp.putString(Constance.CURRENTCP,"");
        sp.putString(Constance.CUSTOMER_ID,"");
        sp.putString(Constance.CURRENTCZ,"");
        sp.putString(Constance.JIECHEDATE,"");
        sp.putString(Constance.BEIZHU,"");
        sp.putString(Constance.CHEJIAHAO,"");
    }

    private void getPersonRepairList(){

        getHomeHelper().getPersonRepairList(new HomeDataHelper.InsertDataListener() {
            @Override
            public void onSuccess() {
                getFirstIconList();
            }

            @Override
            public void onFail() {
                getFirstIconList();
            }
        });
    }

    private void getSecondInconList(){
        DBManager db = DBManager.getInstanse(getActivity());
        List<SecondIconInfo> secondIconInfos = db.querySecondIconListData();
        if(secondIconInfos==null||secondIconInfos.size()==0){

            getHomeHelper().getSecondIconHomeList(new IGetDataListener() {
                        @Override
                        public void onSuccess(String json) {
                            //dismissDialog();
                        }

                        @Override
                        public void onFail() {
                            //dismissDialog();
                        }
                    });

        }else{
            mHandler.sendEmptyMessage(5);
        }

    }

    private void getFirstIconList(){
        DBManager db = DBManager.getInstanse(getActivity());
        List<FirstIconInfo> firstIconInfos = db.queryFirstIconListData();
        if(firstIconInfos==null||firstIconInfos.size()==0){

            getHomeHelper().getFirstIconList(new HomeDataHelper.InsertDataListener() {
                   @Override
                   public void onSuccess() {
                       getSecondInconList();
                   }

                   @Override
                   public void onFail() {
                       getSecondInconList();
                   }
               });
        }else{
            mHandler.sendEmptyMessage(4);
        }

    }
    //初始化数据
    private void initData(){

                    Intent intent = new Intent(getActivity(), HomeService.class);
                    Bundle bundle = new Bundle();
                    intent.putExtras(bundle);
                    getActivity().startService(intent);

       // mHandler.sendEmptyMessageDelayed(101,10000);
    }


    private HomeDataHelper getHomeHelper(){
            if(mHomeDataHelper==null){
                mHomeDataHelper = new HomeDataHelper(getActivity());
            }
            return mHomeDataHelper;
     }

    @Override
    public void updateUIThread(Message msg) {
        int msgInt = msg.what;
         if(msgInt==14){
             dismissDialog();
            final ReciveInfo info = (ReciveInfo) msg.obj;
             mTipDialog = new CommonTipDialog(getContext(), "该车辆已进场", "进入该车", "返回接车");
             mTipDialog.show();
             mTipDialog.setOnClickListener(new CommonTipDialog.OnClickListener() {
                @Override
                public void leftBtnClick() {
                    mTipDialog.dismiss();
                    sp.putString(Constance.JSD_ID,info.getJsd_id());
                    sp.putString(Constance.CUSTOMER_ID,info.getCustomer_id());
                    getActivity().startActivity(new Intent(getActivity(),ProjectOrderActivity.class));
                }

                @Override
                public void rightBtnClick() {
                    mTipDialog.dismiss();
                }
            });
            if(info!=null){
                mTipDialog.setContent("接待人员："+info.getJcr(),"进厂时间："+info.getJc_date());
            }
        }else if(msgInt==15){
             isCpClick = false;
         }
    }

    private View findViewById(int id){
        return mView.findViewById(id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.tv_cp_area:
                ChooseProvinceDialog dialog = new ChooseProvinceDialog(getContext(), new ChooseProvinceDialog.ChooseListener() {
                    @Override
                    public void choose(String proStr) {
                        if(!TextUtils.isEmpty(proStr)){
                            tv_cp_area.setText(proStr);
                        }
                    }
                });
                dialog.show();
                break;
            case R.id.tv_base_info:
                tv_base_info.setTextColor(Color.parseColor("#ffffff"));
                tv_more_info.setTextColor(Color.parseColor("#333333"));
                tv_base_info.setBackgroundColor(Color.parseColor("#ff9db4"));
                tv_more_info.setBackgroundColor(Color.parseColor("#a4a3a3"));
                base_info.setVisibility(View.VISIBLE);
                more_info.setVisibility(View.GONE);
                break;
            case R.id.tv_more_info:
                tv_base_info.setTextColor(Color.parseColor("#333333"));
                tv_more_info.setTextColor(Color.parseColor("#ffffff"));
                tv_base_info.setBackgroundColor(Color.parseColor("#a4a3a3"));
                tv_more_info.setBackgroundColor(Color.parseColor("#ff9db4"));
                base_info.setVisibility(View.GONE);
                more_info.setVisibility(View.VISIBLE);
                break;
            case R.id.take_photo_car:
                isOtherPage = true;
                CoreSetup coreSetup = new CoreSetup();
                Intent cameraIntent = new Intent(getActivity(), PlateidCameraActivity.class);
                coreSetup.takePicMode = false;
                cameraIntent.putExtra("coreSetup", coreSetup);
                startActivityForResult(cameraIntent, 1);
                break;
            case R.id.jieche_btn:
                showDialog(getContext());
                startJieche();
                break;
            case R.id.tv_guanzhu:
                guanzhu();
                break;
            case R.id.yjcsj:
                showDialog();
                break;
            case R.id.search_car:
                String searchName = "";
                if(search_name.getText()!=null){
                    searchName = search_name.getText().toString().trim();
                }
                Intent searchIntent = new Intent(getActivity(),SearchListActivity.class);
                searchIntent.putExtra("searchName",searchName);
                startActivityForResult(searchIntent, 2);
                break;
            default:
                break;
        }

    }

    private void showDialog() {
        CustomDatePicker customDatePicker = new CustomDatePicker(getContext(), new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                Log.d("yyyyy", time);
                if(!TextUtils.isEmpty(time)&&time.length()>=10){
                    String pickTime = time.substring(0,10);
                    yjcsj.setText(pickTime);
                }
            }
        },"2007-01-01 00:00","2025-12-31 23:59");
        customDatePicker.show();
    }

    private void startJieche() {
        String mcSimple = et_province_cp.getText()==null?"":et_province_cp.getText().toString().trim().toUpperCase();
        if(TextUtils.isEmpty(mcSimple)){
            toastMsg="车牌必填";
            mHandler.sendEmptyMessage(TOAST_MSG);
            return;
        }

        String mc = tv_cp_area.getText().toString() + mcSimple;
        String cjhm = chejiahao.getText()==null?"":chejiahao.getText().toString().trim();
        String cx = chexing.getText()==null?"":chexing.getText().toString().trim();
        String gls = gonglishu.getText()==null?"":gonglishu.getText().toString().trim();
        String linkman = songxiuren.getText()==null?"":songxiuren.getText().toString().trim();
        String mobile = shoujihao.getText()==null?"":shoujihao.getText().toString().trim();
        String cz = chezhu.getText()==null?"":chezhu.getText().toString().trim();
        String custom5 = tuijianren.getText()==null?"":tuijianren.getText().toString().trim();
        String gzms = guzhangmiaoshu.getText()==null?"":guzhangmiaoshu.getText().toString().trim();
        String keys_no = yaoshipaihao.getText()==null?"":yaoshipaihao.getText().toString().trim();
        String memo = beizhu.getText()==null?"":beizhu.getText().toString().trim();
        String ns_date = yjcsj.getText()==null?"":yjcsj.getText().toString().trim();
        CarInfo bean = new CarInfo();
        bean.setMc(mc);
        bean.setId(0);
        bean.setCjhm(cjhm);
        bean.setCustom5(custom5);
        bean.setCx(cx);
        bean.setCz(cz);
        bean.setLinkman(linkman);
        bean.setMc(mc);
        bean.setMobile(mobile);
        bean.setNs_date(ns_date);
        bean.setGzms(gzms);
        bean.setGls(gls);
        bean.setMemo(memo);
        bean.setKeys_no(keys_no);

        sp.putString(Constance.GONGLISHU,gls);
        sp.putString(Constance.BEIZHU,memo);
        sp.putString(Constance.CHEJIAHAO,cjhm);
        sp.putString(Constance.CHEXING,cx);
        sp.putString(Constance.YUWANGONG,ns_date);
        sp.putString(Constance.GUZHNAGMIAOSHU,gzms);
        sp.putString(Constance.JIESHAOREN,linkman);

        if(mcSimple.length()<5){
            toastMsg="车牌号输入错误";
            mHandler.sendEmptyMessage(TOAST_MSG);
            return;
        }
        if(TextUtils.isEmpty(linkman)){
            toastMsg="报修人必填";
            mHandler.sendEmptyMessage(TOAST_MSG);
            return;
        }


        if(TextUtils.isEmpty(mobile)){
            toastMsg="手机号必填";
            mHandler.sendEmptyMessage(TOAST_MSG);
            return;
        }

        if(mobile.length()!=11){
            toastMsg="手机号输入有误";
            mHandler.sendEmptyMessage(TOAST_MSG);
            return;
        }
       /* DBManager db = DBManager.getInstanse(getContext());
        db.insertOrUpdateCarInfo(bean);*/
        DBManager db = DBManager.getInstanse(getContext());
        List<CarInfo> carInfos = db.queryListData(bean.getMc(),false);

        if(carInfos==null||carInfos.size()==0){
            //新车
            getHomeHelper().uploadNewCar(bean, new HomeDataHelper.GetDataListener() {
                @Override
                public void getData(int resType, String jsd_id, String jcr, String jc_date,String customer_id) {
                    if(resType==HomeDataHelper.GetDataListener.TYPE_UN_IN){
                        Message msg = new Message();
                        ReciveInfo info = new ReciveInfo();
                        info.setJc_date(jc_date);
                        info.setJcr(jcr);
                        info.setJsd_id(jsd_id);
                        info.setCustomer_id(customer_id);
                        msg.what=14;
                        msg.obj = info;
                        mHandler.sendMessage(msg);
                    }
                }
            });
        }else{
            CarInfo oldCarBean = carInfos.get(0);
            bean.setCustomer_id(oldCarBean.getCustomer_id());
            bean.setPhone(oldCarBean.getPhone());
            bean.setVipnumber(oldCarBean.getVipnumber());
            getHomeHelper().uploadOldCar(bean, new HomeDataHelper.GetDataListener() {
                @Override
                public void getData(int resType, String jsd_id, String jcr, String jc_date,String customer_id) {
                    if(resType==HomeDataHelper.GetDataListener.TYPE_UN_IN){
                        Message msg = new Message();
                        ReciveInfo info = new ReciveInfo();
                        info.setJc_date(jc_date);
                        info.setJcr(jcr);
                        info.setJsd_id(jsd_id);
                        info.setCustomer_id(customer_id);
                        msg.what=14;
                        msg.obj = info;
                        mHandler.sendMessage(msg);
                    }
                }
            });
        }

    }


    private void initPopWindow(){
        try {
        // 用于PopupWindow的View
        View contentView=LayoutInflater.from(getContext()).inflate(R.layout.popwindow_home, null, false);
         mListView = contentView.findViewById(R.id.listview);
            if(showCarInfoList==null){
                showCarInfoList = new ArrayList<>();
            }
        if(homeCarInfoAdapter==null) {
            homeCarInfoAdapter = new HomeCarInfoAdapter(getContext(), showCarInfoList);//新建并配置ArrayAapeter
        }
        mListView.setAdapter(homeCarInfoAdapter);
        if(carInfoList==null){
            carInfoList = new ArrayList<>();
        }
        showCarInfoList.clear();
        showCarInfoList.addAll(carInfoList);
        homeCarInfoAdapter.notifyDataSetChanged();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mPopupWindow.dismiss();
                isCpClick = true;
                CarInfo info = carInfoList.get(position);
                setFormData(info);
                mHandler.sendEmptyMessageDelayed(15,1000);
            }
        });
        // 创建PopupWindow对象，其中：
        // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
        // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
        mPopupWindow=new PopupWindow(contentView, Util.dp2px(getContext(),120), Util.dp2px(getContext(),200), false);
        // 设置PopupWindow的背景
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        mPopupWindow.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        mPopupWindow.setTouchable(true);
        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移

        // 或者也可以调用此方法显示PopupWindow，其中：
        // 第一个参数是PopupWindow的父View，第二个参数是PopupWindow相对父View的位置，
        // 第三和第四个参数分别是PopupWindow相对父View的x、y偏移
        // window.showAtLocation(parent, gravity, x, y);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(isOtherPage){
                isOtherPage=!isOtherPage;
                return;
            }
            try {
                if (mPopupWindow != null && et_province_cp.getText() != null) {
                    String proSimple = tv_cp_area.getText().toString().trim();
                    String proStr = proSimple + "%" + editable.toString();
                    DBManager db = DBManager.getInstanse(getActivity());
                    carInfoList = db.queryListData(proStr);
                    if(carInfoList==null||carInfoList.size()==0||isCpClick){
                        return;
                    }
                    showCarInfoList.clear();
                    showCarInfoList.addAll(carInfoList);
                    homeCarInfoAdapter.notifyDataSetChanged();
                    if (carInfoList != null && carInfoList.size() > 0 && !TextUtils.isEmpty(proSimple) && !TextUtils.isEmpty(editable.toString())) {
                        int[] location = new int[2];
                        et_province_cp.getLocationOnScreen(location); //获取在当前窗口内的绝对坐标,当前activity显示的大小
                        int yOff = location[1] + Util.dp2px(getContext(), 50);
                        mPopupWindow.dismiss();
                        if (getActivity() != null && et_province_cp!=null) {
                            mPopupWindow.showAsDropDown(et_province_cp);
                        }
                    }
                }
            }catch (Exception e){
                    e.printStackTrace();
            }
        }
    }

    private void guanzhu(){
        getHomeHelper().getGuzhuInfo();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                //获取到的识别结果
                String[] recogResult = data.getStringArrayExtra("RecogResult");
                //保存图片路径
                String savePicturePath = data.getStringExtra("savePicturePath");

                File imageFile = new File(savePicturePath);
                if(imageFile.exists()){
                    imageFile.delete();
                }
                if(recogResult!=null &&recogResult.length>0 && !TextUtils.isEmpty(recogResult[0])){
                    Toast.makeText(getContext(),"车牌为："+recogResult[0],Toast.LENGTH_LONG).show();
                    String cpStr = recogResult[0];
                    String cpArea = cpStr.substring(0,1);
                    tv_cp_area.setText(cpArea);
                    int length = cpStr.length();
                    String cpNum =  cpStr.substring(1,length);
                    et_province_cp.setText(cpNum);
                    DBManager db = DBManager.getInstanse(getActivity());
                    List<CarInfo> carInfos = db.queryListData(cpStr);
                    if(carInfos!=null&&carInfos.size()>0){
                        CarInfo info = carInfos.get(0);
                        setFormData(info);
                    }else{
                        CarInfo car = new CarInfo();
                        car.setMc(cpStr);
                        db.insertOrUpdateCarInfo(car);
                        setFormData(car);
                    }

                }
            }
        }else   if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            CarInfo info = (CarInfo) data.getSerializableExtra("carInfo");
            setFormData(info);
        }
    }

    private void setFormData(CarInfo info) {
        if(info==null){
            return;
        }
        String cpStr = info.getMc();
        if(cpStr.length()>1) {
            String cpArea = cpStr.substring(0, 1);
            tv_cp_area.setText(cpArea);
            int length = cpStr.length();
            String cpNum = cpStr.substring(1, length);
            et_province_cp.setText(cpNum);
        }

       /* tv_cp_area.setText(info.getMc());
        et_province_cp.setText(info.getMc());*/
        chejiahao.setText(info.getCjhm());
        chexing.setText(info.getCx());
        songxiuren.setText(info.getLinkman());
        shoujihao.setText(info.getMobile());
        chezhu.setText(info.getCz());
        tuijianren.setText(info.getCustom5());
        yjcsj.setText(info.getNs_date());
        guzhangmiaoshu.setText(info.getGzms());
        yaoshipaihao.setText(info.getKeys_no());
        beizhu.setText(info.getMemo());
        gonglishu.setText(info.getGls());

    }

    @Override
    public void onPause() {
        super.onPause();
        isOtherPage = true;
    }
}
