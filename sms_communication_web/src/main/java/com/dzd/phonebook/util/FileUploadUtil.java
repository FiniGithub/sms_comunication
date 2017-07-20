package com.dzd.phonebook.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.dzd.base.util.DateUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.dzd.phonebook.entity.SmsFileConfig;
import com.dzd.phonebook.page.MobileCheckUtil;
import com.dzd.sms.application.Define;
import com.dzd.sms.util.DistinguishOperator;

/**
 * 操作文件辅助类
 *
 * @author CHENCHAO
 * @date 2017-3-25 14:43:00
 */
public class FileUploadUtil {
    public static final Logger log = LoggerFactory.getLogger(FileUploadUtil.class);
    public static final String PHONE_PATH = "/fileUpload/";
    private static final String readLineText = "qwxtReadLine";

    /**
     * 获取电话分类的个数
     *
     * @param multipartFiles
     * @return
     */
    public static Map<String, Object> getSmsPhoneCategoryMap(MultipartFile[] multipartFiles,
                                                             HttpServletRequest request) {
        // 1.获取号码集合
        List<String> phoneList = new ArrayList<String>();
        String fileName = "";
        try {
            /** 创建临时文件 **/
            File tempFile = File.createTempFile("tempFile", null);
            multipartFiles[0].transferTo(tempFile);

            /** 获取文件的后缀 **/
            fileName = multipartFiles[0].getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf("."));

            // 判断文件格式为TXT
            if (suffix.equals(".txt")) {
                phoneList = readDataByTxt(tempFile);// 获取TXT中的号码

                // Excel .xlsx 调用此方法
            } else if (suffix.equals(".xlsx")) {
                phoneList = readDataByExcelXLSX(tempFile);// 获取Excel中的电话号码

                // Excel .xls 调用此方法
            } else if (suffix.equals(".xls")) {
                phoneList = readDataByExcelXLS(tempFile);// 获取Excel中的电话号码
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("-----------------》获取电话号码集合发生异常：" + e.getMessage());
        }


        // 2. 根据号码，对号码进行分类
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            // 分出重复号码、无效号码、有效号码
            Map<String, List<String>> smsMap = MobileCheckUtil.mobileAssort(phoneList);

            // 无效号码个数
            Integer invalidPhoneNum = smsMap.get(Define.PHONEKEY.INVALID).size();

            // 重复号码个数
            Integer repeatPhoneNum = smsMap.get(Define.PHONEKEY.DUPLICATE).size();

            // 有效号码个数
            Integer validPhoneNum = smsMap.get(Define.PHONEKEY.VALID).size();

            // 所有号码
            Integer allNumber = phoneList.size();

            // 有效号码集合
            // List<String> validList = smsMap.get(MobileCheckUtil.VALID);
            String phoneMsg = formatMobile(phoneList.get(0));
            if (validPhoneNum > 1) {
                phoneMsg += "....";
            }

            map.put("invalidPhoneNum", invalidPhoneNum);
            map.put("repeatPhoneNum", repeatPhoneNum);
            map.put("validPhoneNum", validPhoneNum);
            map.put("allNumber", allNumber);
            map.put("fileName", PHONE_PATH + fileName);
            map.put("phoneList", phoneMsg);
            // map.put("validList", validList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("号码进行分类发生错误：" + e.getMessage());
        }

