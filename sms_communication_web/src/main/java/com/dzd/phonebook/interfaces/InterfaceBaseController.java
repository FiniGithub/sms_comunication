package com.dzd.phonebook.interfaces;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;


public class InterfaceBaseController {

    protected Map<String, Object> getSysConfig() {
        Map<String, Object> sysConfig = null;
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        ServletContext application = webApplicationContext.getServletContext();
        Object sys = application.getAttribute("configMap");
        if (sys != null) {
            sysConfig = (Map<String, Object>) sys;
        }
        return sysConfig;
    }


    protected Object getSysConfigByKey(String key) {
        Object value = null;
        Map<String, Object> sysConfig = getSysConfig();
        value = sysConfig.get(key);
        return value;

    }


    /**
     * 根据请求的头信息
     *
     * @param request
     * @return
     */
    protected Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }


}
