package com.dzd.phonebook.util;

import com.dzd.phonebook.service.ChannelService;
import com.dzd.phonebook.service.SmsSpecificSymbolService;
import com.dzd.phonebook.service.SmsWordShieldingService;

/**
 * 服务辅助类
 * Created by CHENCHAO on 2017/6/9.
 */
public class ServiceBeanUtil {
    private SmsSpecificSymbolService symbolervice;
    private SmsWordShieldingService wordService;
    private ChannelService channelService;

    public SmsSpecificSymbolService getSymbolervice() {
        return symbolervice;
    }

    public void setSymbolervice(SmsSpecificSymbolService symbolervice) {
        this.symbolervice = symbolervice;
    }

    public SmsWordShieldingService getWordService() {
        return wordService;
    }

    public void setWordService(SmsWordShieldingService wordService) {
        this.wordService = wordService;
    }

    public ChannelService getChannelService() {
        return channelService;
    }

    public void setChannelService(ChannelService channelService) {
        this.channelService = channelService;
    }
}
