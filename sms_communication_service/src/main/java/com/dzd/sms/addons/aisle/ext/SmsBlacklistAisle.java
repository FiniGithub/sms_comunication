package com.dzd.sms.addons.aisle.ext;


import com.dzd.sms.addons.aisle.BaseAisle;
import com.dzd.sms.application.Define;
import com.dzd.sms.service.data.SmsSendResult;
import com.dzd.sms.service.data.SmsTaskData;
import com.dzd.utils.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import static com.dzd.sms.application.Define.STATE_SENDED_BLACKLIST_ERROR;

/**
 * 黑名单处理
 */

public class SmsBlacklistAisle extends BaseAisle {
    private  static Logger logger = Logger.getLogger(SmsBlacklistAisle.class );
    public SmsBlacklistAisle() {
        this.setAisleId( -3l );
        theadNumber = 5;                    //默认3个线程数量
        singlePackageNumber = 1000;          //单个包号码数量
        //isFastSendAisle = true;
        init();
    }


    public boolean sendSinglePackage(SmsTaskData smsTaskData, List<String> mobileList) {
//    	String smsId = smsTaskData.getTaskId().toString();
//        //保存到对应的库里
//        for( String p : mobileList ){
//            SmsSendResult smsSendResult = new SmsSendResult( StringUtil.string2MD5(smsId+p) );
//            smsSendResult.setTaskId(smsTaskData.getTaskId().toString());
//            smsSendResult.setMobile(p);
//            smsSendResult.setAisleId( aisleId );
//            smsSendResult.setState(STATE_SENDED_BLACKLIST_ERROR);
//            saveSendResult( smsSendResult );
//        }
//        return false;

        String smsId = smsTaskData.getTaskId().toString();

        List<SmsSendResult> taskDataList = new ArrayList<SmsSendResult>();

        //保存到对应的库里
        for( String p : mobileList ){
            SmsSendResult smsSendResult = new SmsSendResult( StringUtil.string2MD5(smsId+p) );
            smsSendResult.setTaskId(smsTaskData.getTaskId().toString());
            smsSendResult.setMobile(p);
            smsSendResult.setAisleId( aisleId );
            smsSendResult.setState( Define.STATE_SENDED_BLACKLIST_ERROR);
            taskDataList.add(smsSendResult);

        }
        saveSendResult( taskDataList );

        logger.info("SmsBlacklistAisle sendSinglePackage state="+Define.STATE_SENDED_BLACKLIST_ERROR+" size="+mobileList.size());
        return false;


    }

    public   boolean queryReply(){return false;};
    public   boolean queryReport(){return false;};
    public   Double queryBalance(){ return 0.0;};
    public  String pushReport( Map<String,String> params ){ return ""; };
    public  String pushReply( Map<String,String> params ){return ""; };


}
