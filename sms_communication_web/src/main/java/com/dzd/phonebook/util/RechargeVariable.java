package com.dzd.phonebook.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

/**
 * 订单辅助类
 * Created by CHENCHAO on 2017/5/22.
 */
public class RechargeVariable {
    public static JSONArray CARD_TYPE = null;

    public static Integer RECHARGE_ORDER_STATUS_UNPOST = 0;// 未支付
    public static Integer RECHARGE_ORDER_STATUS_SUCCESS = 1;// 支付完成
    public static String RECHARGE_ORDER_SYS_MSG = "系统异常,请稍后再试!";


    /**
     * 初始化充值银行
     */
    public static JSONArray initCardType() {
        CARD_TYPE = new JSONArray();
        JSONObject psbc = new JSONObject();
        psbc.put("code", "PSBC");
        psbc.put("name", "中国邮政储蓄银行");
        psbc.put("icon", "PSBC_OUT.gif");

        JSONObject cmb = new JSONObject();
        cmb.put("code", "CMB");
        cmb.put("name", "招商银行");
        cmb.put("icon", "CMB_OUT.gif");

        JSONObject ccb = new JSONObject();
        ccb.put("code", "CCB");
        ccb.put("name", "建设银行");
        ccb.put("icon", "CCB_OUT.gif");

        JSONObject icbc = new JSONObject();
        icbc.put("code", "ICBC");
        icbc.put("name", "工商银行");
        icbc.put("icon", "ICBC_OUT.gif");

        JSONObject pab = new JSONObject();
        pab.put("code", "PAB");
        pab.put("name", "平安银行");
        pab.put("icon", "SPABANK_OUT.gif");

        JSONObject cmbc = new JSONObject();
        cmbc.put("code", "CMBC");
        cmbc.put("name", "民生银行");
        cmbc.put("icon", "CMBC_OUT.gif");

        JSONObject cib = new JSONObject();
        cib.put("code", "CIB");
        cib.put("name", "兴业银行");
        cib.put("icon", "CIB_OUT.gif");

        JSONObject sdb = new JSONObject();
        sdb.put("code", "SDB");
        sdb.put("name", "深圳发展");
        sdb.put("icon", "SDB_OUT.gif");

        JSONObject abc = new JSONObject();
        abc.put("code", "ABC");
        abc.put("name", "农业银行");
        abc.put("icon", "ABC_OUT.gif");

        JSONObject gdb = new JSONObject();
        gdb.put("code", "GDB");
        gdb.put("name", "广东发展");
        gdb.put("icon", "GDB_OUT.gif");

        JSONObject bob = new JSONObject();
        bob.put("code", "BOB");
        bob.put("name", "北京银行");
        bob.put("icon", "BANKOFBJ_OUT.gif");

        JSONObject post = new JSONObject();
        post.put("code", "POST");
        post.put("name", "中国邮政");
        post.put("icon", "POST_OUT.gif");

        JSONObject hxb = new JSONObject();
        hxb.put("code", "HXB");
        hxb.put("name", "华夏银行");
        hxb.put("icon", "HSBANK_OUT.gif");

        JSONObject bcom = new JSONObject();
        bcom.put("code", "BCOM");
        bcom.put("name", "交通银行");
        bcom.put("icon", "COMM_OUT.gif");

        JSONObject spdb = new JSONObject();
        spdb.put("code", "SPDB");
        spdb.put("name", "浦发银行");
        spdb.put("icon", "SPDB_OUT.gif");

        JSONObject ceb = new JSONObject();
        ceb.put("code", "CEB");
        ceb.put("name", "光大银行");
        ceb.put("icon", "CEB_OUT.gif");

        JSONObject cbhb = new JSONObject();
        cbhb.put("code", "CBHB");
        cbhb.put("name", "渤海银行");
        cbhb.put("icon", "BHBANK_OUT.gif");

        JSONObject citic = new JSONObject();
        citic.put("code", "CITIC");
        citic.put("name", "中信银行");
        citic.put("icon", "CITIC_OUT.gif");

        JSONObject boc = new JSONObject();
        boc.put("code", "BOC");
        boc.put("name", "中国银行");
        boc.put("icon", "BOC_OUT.gif");

        JSONObject srcb = new JSONObject();
        srcb.put("code", "SRCB");
        srcb.put("name", "上海农村商业银行");
        srcb.put("icon", "SRCB_OUT.gif");

        JSONObject bjrcb = new JSONObject();
        bjrcb.put("code", "BJRCB");
        bjrcb.put("name", "北京农商银行");
        bjrcb.put("icon", "BJRCB_OUT.gif");

        JSONObject njcb = new JSONObject();
        njcb.put("code", "NJCB");
        njcb.put("name", "南京银行");
        njcb.put("icon", "NJCB_OUT.gif");

        JSONObject nbcb = new JSONObject();
        nbcb.put("code", "NBCB");
        nbcb.put("name", "宁波银行");
        nbcb.put("icon", "NBCB_OUT.gif");

        JSONObject hzb = new JSONObject();
        hzb.put("code", "HZB");
        hzb.put("name", "杭州银行");
        hzb.put("icon", "HZB_OUT.gif");

        JSONObject hsb = new JSONObject();
        hsb.put("code", "HSB");
        hsb.put("name", "徽商银行");
        hsb.put("icon", "HSB_OUT.gif");

        JSONObject czb = new JSONObject();
        czb.put("code", "CZB");
        czb.put("name", "浙商银行");
        czb.put("icon", "CZB_OUT.gif");

        JSONObject shb = new JSONObject();
        shb.put("code", "SHB");
        shb.put("name", "上海银行");
        shb.put("icon", "SHB_OUT.gif");

//		JSONObject gzrcc = new JSONObject();
//		gzrcc.put("code", "GZRCC");
//		gzrcc.put("name", "广州农村商业银行");
//		gzrcc.put("icon", "GZRCC_OUT.gif");
//
//		JSONObject gzcb = new JSONObject();
//		gzcb.put("code", "GZCB");
//		gzcb.put("name", "广州银行");
//		gzcb.put("icon", "GZCB_OUT.gif");

//		JSONObject bea = new JSONObject();
//		bea.put("code", "BEA");
//		bea.put("name", "东亚银行");
//		bea.put("icon", "BEA_OUT.gif");


        CARD_TYPE.add(icbc);                              //工商银行
        CARD_TYPE.add(abc);                               //农业银行
        CARD_TYPE.add(boc);                               //中国银行
        CARD_TYPE.add(ccb);                               //建设银行
        CARD_TYPE.add(bcom);                              //交通银行
        CARD_TYPE.add(spdb);                              //浦发银行
        CARD_TYPE.add(psbc);                              //中国邮政储蓄银行
        CARD_TYPE.add(cmb);                               //招商银行
        CARD_TYPE.add(cmbc);                              //民生银行
        CARD_TYPE.add(cib);                               //兴业银行
        CARD_TYPE.add(ceb);                               //光大银行
        CARD_TYPE.add(pab);                               //平安银行
        CARD_TYPE.add(sdb);                               //深圳发展
        CARD_TYPE.add(post);                              //中国邮政
        CARD_TYPE.add(gdb);                               //广东发展
        CARD_TYPE.add(citic);                             //中信银行
        CARD_TYPE.add(hxb);                               //华夏银行
        CARD_TYPE.add(cbhb);                              //渤海银行
        CARD_TYPE.add(nbcb);                              //宁波银行
        CARD_TYPE.add(bob);                               //北京银行
        CARD_TYPE.add(bjrcb);                             //北京农商银行
        CARD_TYPE.add(shb);                               //上海银行
        CARD_TYPE.add(srcb);                              //上海农村商业银行
        CARD_TYPE.add(njcb);                              //南京银行
        CARD_TYPE.add(hzb);                               //杭州银行
        CARD_TYPE.add(czb);                               //浙商银行
        CARD_TYPE.add(hsb);                               //徽商银行

        return CARD_TYPE;
        //CARD_TYPE.add(gzrcc);                           //广州农村商业银行//
        //CARD_TYPE.add(gzcb);                            //广州银行
        //CARD_TYPE.add(bea);                             //东亚银行
    }

    /**
     * 生成订单号
     *
     * @return
     */
    public static String getOrderNumber(Integer uid) {
        String orderId = "qxdzd" + uid + System.currentTimeMillis();
        return orderId;
    }


}
