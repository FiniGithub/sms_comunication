package com.dzd.phonebook.util.send.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.dzd.phonebook.util.send.api.qwsend.ISendWs;
import com.dzd.phonebook.util.send.api.qwsend.SenderService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dzd.phonebook.entity.VertifyCode;

import javax.jws.WebParam;

/**
 * 发送短信
 *
 * @author CHENCHAO
 * @date 2017-04-11 13:49:00
 */
public class SendSmsUtil {
    public static final Logger log = LoggerFactory.getLogger(SendSmsUtil.class);
    public static final String SEND_USERNAME = "qwxtyzm";
    public static final String SEND_KEY = "733123272f83472f24261923";
    public static final String SEND_SIGN = "千讯数据";
    public static final String SEND_IP = "99";


    /**
     * ---------- 【千讯信通】发送验证码短信,暂时弃用
     *
     * @param code
     */
    public static void sendSmsVertifyCodes(VertifyCode code) {
        try {
            Date nowTime = new Date();
            System.out.println(nowTime);
            SimpleDateFormat time = new SimpleDateFormat("yyyyMMddHHmmss");
            System.out.println(time.format(nowTime));

            String url = "http://api.dzd.com/v4/sms/send.do";
            Integer userid = 54;
            String timestamp = time.format(nowTime);
            String key = "33ea2e419d4f58fd2c0deb1b962607e9";
            String signOld = userid + key + String.valueOf(timestamp);
            String sign = string2MD5(signOld);
            String content = "【全网信通】您的验证码是：" + code.getVertifycode() + "，30分钟内有效。";

            Map<String, String> params = new HashMap<String, String>();
            params.put("uid", String.valueOf(userid));
            params.put("timestamp", String.valueOf(timestamp));
            params.put("sign", sign);
            params.put("mobile", code.getPhone());
            params.put("text", content);
            params.put("iid", String.valueOf(360));
            String xml = post(url, params);

            log.debug("-----------------------------xml:" + xml);
            System.out.println(xml);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(null, e);
        } finally {

        }
    }


    /**
     * ---------- 【全网信通】发送短信
     *
     * @param code
     */
    public static void sendSMS(VertifyCode code) {
        try {
            Date nowTime = new Date();
            System.out.println(nowTime);
            SimpleDateFormat time = new SimpleDateFormat("yyyyMMddHHmmss");
            System.out.println(time.format(nowTime));

            String url = "http://api.dzd.com/v4/sms/send.do";
            Integer userid = 54;
            String timestamp = time.format(nowTime);
            String key = "33ea2e419d4f58fd2c0deb1b962607e9";
            String signOld = userid + key + String.valueOf(timestamp);
            String sign = string2MD5(signOld);
            String content = code.getContent();

            Map<String, String> params = new HashMap<String, String>();
            params.put("uid", String.valueOf(userid));
            params.put("timestamp", String.valueOf(timestamp));
            params.put("sign", sign);
            params.put("mobile", code.getPhone());
            params.put("text", content);
            params.put("iid", String.valueOf(360));
            String resultCode = post(url, params);






           /* String userName = SEND_USERNAME;// 用户名
            String password = SEND_KEY;// key
            String sign = SEND_SIGN;// 签名
            String ip = SEND_IP;// ip

            String phone = code.getPhone();// 电话
            String content = code.getContent(); // 内容


            ISendWs ws = (new SenderService()).getSenderPort();
            String resultCode = ws.send(userName, password, phone, content, sign, ip);*/
            log.debug("-----------------------------resultCode:" + resultCode);
            System.out.println(resultCode);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(null, e);
        }
    }


    public static String invoke(DefaultHttpClient httpclient, HttpUriRequest httpost) {

        HttpResponse response = sendRequest(httpclient, httpost);
        String body = paseResponse(response);

        return body;
    }

    public static String paseResponse(HttpResponse response) {

        HttpEntity entity = response.getEntity();

        // String charset = EntityUtils.getContentCharSet(entity);

        String body = null;
        try {
            body = EntityUtils.toString(entity);

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }

    public static HttpResponse sendRequest(DefaultHttpClient httpclient, HttpUriRequest httpost) {

        HttpResponse response = null;

        try {
            response = httpclient.execute(httpost);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static String post(String url, Map<String, String> params) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        String body = null;

        HttpPost post = postForm(url, params);
        body = invoke(httpclient, post);
        httpclient.getConnectionManager().shutdown();

        return body;
    }

    public static HttpPost postForm(String url, Map<String, String> params) {

        HttpPost httpost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            nvps.add(new BasicNameValuePair(key, params.get(key)));
        }

        try {
            // log.info("set utf-8 form entity to httppost");
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return httpost;
    }

    private static String string2MD5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    /**
     * 生成随机的四位数字验证码（范围0000~9999）
     */
    public static String createVertifyCode() {
        String s = "";
        int intCount = 0;

        intCount = (new Random()).nextInt(999999);

        s = intCount + "";

        while (s.length() < 6) {
            s = "0" + s;
        }
        return s;
    }

    /**
     * 生成随机的八位数字密码
     */
    public static String createNewPassword() {
        String newPassword = "";
        int intCount = 0;

        intCount = (new Random()).nextInt(99999999);

        newPassword = intCount + "";

        while (newPassword.length() < 8) {
            newPassword = "0" + newPassword;
        }
        return newPassword;
    }
}
