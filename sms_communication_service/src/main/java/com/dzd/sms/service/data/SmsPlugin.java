package com.dzd.sms.service.data;

import java.io.Serializable;

/**
 * Created by IDEA
 * Author: WHL
 * Date: 2017/3/7
 * Time: 17:32
 */
public class SmsPlugin  implements Serializable {

    Long id;
    String name;
    String path;
    String className;
    String aisleName;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getAisleName() {
        return aisleName;
    }

    public void setAisleName(String aisleName) {
        this.aisleName = aisleName;
    }
}
