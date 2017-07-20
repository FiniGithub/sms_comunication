package com.dzd.phonebook.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

/**
 * 根据短信条数 计算应付金额
 *
 * @author CHENCHAO
 * @date 2017-04-26 14:18:00
 */
public class CalculateMoneyUtil {

    /**
     * 计算短信价格段的价格
     *
     * @param smsNum
     * @param smsAisleGroup
     * @return
     */
    public static Float calculateSmsNumMoney(Integer smsNum, SmsAisleGroup smsAisleGroup) {
        Float money = 0f;
        try {
            Float oneIntervalPrice = smsAisleGroup.getOneIntervalPrice(); // 第一区间价格
            Float twoIntervalPrice = smsAisleGroup.getTwoIntervalPrice(); // 第二区间价格
            Float threeIntervalPrice = smsAisleGroup.getThreeIntervalPrice(); // 第三区间价格

            Integer oneIntervalStart = smsAisleGroup.getOneIntervalStart(); // 第一区间开始数量
            Integer oneIntervalEnd = smsAisleGroup.getOneIntervalEnd(); // 第一区间结束数量

            Integer twoIntervalStart = smsAisleGroup.getTwoIntervalStart(); // 第二区间开始数量
            Integer twoIntervalEnd = smsAisleGroup.getTwoIntervalEnd(); // 第二区间结束数量

            Integer threeIntervalStart = smsAisleGroup.getThreeIntervalStart(); // 第三区间开始数量
            Integer threeIntervalEnd = smsAisleGroup.getThreeIntervalEnd(); // 第三区间结束数量

            if (smsNum < oneIntervalStart) {// 小于第一区间,按第一区间价格算
                money = smsNum * oneIntervalPrice;
            } else if (smsNum >= oneIntervalStart && smsNum < oneIntervalEnd) {// 第一区间价格段
                money = smsNum * oneIntervalPrice;
            } else if (smsNum >= twoIntervalStart && smsNum < twoIntervalEnd) {// 第二区间价格段
                money = smsNum * twoIntervalPrice;
            } else if (smsNum >= threeIntervalStart && smsNum <= threeIntervalEnd) {// 第三区间价格段
                money = smsNum * threeIntervalPrice;
            } else if (smsNum > threeIntervalEnd) {// 大于第三区间，按第三区间价格来算
                money = smsNum * threeIntervalPrice;
            }
            if (money <= 0) {
                money = 0f;
            }
        } catch (Exception e) {
            e.printStackTrace();
            money = 0f;
        }
        return money;
    }


    /**
     * 保留三位小数
     *
     * @param money
     * @return
     */
    private static double moneyRoundMode(Double money) {
        DecimalFormat formater = new DecimalFormat();
        // 保留3位小数
        formater.setMaximumFractionDigits(3);
        // 模式 四舍五入2,400
        formater.setRoundingMode(RoundingMode.UP);
        String moneyStr = formater.format(money);
        moneyStr = moneyStr.replace(",", "");
        return Double.parseDouble(moneyStr);
    }
}