        return map;
    }

    /**
     * 获取电话分类：排错、重复、无效号码map
     *
     * @param phoneList
     * @return
     */
    public static Map<String, Object> getPhoneMap(List<String> phoneList, String operators) {
        // 2. 根据号码，对号码进行分类
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            // 分出重复号码、无效号码、有效号码
            Map<String, List<String>> smsMap = MobileCheckUtil.mobileAssort(phoneList);
            if (smsMap == null) {
                smsMap = new HashMap<String, List<String>>();
            }

            // 无效号码个数
            Integer invalidPhoneNum = 0;
            if (smsMap.get(Define.PHONEKEY.INVALID) != null) {
                invalidPhoneNum = smsMap.get(Define.PHONEKEY.INVALID).size();
            }

            // 重复号码个数
            Integer repeatPhoneNum = 0;
            if (smsMap.get(Define.PHONEKEY.DUPLICATE) != null) {
                repeatPhoneNum = smsMap.get(Define.PHONEKEY.DUPLICATE).size();
            }


            // 所有号码
            Integer allNumber = phoneList.size();

            // 有效号码集合
            List<String> validList = getValidList(operators, smsMap);
            if (validList == null) {
                validList = new ArrayList<String>();
            }

            Integer validPhoneNum = validList.size();

            map.put("invalidPhoneNum", invalidPhoneNum);
            map.put("repeatPhoneNum", repeatPhoneNum);
            map.put("validPhoneNum", validPhoneNum);
            map.put("allNumber", allNumber);
            map.put("validList", validList);
            log.info("-----------------》分出的号码集合size：" + map.size());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("分出号码发生错误：" + e.getMessage());
        }
        return map;
    }

    private static List<String> getValidList(String operators, Map<String, List<String>> smsMap) {
        List<String> validList = new ArrayList<String>();
        ;
        if (StringUtils.isEmpty(operators)) {
            validList = smsMap.get(Define.PHONEKEY.VALID);
        } else {
            List<String> beforvalidList = smsMap.get(Define.PHONEKEY.VALID);

            List<String> mobiles = new ArrayList<String>();// 移动
            List<String> unicoms = new ArrayList<String>();// 联通
            List<String> telecoms = new ArrayList<String>();// 电信
            List<String> unknown = new ArrayList<String>();// 未知

            // 0：联通，1：移动，2：电信
            for (String phone : beforvalidList) {
                if (0 == DistinguishOperator.getSupplier(phone)) {
                    unicoms.add(phone);
                } else if (1 == DistinguishOperator.getSupplier(phone)) {
                    mobiles.add(phone);
                } else if (2 == DistinguishOperator.getSupplier(phone)) {
                    telecoms.add(phone);
                } else if (-1 == DistinguishOperator.getSupplier(phone)) {
                    unknown.add(phone);
                }
            }

            String[] split = operators.split(",");
            for (int i = 0; i < split.length; i++) {
                if ("0".equals(split[i])) {
                    validList.addAll(unicoms);
                } else if ("1".equals(split[i])) {
                    validList.addAll(mobiles);
                } else if ("2".equals(split[i])) {
                    validList.addAll(telecoms);
                }
            }

        }
        return validList;
    }

    /**
     * 根据TXT文本获取电话号码
     *
     * @param tempFile
     * @return
     */
    public static List<String> readDataByTxt(File tempFile) {
        log.info("-----------------》文件名称：" + tempFile.getName());
        List<String> phoneList = new ArrayList<String>();// 号码集合
        try {
            String code = resolveCode(tempFile);// 获取文本文件的编码格式
            InputStreamReader reader = new InputStreamReader(new FileInputStream(tempFile), code);
            BufferedReader br = new BufferedReader(reader);
            String s = br.readLine();
            while (s != null) {
                if (!s.equals("")) {// 本行数据不为空，进行添加
                    if (!readLineText.equals(s)) {
                        phoneList.add(s.trim());
                    }
                    s = br.readLine();
                } else {// 遇到空数据，重新赋值读取下一行
                    s = readLineText;
                }
            }
            br.close();
            // 判断UTF-8格式第一行会生成特殊符号并去除
            if (code.equals("UTF-8")) {
                String phone = phoneList.get(0);
                System.out.print(phone + ",length:" + phone.length());
                if (phone.length() > 11) {
                    phone = phone.substring(1, phone.length());
                    phoneList.remove(0);
                    phoneList.add(0, phone);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("-----------------》根据TXT获取号码发生异常：" + e.getMessage());
        }
        return phoneList;
    }

    /**
     * Excel后缀为.xlsx的版本调用此方法
     *
     * @param tempFile
     * @return
     */
    public static List<String> readDataByExcelXLSX(File tempFile) {
        log.info("-----------------》文件名称：" + tempFile.getName());
        List<String> phoneList = new ArrayList<String>();// 号码集合
        try {
            // 创建对Excel工作簿文件的引用
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(tempFile));
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rows = sheet.getPhysicalNumberOfRows();
            for (int i = 0; i < rows; i++) {
                XSSFRow row = sheet.getRow(i);
                if (row != null) {
                    int cells = row.getPhysicalNumberOfCells();

                    for (int j = 0; j < cells; j++) {
                        String value = "";
                        XSSFCell cell = row.getCell(j);
                        if (cell != null) {
                            switch (cell.getCellType()) {
                                case HSSFCell.CELL_TYPE_NUMERIC:
                                    value = new DecimalFormat("0").format(cell.getNumericCellValue());
                                    phoneList.add(value);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("-----------------》根据Excel 2007以上版本 .xlsx获取号码发生异常：" + e.getMessage());
        }
        return phoneList;
    }

    /**
     * 读取Excel后缀为.xls之下的版本调用此方法
     *
     * @param tempFile
     * @return
     */
    public static List<String> readDataByExcelXLS(File tempFile) {
        log.info("-----------------》文件名称：" + tempFile.getName());
        List<String> phoneList = new ArrayList<String>();// 号码集合
        try {
            // 创建对Excel工作簿文件的引用
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(tempFile));
            HSSFSheet sheet = workbook.getSheetAt(0);
            int rows = sheet.getPhysicalNumberOfRows();
            for (int i = 0; i < rows; i++) {
                HSSFRow row = sheet.getRow(i);
                if (row != null) {
                    int cells = row.getPhysicalNumberOfCells();

                    for (int j = 0; j < cells; j++) {
                        String value = "";
                        HSSFCell cell = row.getCell(j);
                        if (cell != null) {
                            switch (cell.getCellType()) {
                                case HSSFCell.CELL_TYPE_NUMERIC:
                                    value = new DecimalFormat("0").format(cell.getNumericCellValue());
                                    phoneList.add(value);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("-----------------》根据Excel 低版本 .xls获取号码发生异常：" + e.getMessage());
        }
        return phoneList;
    }

    /**
     * 保存文件
     *
     * @param multipartFiles
     */
    public static String saveFile(MultipartFile[] multipartFiles) {
        try {

            String fileName = multipartFiles[0].getOriginalFilename();// 原来的文件名称
            String uploadFileName = fileName.substring(0, fileName.lastIndexOf("."));// 去除后缀的文件名
            String suffix = fileName.substring(fileName.lastIndexOf("."));// 后缀 .txt
            fileName = uploadFileName + "dzd" + RandomUtil.getRandomTenThousand() + suffix;// 拼接随机数生成的文件名
            return saveFileInfo(multipartFiles[0].getInputStream(),
                    new byte[(int) multipartFiles[0].getSize()], fileName);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("-----------------》保存文件发生异常：" + e.getMessage());
        }
        return "";
    }

    public static String saveFileInfo(InputStream is, byte[] bytes, String fileName)
            throws IOException, FileNotFoundException {
        // 1. 保存文件
        int read = 0;
        int i = 0;
        while ((read = is.read()) != -1) {
            bytes[i] = (byte) read;
            i++;
        }
        is.close();

        // 2.存放位置
        String catalinaHome = System.getProperty("catalina.home");
        String path = catalinaHome + PHONE_PATH;
        String txtPath = path + "/" + fileName;

        // 3. 判断文件夹是否存在，不存在则创建
        createDir(path);
        // createDir(txtPath);

        OutputStream os = new FileOutputStream(new File(txtPath));
        os.write(bytes);
        os.flush();
        os.close();
        return PHONE_PATH + fileName;
    }

    /**
     * 根据不同的文件后缀，调用不同的方法
     *
     * @param fileName
     * @param file
     * @return
     */
    public static List<String> getPhoneListByFile(String fileName, File file) {
        /** 获取文件的后缀 **/
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 判断文件格式为TXT
        if (suffix.equals(".txt")) {
            return readDataByTxt(file);// 获取TXT中的号码

            // Excel2007之上的版本调用此方法
        } else if (suffix.equals(".xlsx")) {
            return readDataByExcelXLSX(file);// 获取Excel中的电话号码

            // 低版本的Excel调用此方法
        } else if (suffix.equals(".xls")) {
            return readDataByExcelXLS(file);// 获取Excel中的电话号码
        }
        return null;
    }

    /**
     * 写入文件
     */
    public static void writerFile(List<String> phoneList, String fileName) {
        try {
            if (fileName != null && !fileName.equals("") && phoneList != null && phoneList.size() > 0) {

                // 创建目录
                String catalinaHome = System.getProperty("catalina.home");
                String path = catalinaHome + PHONE_PATH;
                createDir(path);

                String txtPath = path + fileName;

                File file = new File(txtPath);
                // 1.文件流
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));

                // 2.号码追加到StringBuffer
                StringBuffer sb = new StringBuffer();
                for (String element : phoneList) {
                    sb.append(element + "\n");
                }
                bw.write(sb.toString());
                bw.newLine();
                bw.close();
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }

    }

    /**
     * 删除文件
     *
     * @return 文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(List<SmsFileConfig> configList, HttpServletRequest request) {
        boolean flag = false;
        try {
            // 删除多个服务器文件
            for (SmsFileConfig config : configList) {
                String catalinaHome = System.getProperty("catalina.home");
                String txtPath = catalinaHome + "/" + config.getFileName();
                File file = new File(txtPath);
                // 路径为文件且不为空则进行删除
                if (file.isFile() && file.exists()) {
                    file.delete();
                }
            }
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    /**
     * 删除单个文件
     *
     * @param filePath
     * @return
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {// 路径为文件且不为空则进行删除
            file.delete();// 文件删除
        }
    }

    /**
     * 创建目录
     *
     * @param destDirName
     * @return
     */
    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {// 判断目录是否存在
            return true;
        }
        if (!destDirName.endsWith(File.separator)) {// 结尾是否以"/"结束
            destDirName = destDirName + File.separator;
        }
        if (dir.mkdirs()) {// 创建目标目录
            return true;
        } else {
            return false;
        }
    }

    /**
     * 下载(导出)号码
     *
     * @param projects
     * @return
     */
    public static List<Map<String, Object>> createExcelRecord(List<String> projects) {
        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sheetName", "sheet1");
        listmap.add(map);
        for (int j = 0; j < projects.size(); j++) {
            Map<String, Object> maps = new HashMap<String, Object>();
            maps.put("id", projects.get(j));
            listmap.add(maps);
        }
        return listmap;
    }

    /**
     * 消费记录(导出)
     *
     * @param projects
     * @return
     */
    public static List<Map<String, Object>> createExcelConsumed(List<SmsUserMoneyRunning> projects) {
        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sheetName", "sheet1");
        listmap.add(map);
        for (int j = 0; j < projects.size(); j++) {
            String type = "";
            Integer typeId = projects.get(j).getType();
            if (typeId == 0) {
                type = "人工充值";
            } else if (typeId == 2) {
                type = "失败返还";
            } else if (typeId == 3) {
                type = "快钱充值";
            } else if (typeId == 4) {
                type = "赠送";
            } else if (typeId == 5) {
                type = "转出";
            } else if (typeId == 6) {
                type = "转入";
            } else if (typeId == 7) {
                type = "退费";
            }
            Map<String, Object> maps = new HashMap<String, Object>();
            maps.put("uaccount", projects.get(j).getUaccount());
            maps.put("email", projects.get(j).getEmail());
            maps.put("createTime", DateUtil.getDateLongCn(projects.get(j).getCreateTime()));
            maps.put("money", projects.get(j).getMoney());
            maps.put("operateNum", projects.get(j).getOperateNum());
            maps.put("beforeNum", projects.get(j).getBeforeNum());
            maps.put("afterNum", projects.get(j).getAfterNum());
            maps.put("type", type);
            maps.put("bname", projects.get(j).getBname());
            listmap.add(maps);
        }
        return listmap;
    }


    /**
     * 获取txt文件编码格式
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static String resolveCode(File file) throws Exception {
        InputStream inputStream = new FileInputStream(file);
        byte[] head = new byte[3];
        inputStream.read(head);
        String code = "gb2312";  //或GBK
        if (head[0] == -1 && head[1] == -2)
            code = "UTF-16";
        else if (head[0] == -2 && head[1] == -1)
            code = "Unicode";
        else if (head[0] == -17 && head[1] == -69 && head[2] == -65)
            code = "UTF-8";

        inputStream.close();

        System.out.println(code);
        return code;
    }


    public static String formatMobile(String mobile) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(mobile);
        mobile = m.replaceAll("").trim();
        return mobile;
    }
}
