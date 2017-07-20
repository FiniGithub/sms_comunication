package com.dzd.sms.application;

import com.dzd.sms.service.data.SmsState;

/**
 * Created by IDEA
 * Author: WHL
 * Date: 2017/1/4
 * Time: 19:41
 */
public class Define {

    //任务ID
    public final static String KEY_SMS_TASK_ID = "sms_task_id";

    //通道发送结果MAP  缓存对比 发送状态对比
    public final static String KEY_SMS_AISLE_SEND_RESULT = "sms_aisle_xr";

    //CMPP提交缓存
    public final static String KEY_SMS_AISLE_SEND_RESULT_CMPP_CACHE = "sms_aisle_cmpp_submit_cache";

    //通道发送结果队列 数据库保存用
    public final static String KEY_SMS_AISLE_SEND_RESULT_DB = "sms_aisle_xr_db";
    //发送包日志
    public final static String KEY_SMS_AISLE_SEND_PACKAGE_DB = "sms_aisle_send_package_db";
    //通道报告队列 数据库保存用
    public final static String KEY_SMS_AISLE_REPORT = "sms_aisle_rp";

    //报告推送队列
    public final static String KEY_SMS_AISLE_REPORT_PUSH = "sms_aisle_report_push";
    //报告推送队列数据库
    public final static String KEY_SMS_AISLE_REPORT_PUSH_DB = "sms_aisle_report_push_db";

    //通道回复队列 数据库保存用
    public final static String KEY_SMS_AISLE_REPLY = "sms_aisle_ry";

    //回复推送队列
    public final static String KEY_SMS_AISLE_REPLY_PUSH = "sms_aisle_reply_push";
    //回复推送队列数据库
    public final static String KEY_SMS_AISLE_REPLY_PUSH_DB = "sms_aisle_reply_push_db";

    //发送任务队列 用于通道发送
    public final static String KEY_SMS_TASK = "sms_task";

    //待审核的任务
    //public final static String  KEY_SMS_TASK_PENDING = "sms_task_pending";
    public final static String KEY_SMS_TASK_PENDING_IDS = "sms_task_pending_ids";

    //任务重发
    public final static String KEY_SMS_TASK_RESEND = "sms_task_resend";

    //发送任务号码队列  用于通道发送
    public final static String KEY_SMS_TASK_MESSAGE = "sms_task_message";
    public final static String KEY_SMS_TASK_MESSAGE_NULL = "sms_task_message_null";
    public final static String KEY_SMS_TASK_MESSAGE_BLACKLIST = "sms_task_message_blacklist";
    public final static String KEY_SMS_TASK_MESSAGE_AUDITFAIL = "sms_task_message_auditfail";
    public final static String KEY_SMS_TASK_MESSAGE_MAXSENDNUMBER = "sms_task_message_maxsendnumber";
    //发送任务队列 用于快速通道发送
    public final static String KEY_SMS_TASK_FAST = "sms_task_fast";

    //发送任务队列 用于数据库保存
    public final static String KEY_SMS_TASK_DB = "sms_task_db";

    //发送任务MAP 用于缓存对比
    public final static String KEY_SMS_TASK_CACHE = "sms_task_cache";

    //今日号码的发送次数
    public final static String KEY_SMS_TODAY_PHONE_NUMBER = "key_sms_today_phone_number";


    //发送号码队列， 用于数据库保存
    //public final static String  KEY_SMS_TASK_MESSAGE_DB = "sms_task_message_db";
    public final static String KEY_SMS_TASK_MESSAGE_DB2 = "sms_task_message_db2";
    //异常队列
    public final static String KEY_SMS_TASK_EXCEPTION_CACHE = "sms_task_exception_cache";

    //详细异常队列
    public final static String KEY_SMS_DB_EXCEPTION_SEND_TASK = "sms_db_exception_send_task";
    public final static String KEY_SMS_DB_EXCEPTION_SEND_TASK_PHONE = "sms_db_exception_send_task_phone";
    public final static String KEY_SMS_DB_EXCEPTION_SEND_LOG = "sms_db_exception_send_log";
    public final static String KEY_SMS_DB_EXCEPTION_SEND_LOG_UPADATE = "sms_db_exception_send_log_update";
    public final static String KEY_SMS_DB_EXCEPTION_REPORT_LOG = "sms_db_exception_report_log";
    public final static String KEY_SMS_DB_EXCEPTION_REPORT_LOG_UPDATE = "sms_db_exception_report_log_update";
    public final static String KEY_SMS_DB_EXCEPTION_REPLY_LOG = "sms_db_exception_reply_log";


