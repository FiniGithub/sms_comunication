package com.dzd.phonebook.controller;

import com.dzd.phonebook.aop.MethodDescription;
import com.dzd.phonebook.entity.SmsHomePage;
import com.dzd.phonebook.entity.SysUser;
import com.dzd.phonebook.service.SmsHomePageService;
import com.dzd.phonebook.util.DzdResponse;
import com.dzd.phonebook.util.ErrorCodeTemplate;
import com.dzd.phonebook.util.FileUploadUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

/**
 * 首页编辑CONTROLLER
 * Created by CHENCHAO on 2017/5/31.
 */
@Controller
@RequestMapping("/smsHomePage")
public class HomePageController {
    public static final Logger log = LoggerFactory.getLogger(HomePageController.class);
    private static final String IMG_PATH = "/img/homePageImg/";
    private static final String DEFAULT_SUB_FOLDER_FORMAT_AUTO = "yyyyMMddHHmmss";

    @Autowired
    private SmsHomePageService smsHomePageService;

    @RequestMapping("/index")
    public String index() {
        return "/homePage/index";
    }

    @RequestMapping("/homePage")
    public String list() {
        return "/homePage/home_page";
    }


    /**
     * 查询首页样式
     *
     * @param request
     * @return
     */
    @RequestMapping("/homePageList")
    @ResponseBody
    public DzdResponse queryHomePageList(HttpServletRequest request) {
        DzdResponse dzdResponse = new DzdResponse();
        try {
            SmsHomePage smsHomePage = smsHomePageService.querySmsHome();
            dzdResponse.setData(smsHomePage);
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
        } catch (Exception e) {
            e.printStackTrace();
            dzdResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            dzdResponse.setRetMsg("系统异常!");
        }
        return dzdResponse;
    }


    /**
     * 新增、修改
     *
     * @param request
     * @param data
     * @return
     */
    @MethodDescription("首页编辑操作")
    @RequestMapping(value = "/from/merge", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public DzdResponse merge(HttpServletRequest request, @RequestBody Map<String, Object> data) {
        DzdResponse dzdPageResponse = new DzdResponse();
        try {
            Object content = data.get("content");
            SmsHomePage smsHomePage = smsHomePageService.querySmsHome();
            SmsHomePage homePage;
            if (smsHomePage == null) {// 新增
                homePage = new SmsHomePage();
                homePage.setContent(content.toString());
                smsHomePageService.add(homePage);
            } else {// 修改
                homePage = smsHomePage;
                homePage.setContent(content.toString());
                smsHomePageService.update(homePage);
            }
            dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_SUCESS);
            dzdPageResponse.setData(homePage);
        } catch (Exception e) {
            log.error(null, e);
            dzdPageResponse.setRetCode(ErrorCodeTemplate.CODE_FAIL);
            e.printStackTrace();
        }
        return dzdPageResponse;
    }


    /**
     * 上传图片
     *
     * @param file
     * @param request
     * @param response
     */
    @RequestMapping(value = "/uploadImg")
    public void uplodaImg(@RequestParam("upload") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            // 1. 获取tomcat路径
            String proPath = request.getSession().getServletContext().getRealPath("/");
            String path = proPath +"../../"+ IMG_PATH;
            FileUploadUtil.createDir(path);//  不存在则创建文件夹
            System.out.print("上传图片地址:" + path);


            PrintWriter out = response.getWriter();
            String CKEditorFuncNum = request.getParameter("CKEditorFuncNum");
            String fileName = file.getOriginalFilename();
            String uploadContentType = file.getContentType();
            String expandedName = "";
            if (uploadContentType.equals("image/pjpeg") || uploadContentType.equals("image/jpeg")) {
                // IE6上传jpg图片的headimageContentType是image/pjpeg，而IE9以及火狐上传的jpg图片是image/jpeg
                expandedName = ".jpg";
            } else if (uploadContentType.equals("image/png") || uploadContentType.equals("image/x-png")) {
                // IE6上传的png图片的headimageContentType是"image/x-png"
                expandedName = ".png";
            } else if (uploadContentType.equals("image/gif")) {
                expandedName = ".gif";
            } else if (uploadContentType.equals("image/bmp")) {
                expandedName = ".bmp";
            } else {
                out.println("<script type=\"text/javascript\">");
                out.println("window.parent.CKEDITOR.tools.callFunction(" + CKEditorFuncNum + ",'',"
                        + "'文件格式不正确（必须为.jpg/.gif/.bmp/.png文件）');");
                out.println("</script>");
                return;
            }
            if (file.getSize() > 1024 * 1024 * 2) {
                out.println("<script type=\"text/javascript\">");
                out.println("window.parent.CKEDITOR.tools.callFunction(" + CKEditorFuncNum + ",''," + "'文件大小不得大于2M');");
                out.println("</script>");
                return;
            }
            System.out.print("保存图片的地址：" + path + "/" + fileName);

            DateFormat df = new SimpleDateFormat(DEFAULT_SUB_FOLDER_FORMAT_AUTO);
            fileName = df.format(new Date()) + expandedName;
            file.transferTo(new File(path + "/" + fileName));
            // 返回"图像"选项卡并显示图片 request.getContextPath()为web项目名
            out.println("<script type=\"text/javascript\">");
            /*out.println("window.parent.CKEDITOR.tools.callFunction(" + CKEditorFuncNum + ",'"
                    + "http://192.168.1.52:60000/" + IMG_PATH + "/" + fileName + "','')");*/
            out.println("window.parent.CKEDITOR.tools.callFunction(" + CKEditorFuncNum + ",'"
                    + "http://192.168.1.52:60009/" + IMG_PATH  + fileName + "','')");

            out.println("</script>");
            return;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
