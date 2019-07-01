package com.kernal.plateid;

import android.os.Environment;

import java.io.Serializable;

/**
 * @author user
 *根据注释设置参数
 */
public class CoreSetup implements Serializable{
    /***
     * 授权方式
     */
    public String Devcode = "56AP5BEE5BIC6AA";//项目授权，开发码设置
    public String Sn = "";//序列号授权方式，序列号
    /**
     * 识别模式
     */
    public boolean takePicMode = false;//拍照识别:true,视频流自动识别:false
    public boolean accurateRecog = true;//精准识别:true,快速识别:false（推荐使用精准识别，识别准确率高。设备配置太低的推荐快速模式）
    /***
     * 保存图片
     */
    public String savePicturePath = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/Plateid.jpg";//设置为""则不保存。
    public boolean onlySaveOnePicture = true;//只保存一张图片，新的图片替换老的图片:true,保存每一张图片:false
    /**
     * 识别车牌参数设置
     */
    public int preWidth = 1920;//预览分辨率设置
    public int preHeight = 1080;

    public int nOCR_Th = 7;//识别阈值(取值范围0-9, 0:最宽松的阈值, 9:最严格的阈值, 5:默认阈值)
    public int nPlateLocate_Th = 5;// 定位阈值(取值范围0-9, 0:最宽松的阈值9, :最严格的阈值, 5:默认阈值)
    public String szProvince = "";// 省份顺序,例:public int szProvince = "京津沪";最好设置三个以内，最多五个。

    public int individual = 1;// 个性化车牌是否开启：0开启；1关闭
    public int tworowyellow = 2;// 双层黄色车牌是否开启：开启；3关闭
    public int armpolice = 4;// 单层武警车牌是否开启：4开启；5关闭
    public int tworowarmy = 6;// 双层军队车牌是否开启：6开启；7关闭
    public int tractor = 8;// 农用车车牌是否开启：8开启；9关闭
    public int embassy = 12;// 使馆车牌是否开启：12开启；13关闭
    public int armpolice2 = 16;// 双层武警车牌是否开启：16开启；17关闭
    public int consulate = 22;// 领事馆车牌开启：22开启；23关闭
    public int newEnergy = 24;// 新能源车牌开启：24开启；25关闭
}