    //发送状态
    public final static int STATE_TASK_PHONE_DEFAULT = -1;//待发送
    public final static int STATE_SENDING = 99;  //已提交，运营商没有返回值 0 改成99 提交后就等运营商返回状态
    public final static int STATE_SENDED_NETWORK_ERROR = 21; //提交网络错误
    public final static int STATE_SENDED_NULL_ERROR = 22; //空号错误

    public final static int STATE_SENDED_MAXNUMBER_ERROR = 23; //超出当天发送的最大条数
    public final static int STATE_SENDED_BLACKLIST_ERROR = 24; //黑名单错误
    public final static int STATE_SENDED_AUDIT_FAIL = 25; //审核失败
    public final static int STATE_SENDED_ERROR = 26; //发送失败
    public final static int STATE_SENDED_RETURN_FAIL = 27; //发送时对方返回失败

    public final static int STATE_SENDED_REPORT_FAIL = 31; //状态报告为失败
    public final static int STATE_SENDED_REPORT_UNKNOWN = 99; //状态报告为未知

    public final static int STATE_SENDED_UNKNOWN = 3;//其它未知
    public final static int STATE_SENDED_SUCCESS = 100; //发送成功


    public final static int STATE_AUDIT_PENDING = 0;        //待审核
    public final static int STATE_AUDIT_AUTO = 1;           //自动通过
    public final static int STATE_AUDIT_MANUAL = 2;         //人工通过
    public final static int STATE_AUDIT_REFUSE = 3;         //人工拒绝

    public final static int STATE_AUDIT_NOFREETRIAL = -1;
    public final static int STATE_AUDIT_FREETRIAL = 0;

    //数据缓存
    public final static String KEY_CACHE_SMS_USER = "cache_sms_user";
    public final static String KEY_CACHE_SMS_USER_ID_KEY = "cache_sms_user_id_map_key";
    public final static String KEY_CACHE_SYS_USER_ID_KEY = "cache_sys_user_id_map_key";
    public final static String KEY_CACHE_SMS_USER_BLANK = "cache_sms_user_blank";
    public final static String KEY_CACHE_PHONE_INFO = "cache_sms_phone_info";


    public final static String KEY_CACHE_AISLE = "cache_sms_aisle";
    public final static String KEY_CACHE_AISLE_ID_MAP = "cache_sms_aisle_id_map";
    public final static String KEY_CACHE_AISLE_GROUP = "cache_sms_aisle_group";
    public final static String KEY_CACHE_AISLE_GROUP_USER = "cache_sms_aisle_group_user";
    public final static String KEY_CACHE_AISLE_GROUP_USER_BY_ID = "cache_sms_aisle_group_user_by_id";

    public final static String KEY_CACHE_AISLE_GROUP_AISLE = "cache_sms_aisle_group_aisle";
    public final static String KEY_CACHE_SHIELD = "cache_sms_shield";
    public final static String KEY_CACHE_BLACKLIST = "cache_sms_blacklist";
    public final static String KEY_CACHE_WHITELIST = "cache_sms_whitelist";
    public final static String KEY_CACHE_FREE_TRIAL = "cache_sms_free_trail";
    public final static String KEY_CACHE_SMS_PLUGIN = "cache_sms_plugin";
    public final static String KEY_CACHE_SYS_CONFIG = "cache_sys_config";


    public final static String KEY_CACHE_PROCESS_TIME = "cache_sms_process_time";

    //订阅消息
    public final static String CHANNEL_UPDATE_USER_BLANK = "channel_update_user_blank";
    public final static String CHANNEL_AB = "ab";


    //接口提交状态码
    public final static SmsState INTERFACE_STATE_SHIELDING_WORD_SUCCESS = SmsState.define(1003, "屏蔽词删除成功！");
    public final static SmsState INTERFACE_STATE_SHIELDING_WORD_EMPTY = SmsState.define(1002, "删除任务不存在，请重新检查需要删除信息！");
    public final static SmsState INTERFACE_STATE_SHIELDING_SUCCESS = SmsState.define(1001, "屏蔽词添加成功！");
    public final static SmsState INTERFACE_STATE_SHIELDING_EMPTY = SmsState.define(1000, "入参信息不能为空，请重新输入！");
    
