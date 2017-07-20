package com.dzd.phonebook.util;

import java.util.Date;
import java.util.List;

import com.dzd.base.page.BasePage;
import com.dzd.phonebook.entity.SysMenuBtn;

/**
 * 资金操作记录
 *
 * @Description:
 * @author:liuyc
 * @time:2017年1月14日 下午4:50:54
 */
public class SmsUserMoneyRunning extends BasePage {

    private List<SysMenuBtn> sysMenuBtns;//菜单拥有的按钮

    private Integer id;                    //ID
    private Integer uid;                //操作人ID
    private Integer type;                //操作类型（0:充值，1:发送扣费，2：返还扣费）
    private Integer smsUserId;            //备操作账户
    private Float beforeMoney;            //操作前金额 【删除】
    private Float afterMoney;            //操作后金额 【删除】
    private Float money;                //充值金额
    private Integer beforeNum;            //操作前条数
    private Integer afterNum;            //操作后条数
    private Integer operateNum;            //操作条数
    private Date createTime;            //操作时间
    private String comment;                //备注
    private String email;               //代理商账号
    private String name;                //代理商名称
    private String uaccount;// 操作人账号
    private String orderNo;// 订单编号
    private String bname;// 归属

    private int consumeNumber;            //消费总和
    private int topUpNumber;            //充值总和
    private int refundNumber;            //退款总和

    private Float consumeNum;            //消费总和
    private Float topUpNum;                //充值总和
    private Float refundNum;            //退款总和

    private Integer superAdmin;// 管理员
    private Integer bid;// 归属
    private String startInput;// 开始时间
    private String endInput;// 结束时间


    public int getConsumeNumber() {
        return consumeNumber;
    }

    public void setConsumeNumber(int consumeNumber) {
        this.consumeNumber = consumeNumber;
    }

    public int getTopUpNumber() {
        return topUpNumber;
    }

    public void setTopUpNumber(int topUpNumber) {
        this.topUpNumber = topUpNumber;
    }

    public int getRefundNumber() {
        return refundNumber;
    }

    public void setRefundNumber(int refundNumber) {
        this.refundNumber = refundNumber;
    }

    public String getUaccount() {
        return uaccount;
    }

    public void setUaccount(String uaccount) {
        this.uaccount = uaccount;
    }

    public List<SysMenuBtn> getSysMenuBtns() {
        return sysMenuBtns;
    }

    public void setSysMenuBtns(List<SysMenuBtn> sysMenuBtns) {
        this.sysMenuBtns = sysMenuBtns;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSmsUserId() {
        return smsUserId;
    }

    public void setSmsUserId(Integer smsUserId) {
        this.smsUserId = smsUserId;
    }

    public Float getBeforeMoney() {
        return beforeMoney;
    }

    public void setBeforeMoney(Float beforeMoney) {
        this.beforeMoney = beforeMoney;
    }

    public Float getAfterMoney() {
        return afterMoney;
    }

    public void setAfterMoney(Float afterMoney) {
        this.afterMoney = afterMoney;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getConsumeNum() {
        return consumeNum;
    }

    public void setConsumeNum(Float consumeNum) {
        this.consumeNum = consumeNum;
    }

    public Float getTopUpNum() {
        return topUpNum;
    }

    public void setTopUpNum(Float topUpNum) {
        this.topUpNum = topUpNum;
    }

    public Float getRefundNum() {
        return refundNum;
    }

    public void setRefundNum(Float refundNum) {
        this.refundNum = refundNum;
    }

    public Integer getBeforeNum() {
        return beforeNum;
    }

    public void setBeforeNum(Integer beforeNum) {
        this.beforeNum = beforeNum;
    }

    public Integer getAfterNum() {
        return afterNum;
    }

    public void setAfterNum(Integer afterNum) {
        this.afterNum = afterNum;
    }

    public Integer getOperateNum() {
        return operateNum;
    }

    public void setOperateNum(Integer operateNum) {
        this.operateNum = operateNum;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getStartInput() {
        return startInput;
    }

    public void setStartInput(String startInput) {
        this.startInput = startInput;
    }

    public String getEndInput() {
        return endInput;
    }

    public void setEndInput(String endInput) {
        this.endInput = endInput;
    }

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

    public Integer getSuperAdmin() {
        return superAdmin;
    }

    public void setSuperAdmin(Integer superAdmin) {
        this.superAdmin = superAdmin;
    }
}
