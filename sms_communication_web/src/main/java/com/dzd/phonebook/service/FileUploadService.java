package com.dzd.phonebook.service;

import com.dzd.base.util.StringUtil;
import com.dzd.phonebook.entity.UploadFile;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

@Service
public class FileUploadService {
    public static final Logger logger = LoggerFactory.getLogger(FileUploadService.class);

    private static final String BASE_PATH = "/plugin_path";

    //apk应用
    private static final String CALIM_PATH = "/calim/";
    //apk应用
    private static final String CALIM_TYPE = "CALIM_TYPE";


    public UploadFile saveFile(HttpServletRequest request, String type) throws IOException {
        String catalinaHome = System.getProperty("catalina.home");
        logger.info("catalinaHome -- >>" + catalinaHome);
        logger.info("saveFile -- >>");
        // 转换为文件类型的request
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获取对应file对象
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        Iterator<String> fileIterator = multipartRequest.getFileNames();
        // 获取项目的相对路径（http://localhost:8080/file）
        String requestURL = request.getRequestURL().toString();
//        String prePath = requestURL.substring(0, requestURL.indexOf("/"));
//        String proPath = request.getSession().getServletContext().getRealPath("/");
        String proPath = catalinaHome;
        //图片的存会储路径
        String path = "";
        if (CALIM_TYPE.equals(type)) {
            path = proPath + BASE_PATH + CALIM_PATH;
        }
        logger.info("path -- >>" + path);
        while (fileIterator.hasNext()) {
            String fileKey = fileIterator.next();
            logger.info("fileKey -- >>" + fileKey);
            // 获取对应文件
            MultipartFile multipartFile = fileMap.get(fileKey);
            if (multipartFile.getSize() != 0L) {
//                validateImage(multipartFile);
                // 调用saveImage方法保存
                UploadFile file = saveImage(multipartFile, path);
                if (file != null) {
                    if (!StringUtil.isEmpty(file.getFileName())) {
                        if (CALIM_TYPE.equals(type)) {
                            //设置图片的虚拟目录路径 <Context path="/img_path" docBase="${catalina.base}\\img" debug="0" reloadable="true"/>  /img_path = ${catalina.base}\\img
                            file.setPrePath(CALIM_PATH + file.getFileName());
                        }
                    }
                }
                return file;
            }
        }
        return null;
    }

    private UploadFile saveImage(MultipartFile image, String path) throws IOException {
        logger.info("saveImage  path -- >>" + path);
        String originalFilename = image.getOriginalFilename();
        logger.info("originalFilename -- >>" + originalFilename);
        String contentType = image.getContentType();
        String type = contentType.substring(contentType.indexOf("/") + 1);
        logger.info("type -- >>" + type);
//        String fileName = System.currentTimeMillis() + new Random().nextInt(100) + "." + type;
        String fileName = originalFilename ;
//        logger.info("fileName -- >>" + fileName);
        // 封装了一个简单的file对象，增加了几个属性
        UploadFile file = new UploadFile(path, fileName);
        file.setContentType(contentType);
        logger.info("saveDirectory -- >>" + file.getSaveDirectory());
        // 通过org.apache.commons.io.FileUtils的writeByteArrayToFile对图片进行保存
        FileUtils.writeByteArrayToFile(file.getFile(), image.getBytes());
        return file;
    }
    
    
}