    public final static SmsState INTERFACE_STATE_PARAM_EMPTY = SmsState.define(1000, "修改内容不能为空，请添加修改内容.");
    public final static SmsState INTERFACE_STATE_SHIELDING = SmsState.define(1001, "内容存在屏蔽词.");
    public final static SmsState INTERFACE_STATE_ID_EMPTY = SmsState.define(1002, "该任务不存在，请检查任务ID.");
    public final static SmsState INTERFACE_STATE_UPDATE_SUCCESS = SmsState.define(1003, "内容更新成功.");
    
    public final static SmsState INTERFACE_STATE_SUCCESS = SmsState.define(0, "提交成功.");
    public final static SmsState INTERFACE_STATE_KEY_INVALID = SmsState.define(1, "key无效.");
    public final static SmsState INTERFACE_STATE_NONE_AISLE_GROUP = SmsState.define(2, "没有分配用户组.");


    public final static SmsState INTERFACE_STATE_EMPTY_PHONE = SmsState.define(3, "手机号码无效.");
    public final static SmsState INTERFACE_STATE_INSUFFICIENT_BALANCE = SmsState.define(4, "帐户余额不足.");
    public final static SmsState INTERFACE_STATE_DEDUCTION_FAIL = SmsState.define(5, "扣费失败.");
    public final static SmsState INTERFACE_STATE_SHIELD = SmsState.define(6, "内容包含屏蔽词.");
    public final static SmsState INTERFACE_STATE_PARAM_ERROR = SmsState.define(7, "参数不足或参数内容为空.");
    public final static SmsState INTERFACE_STATE_KEY_UNENABLE = SmsState.define(8, "帐户停用.");
    public final static SmsState INTERFACE_STATE_SIGN_LONG = SmsState.define(9, "短信签名过长.");
    public final static SmsState INTERFACE_STATE_SIGN_ERROR = SmsState.define(9, "短信签名错误.");
    public final static SmsState INTERFACE_STATE_SIGN_NONE = SmsState.define(9, "没有短信签名. 短信签名例子：【千讯信通】");
    public final static SmsState INTERFACE_STATE_SIGN_NOEXSITS = SmsState.define(9, "短信签名不存在.");
    public final static SmsState INTERFACE_STATE_NONE_AISLE_GROUP_TYPE_ERROR = SmsState.define(13, "通道发送类型不匹配(参数：type值和通道值不匹配).");
    //查询帐户余额
    public final static SmsState INTERFACE_STATE_BALANCE_NONE = SmsState.define(21, "帐户不存在");

    // 访问次数过多
    public final static SmsState INTERFACE_STATE_ACCESS_TIME = SmsState.define(22, "访问次数过多，请稍后访问.");


    public final static int PUSH_STATE_NO = 1;//推送状态（1：未推送，2：已推送）注：推送给下家
    public final static int PUSH_STATE_ERROR = 2;
    public final static int PUSH_STATE_YES = 3;
    public final static int PUSH_STATE_URL_NULL = 4;//不用推送
    public final static int PUSH_STATE_DEFAULT = 8; //默认是8没有推送


    public interface INTERFACEKEY {
        String SEND = "send";
        String PULL_STATUS = "pull_status";
        String GET_REPLY = "get_reply";
        String GET_BALANCE = "get_balance";
    }

    public interface PHONEKEY {
        String INVALID = "invalid";// 无效号码
        String VALID = "valid";
        String DUPLICATE = "duplicate";// 重复号码
    }

