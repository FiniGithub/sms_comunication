package com.dzd.sms.service.data;

/**
 * Created by Administrator on 2017-1-11.
 */
public class SmsState {
    public int code;
    public String message;
    public SmsState(int code, String message ){
        this.code = code;
        this.message = message;
    }
    public static SmsState define(int code, String message){
        return new SmsState(code, message );
    }
}
