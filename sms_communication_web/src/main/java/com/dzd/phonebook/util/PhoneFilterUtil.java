package com.dzd.phonebook.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.dzd.phonebook.entity.SmsFileConfig;
import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.service.SmsFileConfigService;

import net.sf.json.JSONObject;

/**
 * 号码过滤
 *
 * @author CHENCHAO
 * @date 2017-05-17
 */
public class PhoneFilterUtil {
    private static final String PHONE_PATH = "/fileUpload/";

    /**
     * 过滤手动输入的号码,并且写入文件
     *
     * @param phoneStr
     * @param uuid
     * @return
     */
    @SuppressWarnings("unchecked")
    public static JSONObject headPhoneFilter(String phoneStr, String uuid, SysUser user,
                                             SmsFileConfigService<SmsFileConfig> smsFileConfigService) {
        JSONObject json = new JSONObject();
        Integer code = 0;
        String msg = "";
        String phone = "";
        Integer phoneSize = 0;
        String fileName = "";

        try {
            // 1.将号码按照\n进行分割
            String[] phoneStrList = phoneStr.split("\n");
            List<String> phoneList = new ArrayList<String>();
            Collections.addAll(phoneList, phoneStrList);

            // 2.过滤出有效的号码
            Map<String, Object> map = FileUploadUtil.getPhoneMap(phoneList, null);
            List<String> validPhoneList = (List<String>) map.get("validList");

            // 3.保存有效号码文件,并且存入数据库
            if (validPhoneList != null && validPhoneList.size() > 0) {
                fileName = "" + UUID.randomUUID() + "dzdqw" + RandomUtil.getRandomTenThousand() + ".txt";// 存入服务器的文件名
                FileUploadUtil.writerFile(validPhoneList, fileName);
                String phoneStrs = validPhoneList.get(0);
                if (validPhoneList.size() > 1) {
                    phoneStrs = validPhoneList.get(0) + "....";
                }
                fileName = PHONE_PATH + fileName;// 入库的文件名

                SmsFileConfig config = new SmsFileConfig();
                config.setSms_uid(user.getId());
                config.setUuid(uuid);
                config.setFileName(fileName);
                config.setType(0);
                config.setPhone(phoneStrs);
                config.setPhoneSize(validPhoneList.size());
                smsFileConfigService.add(config);

                code = 0;
                msg = "OK";
                if (validPhoneList.size() > 1) {// 号码
                    phone = validPhoneList.get(0) + "....";
                } else {
                    phone = validPhoneList.get(0);
                }
                phoneSize = validPhoneList.size();
            } else {
                code = -2;
                msg = "无有效号码";
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = "系统异常,请稍后再试!";
            phone = "";
            code = -1;
        }

        json.put("code", code);
        json.put("msg", msg);
        json.put("fileName", fileName);
        json.put("phone", phone);
        json.put("phoneSize", phoneSize);
        return json;
    }
}