    public interface STATICAL {
        String CUSTOMNUMBER = "110";
        String NUMBER = "number";
        String LENGTH = "length";
        String DATE = "date";
        String NICKNAME = "nickName";
        String NAME = "name";
        String EMAIL = "email";
        String SMSAISLENAME = "smsAisleName";
        String SMSAISLETYPEID = "smsAisleTypeId";
        String SENDNUM = "sendNum";
        String SUCCEEDNUM = "succeedNum";
        String FAILURENUM = "failureNum";
        String BILLING_NUM = "billing_num";
        String SID = "sid";
        String USERID = "userId";
        String LIMIT = "limit";
        String STATE = "state";
        String STARTTIME = "startTime";
        String SENDTIME = "sendTime";
        String CONTENT = "content";
        String ENDTIME = "endTime";
        String USER_RECEIVE_TIME = "user_receive_time";
        String MOBILE = "mobile";
        String MOBILES = "mobiles";
        String TEXT = "text";
        String REPLY_TIME = "reply_time";
        String SMSID = "smsId";
        String SYSID = "sysId";
        String SMS_REPLY = "sms_reply";
        String FILENAME = "fileName";
        String SUMSUCCEEDNUMUS = "sumSucceedNumUs";
        String SUMFAILURENUMUS = "sumFailureNumUs";
        String SUMUNKNOWNNUMUS = "sumUnknownNumUs";
        String SUMSUCCEEDNUMMS = "sumSucceedNumMs";
        String SUMFAILURENUMMS = "sumFailureNumMs";
        String SUMUNKNOWNNUMMS = "sumUnknownNumMs";
        String SUMSUCCEEDNUMTS = "sumSucceedNumTs";
        String SUMFAILURENUMTS = "sumFailureNumTs";
        String SUMUNKNOWNNUMTS = "sumUnknownNumTs";
        String UNKNOWNSUCCEEDNUM = "unknownSucceedNum";
        String UNKNOWNFAILURENUM = "unknownFailureNum";
        String SUCCESSRATE = "successRate";
        String FAILURERATE = "failureRate";
    }

    public interface RESULTSTATE {
        String CODE = "code";
        String MSG = "msg";
        String SID = "sid";
        String COUNT = "count";
        String AUDIT = "audit";
        String ERRORPHONES = "errorphones";
        String RESULT = "result";
        String SMS_STATUS = "sms_status";
        String DATA = "data";
        String TOTAL = "total";
        String ROWS = "rows";
        String BTN = "btn";
    }

    public interface FILENAME {
        String CUSTOMER_ORDER = "customer_order";
    }

    public interface REQUESTPARAMETER {
        String TODAY = "today";
        String HISTORY = "history";
        String LOGTIME = "logTime";
        String EMAIL = "email";
        String IMGCODE = "imgCode";
        String PHOCODE = "phocode";
        String NEWPWD = "newPwd";
        String OLDPWD = "oldPwd";
        String SENDTYPE = "sendType";
        String SMSUSEREMAIL = "smsUserEmail";
        String SMSUSERID = "smsUserId";
        String UID = "uid";
        String AISLENAME = "aisleName";
        String AISLENAMES = "aisleNames";
        String SIGNATURES = "signatures";
        String SHIELDWORD = "shieldWord";
        String NICKNAME = "nickName";
        String NICKNAMES = "nickNames";
        String ID = "id";
        String MENUID = "menuId";
        String OFFSET = "offset";
        String PAGENUM = "pagenum";
        String PAGESIZE = "pagesize";
        String IDS = "ids";
        String SMSUSER = "smsUser";
        String PHONE = "phone";
        String BGZTSELECT = "bgztSelect";
        String EXPORT = "export";
        String STARTINPUT = "startInput";
        String ENDINPUT = "endInput";
        String TASKID = "taskId";
        String TASKIDS = "taskIds";
        String SENDTYPES = "sendTypes";
        String TIMING = "timing";
        String BILLINGNNUM = "billingNnum";
        String PHONES = "phones";
        String FORWARDUUID = "forwardUuid";
        String VALIDPHONENUM = "validPhoneNum";
    }

    public interface MANAGEMENTUSER {
        String CONTACT = "contact";
        String PHONE = "phone";
        String CREATETIME = "createTime";
        String SIGNATURE = "signature";
        String AISLEGROUP = "aisleGroup";
        String NETWORKCHARGINGSTATE = "networkChargingState";
        String ROLENAME = "roleName";
        String NICKNAME = "nickName";
    }

    public interface REDISCLIENTKEY {
        String SMS_DAYLOG_ORDER = "sms_daylog_order";
        String SMS_HISTORYLOG_ORDER = "sms_historylog_order";
    }

