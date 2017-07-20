package test;



import com.dzd.sms.application.Define;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/6/7.
 */
public class TestQW {

//    @Test
    public void test3() throws Exception {
        int max = 9999;
        int min = 1000;

        for(int i = 0;i<10000;i++){
            Random random = new Random();
            int s = random.nextInt(max) % (max - min + 1) + min;
            System.out.println(s);
        }


    }


    //    @Test
    public void test() throws Exception {
        /*String content = "发！！！！票钓鱼岛是中国的! ";
        List<SmsShieldWord> wordList = new ArrayList<SmsShieldWord>();

        SmsShieldWord word1 = new SmsShieldWord();
        word1.setLevel(1);
        word1.setWordName("发票");
        SmsShieldWord word2 = new SmsShieldWord();
        word2.setLevel(2);
        word2.setWordName("钓鱼岛");
        wordList.add(word1);
        wordList.add(word2);

        String regEx = Define.USERSENDSMS.SMS_SEND_REGEX;// 过滤中英文、数字的正则

        for (SmsShieldWord word : wordList) {
            String wordName = word.getWordName();
            int level = word.getLevel();// 等级

            String regContent = content;
            if (level == 1) {// 等级为1级,过滤所有符号
                regContent = regContent.replaceAll(regEx, "").trim();
                // 判断是否包含屏蔽词
                if (regContent.indexOf(wordName) != -1) {
                    System.out.println("包含屏蔽词:" + wordName);
                    break;
                }

            } else if (level == 2) { // 屏蔽词等级为2等级
                if (regContent.indexOf(wordName) != -1) {
                    System.out.println("包含屏蔽词:" + wordName);
                    break;
                }
            }


        }*/
    }


    //@Test
 /*   public void test2() throws Exception {
        VertifyCode vertifyCode = new VertifyCode();
        vertifyCode.setPhone("18617052312");
        vertifyCode.setVertifycode("8645");
        vertifyCode.setContent(SmsContentBean.getSendVerifyCodeSmsContent(vertifyCode));// 内容
        SendSmsUtil.sendSMS(vertifyCode);
    }*/


    public static String replaceSpecStr(String orgStr) {
        if (null != orgStr && !"".equals(orgStr.trim())) {
            String regEx = Define.USERSENDSMS.SMS_SEND_REGEX;// 过滤中英文、数字的正则
            orgStr = orgStr.replaceAll(regEx, "\\$");
            return orgStr.trim();
        }
        return orgStr;
    }

}
