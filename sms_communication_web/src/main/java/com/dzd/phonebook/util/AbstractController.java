package com.dzd.phonebook.util;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AbstractController {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 将请求的参数转为bean对象
     * @param data
     * @param cla
     * @param <E>
     * @return
     */
    protected <E> E getRequestParameters(Class<E> cla,Map<String, Object> data) {
        E bean = null;
        if (data != null) {
            //将参数转成对象
            bean = GsonUtil.getBean(GsonUtil.toStr(data), cla);
        }
        return bean;
    }
}
