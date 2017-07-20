package com.dzd.sms.service.data;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-1-10.
 */
public class PhoneInfo  implements Serializable {

    String prefix;//号段
    String province;//省
    Long regionId; //地区ID
    Integer supplier; //运营商类型

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
    public Integer getSupplier() {
        return supplier;
    }

    public void setSupplier(Integer supplier) {
        this.supplier = supplier;
    }
}
