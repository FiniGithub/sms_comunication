package com.dzd.phonebook.controller.base;



import com.dzd.phonebook.util.AbstractController;
import com.dzd.phonebook.util.DzdPageParam;
import com.dzd.phonebook.util.WebRequestParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.util.Map;

/**
 * @author dlluo
 * @Description:
 * @date 2016-4-19
 */
public class WebBaseController extends AbstractController {
    public Logger logger = LoggerFactory.getLogger(getClass());
    /*封装jsp请求带有分页的参数*/
    protected DzdPageParam getDzdPageParam(Map<String,Object> data)  {
        WebRequestParameters webRequestParameters = getRequestParameters(WebRequestParameters.class, data);
        return new DzdPageParam(webRequestParameters.getPagenum(), webRequestParameters.getPagesize());
    }


    protected Map<String, Object> getSysConfig() {
        Map<String, Object> sysConfig = null;
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        ServletContext application = webApplicationContext.getServletContext();
        Object sys=application.getAttribute("configMap");
        if(sys != null){
            sysConfig = (Map<String, Object>)sys;
        }
        return sysConfig;
    }


    protected Object getSysConfigByKey(String key) {
        Object value = null;
        Map<String, Object> sysConfig  = getSysConfig();
        value = sysConfig.get(key);
        return value;

    }




}