    public interface DISTINGUISHOPERATOR {
        String MOBILEOPERATOR = "mobileOperator";//移动
        String UNICOMOPERATOR = "unicomOperator";//联通
        String TELECOMOPERATOR = "telecomOperator";//电信
        String INVALIDOPERATOR = "invalidOperator";//未知

        String MOBILETOTALPAGE = "mobileTotalPage";//
        String UNICOMTOTALPAGE = "unicomTotalPage";//
        String TELECOMTOTALPAGE = "telecomTotalPage";//
        String INVALIDTOTALPAGE = "invalidTotalPage";//

        String MOBILECURRENTPAGE = "mobileCurrentPage";//
        String UNICOMCURRENTPAGE = "unicomCurrentPage";//
        String TELECOMCURRENTPAGE = "telecomCurrentPage";//
        String INVALIDCURRENTPAGE = "invalidCurrentPage";//

        String TELECOMLENGTH = "telecomLength";
        String MOBILELENGTH = "mobileLength";
        String UNICOMLENGTH = "unicomLength";
        String INVALIDLENGTH = "invalidLength";
    }


    public interface USERSENDSMS {
        // 过滤中文英文数字的正则
        String SMS_SEND_REGEX = "[^(a-zA-Z0-9\\u4e00-\\u9fa5)]";
        // 发送短信校验提示语
        String SMS_SEND_CONTENT_IS_NULL = "请输入短信内容!";
        String SMS_SEND_MOBILE_IS_NULL = "无可发送号码!";
        String SMS_SEND_CONTENT_UNREG = "退订";
        String SMS_SEND_AISLE_GROUP_CLOSE = "当前通道关闭中！";
        String SMS_SEND_AISLE_UNOPEN = "现在是通道关闭时段！\n" + "请在通道开通时段提交发送短信。";
        String SMS_SEND_AISLE_FORMAT = "短信内容结尾须加：\"回T退订\"或\"退订回T\"!";
        String SMS_SEND_AISLE_HUIFU = "退订格式中的回复内容只能为字母!";
        String SMS_SEND_TIMING_INVALID = "您预定的时间无效.";
        String SMS_SEND_TIMING_BEFORE = "预定发送时间需设置为10分钟以后!\n" + "请重新设置发送时间.";
        String SMS_SEND_USER_CLOSE = "您的账户已关闭短信发送！";
        String SMS_SEND_SYSTEM_ERROR = "服务器故障,请稍后再试.";
        String SMS_SEND_SHIELDWORD = "短信内容中含有敏感词：";
        String SMS_SEND_SIGNATURE = "请输入签名!";
        String SMS_SEND_SIGNTURE_LENGTH = "签名字数要求3—8个，请重新输入！";
        String SMS_SEND_SIGNTRUE_DOUBLE_MSG = "短信内容中不能重复使用签名符号 【】,请返回修改。";
        String SMS_SEND_INSUFFICIENT_BALANCE = "您的账户余额不足！请充值。";
        String SMS_SEND_SPECIALSYMBOLMSG = "短信内容中含有特殊符号：";
        String SMS_SEND_VERIFYCODE_MSG = "请输入验证码.";


    }

    //角色id
    public interface ROLEID {
        String ROLE_ADMINISTRATOR = "3"; //超级管理员
        String AGENT = "47"; //超级管理员
        String ROLE_FIRST_LEVEL_ADMINISTRATOR = "48"; //一级管理员
        String ROLE_SALES_MANAGER = "51"; //销售经理
        String ROLE_USER_ADMINISTRATOR = "52"; //用户管理员
        String ROLE_CUSTOMER_SERVICE = "58"; //客服
        String ROLE_SALESMAN = "59"; //业务员
        String ROLE_USER = "60"; //用户
        Integer LEVEL_1 = 1;  //一级用户
        Integer LEVEL_2 = 2;  //二级用户
        Integer LEVEL_3 = 3;  //三级用户
    }
    public interface QUERYINFO {
    	String SMSUSERID = "smsUserId";
    	String SENDTASKID = "sendTaskId";
    	String SENDTIME = "sendTime";
    }
    
    public interface SMSSENDTASK {
    	String ID = "id";
    	String CONTENT = "content";
        String CODE = "code";
        String MSG = "msg";
    }
    
    public interface SHIELDINGWORD {
		String WORD = "word";
		String DELETEMODEL = "deleteModel";
	}
}
