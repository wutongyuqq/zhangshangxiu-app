/*
 * @(#)IWaitParent.java
 * 
 * Copyright(c)2001-2015 SANY Heavy Industry Co.,Ltd
 * All right reserved.
 * 
 * 这个软件是属于三一重工股份有限公司机密的和私有信息，不得泄露。
 * 并且只能由三一重工股份有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information 
 * of SANY Heavy Industry Co, Ltd. You shall not disclose such 
 * Confidential Information and shall use it only in accordance 
 * with the terms of the license agreement you entered into with 
 * SANY Heavy Industry Co, Ltd.
 */
package com.shoujia.zhangshangxiu.web.util;

/**
 * IWaitParent.java
 * 
 * 等待进度条接口.
 * 
 * @author xudq
 * @company SANY Heavy Industry Co, Ltd
 * @creation date 2015-7-27
 * @version $Revision: 3 $
 */
public interface IWaitParent {
    // 返回取消进度条对话框
    void waitDialogCanced();

    // 结果当前activity
    void activityFinish();
}
