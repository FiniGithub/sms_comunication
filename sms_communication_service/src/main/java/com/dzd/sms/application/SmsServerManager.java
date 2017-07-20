package com.dzd.sms.application;

import static com.dzd.sms.application.Define.KEY_CACHE_PHONE_INFO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.dzd.sms.service.data.*;
import com.dzd.sms.task.db.*;
import com.dzd.sms.task.push.SmsReplyPushTask;
import com.dzd.sms.task.push.SmsReportPushTask;
import com.dzd.sms.task.quartz.QuartzJobManager;
import com.dzd.utils.*;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.FileCopyUtils;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;
import org.xeustechnologies.jcl.JclUtils;

import com.dzd.cache.redis.manager.RedisClient;
import com.dzd.cache.redis.manager.RedisManager;
import com.dzd.db.mysql.MysqlOperator;
import com.dzd.sms.addons.aisle.BaseAisle;
import com.dzd.sms.addons.aisle.BaseAisleAdapter;
import com.dzd.sms.addons.aisle.ext.SmsAuditFailAisle;
import com.dzd.sms.addons.aisle.ext.SmsBlacklistAisle;
import com.dzd.sms.addons.aisle.ext.SmsMaxNumberAisle;
import com.dzd.sms.addons.aisle.ext.SmsNullAisle;
import com.dzd.sms.task.aisle.SmsSendTask;
import com.dzd.sms.task.base.BaseTask;
import com.dzd.sms.task.sub.TaskSubscriber;

import net.sf.json.JSONObject;

/**
 * 提供短信服务的管理
 *
 * @Author WHL
 * @Date 2017-3-24.
 */
public class SmsServerManager {


    private static Logger logger = Logger.getLogger(SmsServerManager.class);


    //单例模式
    public final static SmsServerManager I = new SmsServerManager();


    //    public Map<String, PhoneInfo> phoneInfoMap = new ConcurrentHashMap<String, PhoneInfo>();
    public Map<String, String> phoneInfoMap2 = new ConcurrentHashMap<String, String>();

    //REDIS操作对象
    public RedisClient redisClient = RedisManager.I.getRedisClient();

    //异步处理队列
    public Map<String, BaseTask> taskMap = new HashMap<String, BaseTask>();

    //缓存通道BEAN信息
    public Map<Long, BaseAisle> aisleMap = new HashMap<Long, BaseAisle>();
    public Map<String, BaseAisle> aisleMapExts = new HashMap<String, BaseAisle>();

    //缓存通道插件对象信息
    public Map<String, BaseAisleAdapter> aisleAdapterMap = new HashMap<String, BaseAisleAdapter>();


    // 屏蔽词分等级制
    public List<ShieldWord> shieldWords = new ArrayList<ShieldWord>();


    final TaskSubscriber subscriber = new TaskSubscriber();


    String pluginDir = "";//插件保存目录
    String cacheFiile = "";// 缓存文件存放目录
    String fileUpload = "";// 号码文件存放保存目录
    boolean isInited = false;
    public boolean isWindows = false;


    //定义分包大小

    public SmsServerManager() {
        init();
    }

    /**
     * 启动服务， 初始化相关的缓存
     */
    public void start() {

    }


    /**
     * 初始化相关的缓存
     */
    public void init() {

        //初始化当前运行环境
        initEnv();
        initUploadEnv();

        //初始化缓存信息
        synchronousAllData();

        //订阅通知
        sub();

        //初始化对应的扩展通道
        aisleMapExts.put("SmsAuditFailAisle", new SmsAuditFailAisle());
        aisleMapExts.put("SmsBlacklistAisle", new SmsBlacklistAisle());
        aisleMapExts.put("SmsMaxNumberAisle", new SmsMaxNumberAisle());
        aisleMapExts.put("SmsNullAisle", new SmsNullAisle());


        //创建处理任务处理线程
        taskMap.put("SmsSendTask", new SmsSendTask());
        taskMap.put("SaveSendTaskPhone", new SaveSendTaskPhone());
        taskMap.put("SaveSendTaskPending", new SaveSendTaskPending());
        taskMap.put("SaveSendLog", new com.dzd.sms.task.db.SaveSendLog());
        taskMap.put("SaveReportLog", new SaveReportLog());
        taskMap.put("SaveReplyLog", new SaveReplyLog());
        taskMap.put("SmsReplyPushTask", new SmsReplyPushTask());
        taskMap.put("SmsReportPushTask", new SmsReportPushTask());
        taskMap.put("UpdateReplyPushLog", new UpdateReplyPushLog());
        taskMap.put("UpdateReportPushLog", new UpdateReportPushLog());
        taskMap.put("SaveSendPackageLog", new SaveSendPackageLog());
        taskMap.put("TaskResend", new TaskResend());

        //启动任务
        for (String key : taskMap.keySet()) {
            taskMap.get(key).start();
        }


        //定时器更新程序
        QuartzJobManager.I.start();

    }


    public void initEnv() {

        //判断当前运行环境
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {

            System.out.println(os + " is debuging...");
            isWindows = true;

            String userDir = System.getProperty("user.dir");
            int separatorLastPos = userDir.lastIndexOf(File.separator);
            pluginDir = userDir.substring(0, separatorLastPos) + File.separator + "aplugins";
            System.out.println("plugin path=" + pluginDir);

            java.io.File f = new File(pluginDir);
            if (!f.exists()) {
                if (f.mkdirs()) {
                    //创建目录
                    System.out.println("------- plugins create success !--------");
                } else {
                    System.out.println("*************** plugins create fail !*********");
                }
            } else {
                System.out.println(" pluginDir is exists!");
            }
        } else {
            pluginDir = "/data/aplugins";
            System.out.println("plugin path=" + pluginDir);
            java.io.File f = new File(pluginDir);
            if (!f.exists()) {
                if (f.mkdirs()) {
                    //创建目录
                    System.out.println("------- plugins create success !--------");
                } else {
                    System.out.println("*************** plugins create fail !*********");
                }
            } else {
                System.out.println(" pluginDir is exists!");
            }
        }
    }


    public void initUploadEnv() {

        //判断当前运行环境
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {

            System.out.println(os + " is debuging...");
            isWindows = true;

            String userDir = System.getProperty("user.dir");
            int separatorLastPos = userDir.lastIndexOf(File.separator);
            fileUpload = userDir.substring(0, separatorLastPos) + File.separator + "fileUpload";
            System.out.println("fileUpload path=" + fileUpload);

            java.io.File f = new File(fileUpload);
            if (!f.exists()) {
                if (f.mkdirs()) {
                    //创建目录
                    System.out.println("------- fileUpload create success !--------");
                } else {
                    System.out.println("*************** fileUpload create fail !*********");
                }
            } else {
                System.out.println(" fileUpload is exists!");
            }
        } else {
            fileUpload = "/data/fileUpload";
            System.out.println("fileUpload path=" + fileUpload);
            java.io.File f = new File(fileUpload);
            if (!f.exists()) {
                if (f.mkdirs()) {
                    //创建目录
                    System.out.println("------- fileUpload create success !--------");
                } else {
                    System.out.println("*************** fileUpload create fail !*********");
                }
            } else {
                System.out.println(" fileUpload is exists!");
            }
        }
    }


    /**
     * 停止服务
     */
    public void destory() {

    }


    /**
     * 获取系统配置信息
     */
    public String getSysConfig(String key) {
        return redisClient.hget(Define.KEY_CACHE_SYS_CONFIG, key).toString();
    }


    /**
     * 查询用户信息
     */
    public SmsUser getUser3(String key) {
        if (redisClient.hexists(Define.KEY_CACHE_SMS_USER, key.toString())) {
            return (SmsUser) redisClient.hgetObject(Define.KEY_CACHE_SMS_USER, key);
        } else {
            logger.error(" key=" + key + " 没有找到用户。");
        }
        return null;
    }

    /**
     * 通过UID获取用户
     *
     * @param userId
     * @return
     */
    public SmsUser getUser(Long userId) {
        if (redisClient.hexists(Define.KEY_CACHE_SMS_USER_ID_KEY, userId.toString())) {
            String key = redisClient.hget(Define.KEY_CACHE_SMS_USER_ID_KEY, userId.toString()).toString();
            return getUser3(key);
        } else {
            logger.error(" userId=" + userId + " 没有找到用户。");
        }
        return null;
    }

    /**
     * 通过UID获取用户
     *
     * @param sysId
     * @return
     */
    public SmsUser getUserBySysId(Long sysId) {
        if (redisClient.hexists(Define.KEY_CACHE_SYS_USER_ID_KEY, sysId.toString())) {
            String key = redisClient.hget(Define.KEY_CACHE_SYS_USER_ID_KEY, sysId.toString()).toString();
            return getUser3(key);
        } else {
            logger.error(" userId=" + sysId + " 没有找到用户。");
        }
        return null;
    }

    /**
     * 通过UID开通CMPP协议帐户
     */
    public void openCmpp(Long userId) {
        SmsUser smsUser = getUser(userId);
        if (smsUser != null) {
            //打开服务端口
            if (smsUser.isCmppProtocol()) {
                logger.info(smsUser.getAccount() + " ---isCmppProtocol-openCmpp-by-id-");
                //CmppServiceManager.addCmppService( smsUser);
            } else {
                //CmppServiceManager.removeCmppService( smsUser.getFirmName() );
            }
        }
    }

    /**
     * 直接打开CMPP通道
     *
     * @param smsUser
     */
    public void openCmpp(SmsUser smsUser) {
        if (smsUser != null) {
            //打开服务端口
            if (smsUser.isCmppProtocol()) {
                logger.info(smsUser.getAccount() + " ---isCmppProtocol-openCmpp-by-smsUser-");
                //CmppServiceManager.addCmppService( smsUser);
            } else {
                //CmppServiceManager.removeCmppService( smsUser.getFirmName() );
            }
        }
    }

    /**
     * 查询用户余额信息
     */
    public SmsUserBlank getUserBlank(Long userId) {
        if (redisClient.hexists(Define.KEY_CACHE_SMS_USER_BLANK, userId.toString())) {
            return (SmsUserBlank) redisClient.hgetObject(Define.KEY_CACHE_SMS_USER_BLANK, userId.toString());
        }
        return null;
    }


    /**
     * 查询用户余额
     */
    public int getUserSurplusNum(Long userId) {
        if (getUserBlank(userId) == null) {
            return 0;
        }
        return getUserBlank(userId).getSurplus_num();

    }

    public boolean addUserBalance(Long userId, int surplus_num, String comment) {
        return updateUserBalance(userId, surplus_num, comment);
    }

    public boolean reduceUserBalance(Long userId, int surplus_num, String comment) {
        return updateUserBalance(userId, 0 - surplus_num, comment);
    }

    /**
     * 更新用户余额
     * 添加或减少帐户信息
     */
    public synchronized boolean updateUserBalance(Long userId, int surplus_num) {
        return updateUserBalance(userId, surplus_num, "-");
    }

    public synchronized boolean updateUserBalance(Long userId, int surplus_num, String comment) {
        try {

            if (surplus_num == 0) {
                return false;
            }
            SmsUserBlank smsUserBlank = getUserBlank(userId);
            String sql = "";
            if (surplus_num > 0) {
                sql = "update sms_user_blank set surplus_num=surplus_num+" + surplus_num + " where user_id=" + userId;
            } else {
                //一定要加上，否则会多扣
                if (smsUserBlank.getSurplus_num() < 0 - surplus_num) {
                    return false;
                }
                sql = "update sms_user_blank set surplus_num=surplus_num-" + (0 - surplus_num) + " where user_id=" + userId;
            }
            MysqlOperator.I.update(sql);

            logger.info("userblack sql=" + sql);
            int before_num = smsUserBlank.getSurplus_num();
            smsUserBlank.setSurplus_num(smsUserBlank.getSurplus_num() + surplus_num);
            redisClient.hsetObject(Define.KEY_CACHE_SMS_USER_BLANK, userId.toString(), smsUserBlank);


            int type = 1;
            if (surplus_num > 0) {
                type = 2;
            }


            sql = "insert into sms_user_money_running(uid, `type`,sms_user_id,create_time,`comment`,before_num,after_num,operate_num) VALUES (" +
                    "0," +
                    type + "," +
                    userId + "," +
                    "NOW()," +
                    "'" + comment + "'" + "," +
                    before_num + "," +
                    (before_num + surplus_num) + "," +
                    surplus_num +
                    ")";
            logger.info("user blank detail add sql= " + sql);
            MysqlOperator.I.execute(sql);


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 获取号码运营商信息
     */
    public PhoneInfo getPhoneInfo(String prefix) {
        //return (PhoneInfo) redisClient.hgetObject(KEY_CACHE_PHONE_INFO ,  prefix );
        String phoneInfoStr = phoneInfoMap2.get(prefix);
        if (phoneInfoStr == null) {
            return null;
        }
        String[] phoneInfoStrArr = StringUtil.split(phoneInfoStr, ",");
        PhoneInfo phoneInfo = new PhoneInfo();
        phoneInfo.setProvince(phoneInfoStrArr[1]);
        phoneInfo.setRegionId(Long.valueOf(phoneInfoStrArr[2]));
        phoneInfo.setSupplier(Integer.valueOf(phoneInfoStrArr[3]));
        return phoneInfo;

    }

    /**
     * 根据用户信息选择通道组关系
     */
    public SmsAisleGroupUserRelation getAisleGroupUser(Long userId, int smsType) {
        int aisleType = 0;
        SmsAisleGroupUserRelation smsAisleGroupUserRelation = null;
        Map<byte[], byte[]> map = redisClient.hgetAll((Define.KEY_CACHE_AISLE_GROUP_USER + userId));
        for (Map.Entry<byte[], byte[]> entry : map.entrySet()) {
            SmsAisleGroupUserRelation sg = (SmsAisleGroupUserRelation) ObjectUtils.unserialize(entry.getValue());
            Long groupId = sg.getGroupId();

            System.out.println("getGroupId=" + groupId + "SmsAisleGroupUserRelation id= " + sg.getId());
            Object val = redisClient.hgetObject(Define.KEY_CACHE_AISLE_GROUP, groupId.toString());
            SmsAisleGroup smsAisleGroup1 = (SmsAisleGroup) val;

            if (sg.getSmsType() == smsType) {
                smsAisleGroupUserRelation = sg;
            }

        }
        return smsAisleGroupUserRelation;
    }


    /**
     * 根据接口ID查找对应的通道信息
     *
     * @param iid
     * @return
     */
    public SmsAisleGroupUserRelation getAisleGroupUserById(Long uid, String iid) {
        SmsAisleGroupUserRelation smsAisleGroupUserRelation = (SmsAisleGroupUserRelation) redisClient.hgetObject((Define.KEY_CACHE_AISLE_GROUP_USER_BY_ID + uid), iid);
        logger.info("getAisleGroupUserById uid=" + uid + " iid=" + iid);
        return smsAisleGroupUserRelation;
    }

    /**
     * 通道组 和 通道关系信息
     *
     * @param groupId
     * @return
     */
    public SmsAisleSelectParam getAisleGroupRelation(Long groupId) {

        SmsAisleSelectParam smsAisleSelectParam = new SmsAisleSelectParam();
        Object val = redisClient.hgetObject(Define.KEY_CACHE_AISLE_GROUP, groupId.toString());
        SmsAisleGroup smsAisleGroup1 = (SmsAisleGroup) val;


        final int sortType = smsAisleGroup1.getSort();
        smsAisleSelectParam.setGroupSort(sortType);

        //通过通道组， 找通道
        Map<byte[], byte[]> map2 = redisClient.hgetAll((Define.KEY_CACHE_AISLE_GROUP_AISLE + groupId));
        List<SmsAisleGroupAisleRelation> smsAisleGroupAisleRelationList = new ArrayList<SmsAisleGroupAisleRelation>();
        for (Map.Entry<byte[], byte[]> entry : map2.entrySet()) {
            SmsAisleGroupAisleRelation sa = (SmsAisleGroupAisleRelation) ObjectUtils.unserialize(entry.getValue());
            if (sa != null) {
                smsAisleGroupAisleRelationList.add(sa);
            } else {

            }
        }

        //按通道组做排序
        if (smsAisleGroupAisleRelationList.size() > 0) {
            Collections.sort(smsAisleGroupAisleRelationList, new Comparator<SmsAisleGroupAisleRelation>() {
                /*
                 * int compare(  o1,   o2) 返回一个基本类型的整型，
                 * 返回负数表示：o1 小于o2，
                 * 返回0 表示：o1和o2相等，
                 * 返回正数表示：o1大于o2。
                 */
                public int compare(SmsAisleGroupAisleRelation o1, SmsAisleGroupAisleRelation o2) {
                    if (sortType == 1) {
                        //进行升序排列
                        if (o1.getSort().intValue() > o2.getSort().intValue()) {
                            return 1;
                        }
                        if (o1.getSort().intValue() == o2.getSort().intValue()) {
                            return 0;
                        }
                        return -1;
                    } else {
                        //进行升序排列
                        if (o1.getSmsAisle().getPrice().longValue() > o2.getSmsAisle().getPrice().longValue()) {
                            return 1;
                        }
                        if (o1.getSmsAisle().getPrice().longValue() == (o2.getSmsAisle().getPrice().longValue())) {
                            return 0;
                        }
                        return -1;
                    }
                }
            });
        }

        smsAisleSelectParam.setSmsAisleGroupAisleRelationList(smsAisleGroupAisleRelationList);
        return smsAisleSelectParam;

    }

    /**
     * 构建缓存队列KEY
     *
     * @param taskId
     * @return
     */
    public String getSmsMessageQueueKey(Long taskId) {
        return Define.KEY_SMS_TASK_MESSAGE + taskId;
    }

    /**
     * 从缓存中获取通道对象
     *
     * @param aisleName
     * @return
     */
    public BaseAisle getAisle(String aisleName) {

        if (aisleMapExts.containsKey(aisleName)) {
            return aisleMapExts.get(aisleName);
        }
        return null;
    }


    /**
     * 从缓存中获取多个通道ID
     *
     * @param aisleId
     * @return
     */
    public List<String> getSmsAisleIds(Long aisleId) {

        logger.info("getSmsAisleIds aisleId=" + aisleId);
        List<String> aisleIds = new ArrayList<String>();
        //Object value = redisClient.hgetObject( Define.KEY_CACHE_AISLE_ID_MAP,aisleId.toString());
        Object value = redisClient.hget(Define.KEY_CACHE_AISLE_ID_MAP, aisleId.toString());
        if (value != null) {
            String idsStr = (String) value;

            aisleIds = StringUtil.StringToList(idsStr, ",");
            logger.info("find-aisleIds=" + aisleIds);
        }
        return aisleIds;
    }


    /**
     * 获取通道信息
     */
    public SmsAisle getSmsAisle(Long id) {
        if (redisClient.hexists(Define.KEY_CACHE_AISLE, id.toString())) {
            return (SmsAisle) redisClient.hgetObject(Define.KEY_CACHE_AISLE, id.toString());
        }
        return null;
    }


    /**
     * 添加任务处理的号码数量到缓存中
     *
     * @param taskId
     * @param n
     */
    public synchronized void addSmsTaskSaveSendPhoneNumber(String taskId, int n) {

        logger.info("addSmsTaskSaveSendPhoneNumber.taskId=" + taskId + ",n=" + n);

        String key = taskId;
        SmsTaskData smsTaskData = (SmsTaskData) redisClient.hgetObject(Define.KEY_SMS_TASK_CACHE, key);
        smsTaskData.setSaveSendPhoneNumber(smsTaskData.getSaveSendPhoneNumber() + n);
        //存放进缓存
        redisClient.hsetObject(Define.KEY_SMS_TASK_CACHE, key, smsTaskData);
    }

    public synchronized void addSmsTaskSaveSendPhoneNumber(SmsTaskData smsTaskData, String taskId, int n) {

        smsTaskData.setSaveSendPhoneNumber(smsTaskData.getSaveSendPhoneNumber() + n);
        //存放进缓存
        redisClient.hsetObject(Define.KEY_SMS_TASK_CACHE, taskId, smsTaskData);
    }

    public synchronized void addSmsTaskSaveReportPhoneNumber(SmsTaskData smsTaskData, int n) {
        smsTaskData.setSaveReportPhoneNumber(smsTaskData.getSaveReportPhoneNumber() + n);
        //存放进缓存
        redisClient.hsetObject(Define.KEY_SMS_TASK_CACHE, smsTaskData.getTaskId().toString(), smsTaskData);
    }

    public synchronized void addSmsTaskSaveReportPhoneNumber(String taskId, int n) {

        logger.info("addSmsTaskSaveReportPhoneNumber.taskId=" + taskId + ",n=" + n);

        SmsTaskData smsTaskData = (SmsTaskData) redisClient.hgetObject(Define.KEY_SMS_TASK_CACHE, taskId);
        smsTaskData.setSaveReportPhoneNumber(smsTaskData.getSaveReportPhoneNumber() + n);
        //存放进缓存
        redisClient.hsetObject(Define.KEY_SMS_TASK_CACHE, taskId, smsTaskData);


    }

    public SmsTaskData getSmsTaskDataFromCache(String taskId) {
        return (SmsTaskData) redisClient.hgetObject(Define.KEY_SMS_TASK_CACHE, taskId);
    }

    /**
     * 通道组和通道的关联信息
     *
     * @param id
     * @return
     */
    public List<SmsAisleGroupAisleRelation> getAisleGroupRelationList(Long id) {
        return (List<SmsAisleGroupAisleRelation>) redisClient.hgetObject(Define.KEY_CACHE_AISLE_GROUP_AISLE, id.toString());
    }

    /**
     * 获取通道组信息
     */
    public SmsAisleGroup getSmsAislGroup(Long id) {
        return (SmsAisleGroup) redisClient.hgetObject(Define.KEY_CACHE_AISLE_GROUP, id.toString());
    }


    /**
     * 获取屏蔽词信息
     */
    public List<String> getShieldList() {
        return (List<String>) redisClient.getObject(Define.KEY_CACHE_SHIELD);
    }

    public List<ShieldWord> getShieldObjectList() {
        return shieldWords;
    }

    /**
     * 判断是否为免审模板
     *
     * @param userid
     * @param content
     * @return
     */
    public boolean isFreeTrial(Long userid, String content) {
        boolean re = false;
        String regex = "(.*?)[0-9]{0,8}(.*?)";

        String _content = content.replaceAll(regex, "$1#code#$2");

        logger.info("_content" + _content + redisClient.hlen(Define.KEY_CACHE_FREE_TRIAL + userid));
        String key = StringUtil.string2MD5(_content);

        if (redisClient.hexists(Define.KEY_CACHE_FREE_TRIAL + userid, key)) {
            logger.info("isFreeTrial ok ");
            return true;
        } else {
            //不通过
            Map<byte[], byte[]> map2 = redisClient.hgetAll(Define.KEY_CACHE_FREE_TRIAL + userid);
            for (byte[] c : map2.keySet()) {
                logger.info("c=" + c + " content=" + content);
                if (map2.get(c).equals(content)) {
                    return true;
                } else {
                    String v = new String(map2.get(c));
                    String contentRegex = v.replaceAll("#code#", "([0-9]{0,8})");
                    logger.info("content Number Regex" + contentRegex);
                    if (content.matches(contentRegex)) {
                        return true;
                    }
                    contentRegex = v.replaceAll("@", "([^@]{0,8})");
                    logger.info("content Word Regex" + contentRegex);
                    if (content.matches(contentRegex)) {
                        return true;
                    }
                }
            }
        }
        return re;
    }


    /**
     * 缓存今日号码发送次数
     */
    public int getTodaySendNumber(String phone) {
        String todaySendNumberKey = (Define.KEY_SMS_TODAY_PHONE_NUMBER + DateUtil.formatDate());
        int n = 1;
        if (redisClient.hexists(todaySendNumberKey, phone)) {
            n = Integer.valueOf(new String(redisClient.hget(todaySendNumberKey, phone))) + 1;
        }
        redisClient.hset(todaySendNumberKey, phone, String.valueOf(n));
        return n;
    }

    /**
     * 判单是否为黑名单
     */

    public boolean isBlacklistPhone(String phone) {
        return redisClient.hexists(Define.KEY_CACHE_BLACKLIST, phone);
    }


    /**
     * 判断黑名单
     *
     * @return
     */
    public boolean existsBlacklist() {
        return false;//redisClient.hlen(Define.KEY_CACHE_BLACKLIST) > 0;
    }

    /**
     * 缓存提交信息
     *
     * @param smsSendResult
     */
    public void cacheSubmitSendResult(SmsSendResult smsSendResult) {

    }


    /**
     * 存放任务号码
     */
    public void putSmsTaskMessage(Long taskId, SmsMessage smsMessage) {
        redisClient.rpush(getSmsMessageQueueKey(taskId), smsMessage);//KEY_SMS_TASK.getBytes());
    }

    /**
     * 存放任务手机号码队列项
     *
     * @param key
     * @param smsMessage
     */
    public void putSmsTaskMessage(byte[] key, SmsMessage smsMessage) {
        //放入发送队列
        redisClient.rpush(new String(key), smsMessage);//KEY_SMS_TASK.getBytes());
        //放入数据库队列
        putSmsTaskMessageDB(smsMessage);
    }

    public void putSmsTaskMessageList(byte[] key, List<SmsMessage> smsMessageList) {

        RedisClient rc = RedisManager.I.newRedisClient();
        rc.openResource();
        String cacheKey = new String(key);
        logger.info("putSmsTaskMessageList-start");
        StringBuilder b = new StringBuilder();
        int i = 1;
        for (SmsMessage smsMessage : smsMessageList) {

            b.append("-").append(smsMessage.getTaskId()).append(",")
                    .append(smsMessage.getMobile()).append(",")
                    .append(smsMessage.getProvince()).append(",")
                    .append(smsMessage.getRegionId()).append(",")
                    .append(smsMessage.getSupplier()).append(",")
                    .append(smsMessage.getNum());

            if (i++ % 10000 == 0 && b.length() > 0) {

                String messageStr = b.substring(1).toString();

                //System.out.println( "["+messageStr+"]");

                //放入发送队列
                rc.rpushString(cacheKey, messageStr);//KEY_SMS_TASK.getBytes());
                rc.rpushString(Define.KEY_SMS_TASK_MESSAGE_DB2, messageStr);//KEY_SMS_TASK.getBytes());
                b = new StringBuilder();
            }
        }

        if (b.length() > 0) {

            String messageStr = b.substring(1).toString();
            //放入发送队列
            rc.rpushString(cacheKey, messageStr);//KEY_SMS_TASK.getBytes());
            rc.rpushString(Define.KEY_SMS_TASK_MESSAGE_DB2, messageStr);//KEY_SMS_TASK.getBytes());
        }
        rc.closeResource();


        //System.out.println( KEY_SMS_TASK_MESSAGE_DB2 + " - " + jedisCluster.llen( KEY_SMS_TASK_MESSAGE_DB2.getBytes()));
        logger.info("putSmsTaskMessageList-end");
        taskMap.get("SaveSendTaskPhone").signalNotEmpty();
    }

    /**
     * 存放任务手机号码队列到数据库保存队列
     *
     * @param smsMessage
     */
    public void putSmsTaskMessageDB(SmsMessage smsMessage) {
        //System.out.println( KEY_SMS_TASK_MESSAGE_DB2 + ":"+smsMessage.getMobile() );
        //SmsWarehouse.getInstance().getJedisCluster().lpush( KEY_SMS_TASK_MESSAGE_DB.getBytes(),messageByte );
        redisClient.rpush(Define.KEY_SMS_TASK_MESSAGE_DB2, smsMessage);//KEY_SMS_TASK.getBytes());
        //System.out.println( KEY_SMS_TASK_MESSAGE_DB2 + " - " + jedisCluster.llen( KEY_SMS_TASK_MESSAGE_DB2.getBytes()));
        taskMap.get("SaveSendTaskPhone").signalNotEmpty();
    }

    /**
     * 存放待审核的任务
     */
    public void putSmsTaskPending(SmsTaskData smsTaskData) {
        //jedisCluster.hset(KEY_SMS_TASK_PENDING.getBytes(),smsTaskData.getTaskId().toString().getBytes(),SerializeUtil.serialize(smsTaskData));
        redisClient.rpush(Define.KEY_SMS_TASK_PENDING_IDS, smsTaskData.getTaskId().toString());
        //通道保存任务线程
        taskMap.get("SaveSendTaskPending").signalNotEmpty();

        //大于0定时发送
        if (smsTaskData.getTaskType() == 1) {

            //定时任务如果免审的话， 加到任务中，否则不加
            //if( smsTaskData.isFree()  ) {
            QuartzJobManager.I.addTimeTask(smsTaskData.getTiming(), smsTaskData.getTaskId());
            //}
        } else {
            //不需要审核的任务直接发送
            if (smsTaskData.isFree()) {
                putSmsTask(smsTaskData);
            }
        }
    }


    /**
     * 重发任务
     */
    public void putSmsTaskResend(SmsTaskData smsTaskData) {
        //jedisCluster.hset(KEY_SMS_TASK_PENDING.getBytes(),smsTaskData.getTaskId().toString().getBytes(),SerializeUtil.serialize(smsTaskData));
        redisClient.rpush(Define.KEY_SMS_TASK_RESEND, smsTaskData.getTaskId().toString());
        //通道保存任务线程
        taskMap.get("TaskResend").signalNotEmpty();
    }


    /**
     * 存放发送任务
     */
    public void putSmsTask(SmsTaskData smsTaskData) {
        redisClient.rpush(Define.KEY_SMS_TASK, smsTaskData);//KEY_SMS_TASK.getBytes());

        //通知发送任务线程
        taskMap.get("SmsSendTask").signalNotEmpty();
    }

    /**
     * 定时任务发送
     *
     * @param taskId
     */
    public void sendSmsTask(String taskId) {
        System.out.println("----------------------------------执行定时任务");
        SmsTaskData smsTaskData = (SmsTaskData) redisClient.hgetObject(Define.KEY_SMS_TASK_CACHE, taskId);

        // 定时任务发送时，发送时间被修改延后，则不进行任何操作--考虑到程序运行时间差，以及相关定时时间延后提前等，将时间差距调整1分钟
        if ((smsTaskData.isFree() || smsTaskData.isAuditState())) {
            //通知发送任务线程
            putSmsTask(smsTaskData);
        }
        // 定时任务需要发送时状态是停止状态，则进行退款等操作--考虑到程序运行时间差，以及相关定时时间延后提前等，将时间差距调整1分钟
        if (2 == smsTaskData.getTaskType()) { //没有进行审核的需要做审核失败处理，并且返还短信条数
            //给用户返款
            SmsServerManager.I.addUserBalance(smsTaskData.getUserId(), smsTaskData.getBilling_num(), "任务ID：" + smsTaskData.getTaskId());
            //修改数据库任务状态为终止发送
            String sql = "update sms_send_task set state=9 where id=" + taskId;
            MysqlOperator.I.execute(sql);
            //存放进缓存
            //redisClient.hsetObject( Define.KEY_SMS_TASK_CACHE , taskId, smsTaskData);
            //放入发送队列
            putSmsTask(smsTaskData);


        }

    }

    /**
     * 审核通过并添到任务到发送队列
     */
    public synchronized void putSmsTaskToSendQueue(byte[] taskIdKey, boolean passed, SmsTaskData smsTaskDataParam) {
        SmsTaskData smsTaskData = (SmsTaskData) redisClient.hgetObject(Define.KEY_SMS_TASK_CACHE, new String(taskIdKey));

        logger.info("putSmsTaskToSendQueue id=" + taskIdKey);
        if (smsTaskData != null && smsTaskData.isPushSendQueue() == false) {

            //避免再次审核通过
            smsTaskData.setPushSendQueue(true);
            smsTaskData.setAuditState(passed);

            //审核时，修改发送的内容
            if (smsTaskDataParam != null) {
                smsTaskData.setText(smsTaskDataParam.getText());
                smsTaskData.setAisleGroupId(smsTaskDataParam.getAisleGroupId());
            }


            //大于0定时发送，定时任务，时间到了，在发
            if (smsTaskData.getTaskType() == 1 && passed) {
                //存放进缓存
                redisClient.hsetObject(Define.KEY_SMS_TASK_CACHE, new String(taskIdKey), smsTaskData);

            } else {
                smsTaskData.setTaskSendTime(new Date());//审核通过， 加上发送时间

                //存放进缓存
                redisClient.hsetObject(Define.KEY_SMS_TASK_CACHE, new String(taskIdKey), smsTaskData);

                //放入发送队列
                putSmsTask(smsTaskData);
            }
        } else {
            logger.error("任务处理过， 当前再次审核通过， 异常任务ID=" + smsTaskData.getTaskId());
        }
    }


    /**
     * 删除任务缓存
     *
     * @param smsTaskData
     */
    public void delSmsTaskCache(SmsTaskData smsTaskData) {
        //删除任务缓存
        redisClient.hdelObject(Define.KEY_SMS_TASK_CACHE, smsTaskData.getTaskId().toString());
        //删除发送记录缓存
        String taskId = smsTaskData.getTaskId().toString();
        Iterator<String> iter = redisClient.hkeys(Define.KEY_SMS_AISLE_SEND_RESULT).iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            if (key != null) {
                String value = redisClient.hget(Define.KEY_SMS_AISLE_SEND_RESULT, key);
                if (value != null) {
                    if (value.equals(taskId)) {
                        redisClient.hdel(Define.KEY_SMS_AISLE_SEND_RESULT, key);
                    }
                }
            }
        }

    }

    /**
     * 获取任务ID
     *
     * @return
     */
    public Long getTaskId() {
        return redisClient.incr(Define.KEY_SMS_TASK_ID);
    }


    /**
     * 添加通道对象
     */
    public void addAisle(BaseAisle aisle) {
        aisleMap.put(aisle.getSmsAisle().getId(), aisle);
    }

    /**
     * 根据通道ID获取通道对象
     *
     * @param aisleId
     * @return
     */
    public BaseAisle getAisle(Long aisleId) {
        return aisleMap.get(aisleId);
    }


    /**
     * 添加通道适配器到缓存
     *
     * @param className
     * @param aisle
     */
    public void addBaseAisleAdapter(String className, BaseAisleAdapter aisle) {
        //logger.info("addBaseAisleAdapter className="+className);
        aisleAdapterMap.put(className, aisle);
    }

    /**
     * 获取通道适配器
     *
     * @param className
     * @return
     */
    public BaseAisleAdapter getBaseAisleAdapter(String className) {
        return aisleAdapterMap.get(className);
    }

    /**
     * 判断通道适配器是否存在
     *
     * @param className
     * @return
     */
    public boolean hasBaseAisleAdapter(String className) {
        return aisleAdapterMap.containsKey(className);
    }


    /**
     * 新创建一个通道
     *
     * @param className
     * @param smsAisle
     * @param messageQueueKey
     * @return
     */
    public BaseAisle newBaseAisle(String className, SmsAisle smsAisle, String messageQueueKey) {

        //获取通道适配器
        BaseAisleAdapter aisleAdapter = getBaseAisleAdapter(className);
        //logger.info(" className="+className+" aisleAdapter="+aisleAdapter);
        BaseAisle baseAisle = new BaseAisle();
        baseAisle.initAdapter(className, aisleAdapter.clone());
        baseAisle.setSmsAisle(smsAisle);
        baseAisle.setAisleId(smsAisle.getId());
        baseAisle.baseAisleAdapter.setAisleId(smsAisle.getId());

        //设置单个包的数量
        if (smsAisle.getSingleNum() > 1) {
            baseAisle.singlePackageNumber = smsAisle.getSingleNum();
        }
        JSONObject jsonObject = StringUtil.stringToJSONObject(smsAisle.getParamValue());
        //logger.info( jsonObject.toString() );
        baseAisle.baseAisleAdapter.setParams(StringUtil.stringToJSONObject(smsAisle.getParamValue()));
        baseAisle.setMessageQueueKey(messageQueueKey);
        return baseAisle;
    }

    /**
     * 判断是否有通道对象
     *
     * @param className
     * @return
     */
    public boolean containsBean(String className) {
        return false;
    }

    /**
     * 获取AISLE BEAN对象
     *
     * @param className
     * @return
     */
    public BaseAisle getBean(String className) {
        return new BaseAisle();
    }

    /**
     * 缓存任务
     *
     * @param smsTaskData
     */
    public synchronized void cacheSmsTaskDate(SmsTaskData smsTaskData) {
        //存放进缓存
        redisClient.hsetObject(Define.KEY_SMS_TASK_CACHE, String.valueOf(smsTaskData.getTaskId()), smsTaskData);
        //taskMap.get("SaveSendTaskPending").signalNotEmpty();
    }

    /**
     * 从缓存中获取号码记录
     *
     * @param mid
     * @return
     */
    public SmsSendResult getSubmitSendResult(String mid) {
        return (SmsSendResult) redisClient.hgetObject(Define.KEY_SMS_AISLE_SEND_RESULT, mid);
    }


    /**
     * 更新缓存中的号码记录
     *
     * @param sequenceId
     * @param msgId
     * @param state
     */
    public void updateSubmitSendResult(String sequenceId, String msgId, int state) {
        SmsSendResult smsSendResult = (SmsSendResult) redisClient.hgetObject(Define.KEY_SMS_AISLE_SEND_RESULT_CMPP_CACHE, sequenceId);
        smsSendResult.setMid(msgId);
        smsSendResult.setState(state);
        saveSendResult(smsSendResult);
        redisClient.hdelObject(Define.KEY_SMS_AISLE_SEND_RESULT_CMPP_CACHE, sequenceId);
    }

    /**
     * 批量保存号码记录
     *
     * @param smsSendResultList
     */
    public void saveSendResult(List<SmsSendResult> smsSendResultList) {

        String reprotDateTimeStr = DateUtil.formatDateTime();

        StringBuilder b = new StringBuilder();
        StringBuilder c = new StringBuilder();
        Map<String, String> midMap = new HashMap<String, String>();
        //aid,receive_phone, mid, sms_send_task_id, create_time, send_time, state)
        for (SmsSendResult smsSendResult1 : smsSendResultList) {
            midMap.put(smsSendResult1.getMid(), smsSendResult1.getTaskId());
            b.append("☆").append(smsSendResult1.getMid()).append(",").append(smsSendResult1.getMobile()).append(",").append(smsSendResult1.getAisleId()).append(",").append(smsSendResult1.getTaskId()).append(",").append(smsSendResult1.getState());
            if (smsSendResult1.getState().intValue() != Define.STATE_SENDING) {
                c.append("☆").append(smsSendResult1.getMid()).append(",").append(smsSendResult1.getMobile()).append(",").append("ERROR").append(",").append(smsSendResult1.getState()).append(",").append(reprotDateTimeStr);
            }
        }

        //2.加到保存数据库队列
        redisClient.rpushString(Define.KEY_SMS_AISLE_SEND_RESULT_DB, b.substring(1).toString());
        redisClient.hsetMap(Define.KEY_SMS_AISLE_SEND_RESULT, midMap);
        taskMap.get("SaveSendLog").signalNotEmpty();
        if (c.length() > 0) {
            redisClient.rpushString(Define.KEY_SMS_AISLE_REPORT, c.substring(1).toString());
        }
        taskMap.get("SaveReportLog").signalNotEmpty();
    }

    /**
     * 保存号码记录
     *
     * @param smsSendResult
     */
    public void saveSendResult(SmsSendResult smsSendResult) {

        //logger.info(" aisle class Name ="+ this.getClass().getName());
        //1.先缓存
        redisClient.hsetObject(Define.KEY_SMS_AISLE_SEND_RESULT, smsSendResult.getMid(), smsSendResult);

        //2.加到保存数据库队列
        redisClient.rpush(Define.KEY_SMS_AISLE_SEND_RESULT_DB, smsSendResult);

        //3 失败状态，加到状态报告发送结果中
        //不包括已提交和网络错误
        //if( smsSendResult.getState().intValue() != STATE_SENDING && smsSendResult.getState().intValue() != STATE_SENDED_NETWORK_ERROR ) {

        if (smsSendResult.getState().intValue() != Define.STATE_SENDING) {
            SmsReport smsReport = new SmsReport(smsSendResult.getMid());
            smsReport.setMobile(smsSendResult.getMobile());
            smsReport.setState("ERROR");
            smsReport.setStateCode(smsSendResult.getState());
            saveReport(smsReport);
        }
        taskMap.get("SaveSendLog").signalNotEmpty();
    }


    /**
     * 保存回执
     *
     * @param smsReportList
     */
    public void saveReport(List<SmsReport> smsReportList) {
        StringBuilder c = new StringBuilder();
        for (SmsReport smsReport1 : smsReportList) {
            c.append("☆").append(smsReport1.getMid()).append(",").append(smsReport1.getMobile()).append(",").append(smsReport1.getState()).append(",").append(smsReport1.getStateCode()).append(",").append(smsReport1.getReceiveDate());
        }
//        System.out.println("===========================================================>>>saveReport smid:" + c.substring(1).toString());
        redisClient.rpushString(Define.KEY_SMS_AISLE_REPORT, c.substring(1).toString());
        taskMap.get("SaveReportLog").signalNotEmpty();
    }


    public void saveReport(SmsReport smsReport) {
        redisClient.rpush(Define.KEY_SMS_AISLE_REPORT, smsReport);
        taskMap.get("SaveReportLog").signalNotEmpty();
    }

    public void saveReply(String aisleClassName, List<SmsReply> smsReplyList) {
        StringBuilder c = new StringBuilder();
        for (SmsReply smsReply : smsReplyList) {
            c.append("☆").append(smsReply.getAisleClassName()).append("★").append(smsReply.getMobile()).append("★").append(smsReply.getContent()).append("★").append(smsReply.getAisleId()).append("★").append(smsReply.getAisleCode());
        }
        redisClient.rpushString(Define.KEY_SMS_AISLE_REPLY, c.toString().substring(1));
        taskMap.get("SaveReplyLog").signalNotEmpty();
    }


    //保存回复内容信息
    public void saveReply(String aisleClassName, SmsReply smsReply) {


        StringBuilder c = new StringBuilder();
        c.append("☆").append(smsReply.getAisleClassName()).append("★").append(smsReply.getMobile()).append("★").append(smsReply.getContent()).append("★").append(smsReply.getAisleId()).append("★").append(smsReply.getAisleCode());
        redisClient.rpushString(Define.KEY_SMS_AISLE_REPLY, c.toString().substring(1));
        taskMap.get("SaveReplyLog").signalNotEmpty();


    }

    //保存发送保日志
    public void saveSendPackage(SmsSendPackage smsSendPackage) {
        redisClient.rpush(Define.KEY_SMS_AISLE_SEND_PACKAGE_DB, smsSendPackage);

        taskMap.get("SaveSendPackageLog").signalNotEmpty();
    }

    //////////////////////////////////////////////////////////////////////////////////////


    /**
     * 根据通道ID， 获取相关的发送记录信息
     */
    public List<SmsSendLog> getSmsSendLogs(String[] aids, String phone) {
        List<SmsSendLog> smsSendLogList = new ArrayList<SmsSendLog>();
        try {
            String sql = " SELECT *  FROM sms_send_log  WHERE aid in (" + StringUtil.arrayToString(aids) + ") and receive_phone='" + phone + "'  and create_time>'" + DateUtil.formatDateTime(new Date(new Date().getTime() - 72 * 3600 * 1000)) + "' ";
            logger.info(" getSmsSendLogs:" + sql);
            smsSendLogList = MysqlOperator.I.query(sql, new RowMapper() {
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    SmsSendLog smsSendLog = new SmsSendLog();
                    smsSendLog.setTaskId(rs.getLong("sms_send_task_id"));
                    System.out.println("getSmsSendLogs sms_send_task_id=" + rs.getLong("sms_send_task_id"));
                    return smsSendLog;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return smsSendLogList;
    }

    /**
     * 获取发送任务
     */
    public SmsTaskData getSmsSendTask(Long id) {
        SmsTaskData smsTaskData = null;
        try {
            String sql = " SELECT *  FROM sms_send_task  WHERE id=" + id;
            logger.info(" getSmsSendTask:" + sql);
            List<SmsTaskData> smsTaskDataList = MysqlOperator.I.query(sql, new RowMapper() {
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    SmsTaskData smsTaskDataTmp = new SmsTaskData(0l, 0, 0l);
                    smsTaskDataTmp.setText(rs.getString("content"));
                    smsTaskDataTmp.setTaskType(rs.getInt("send_type"));
                    smsTaskDataTmp.setAisleGroupId(rs.getLong("sms_aisle_group_id"));
                    System.out.println("getSmsSendTask sms_send_task_id=" + rs.getLong("id"));
                    return smsTaskDataTmp;
                }
            });

            if (smsTaskDataList.size() > 0) {
                smsTaskData = smsTaskDataList.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return smsTaskData;
    }
    //////////////////////////////---------------加载到缓存------------------///////////////////////////


    /**
     * 同步用户基本信息
     */
    public void synchronizeUsers() {
        synchronizeUsers("all");
    }

    public void synchronizeUsers(String k) {
        String sql = " SELECT * FROM sms_user  ";
        if (!k.equals("all")) {
            sql += " where `key`='" + k + "' ";
        }
        synchronizeUsersExecutor(sql);
    }

    public void synchronizeUsers(int id) {
        String sql = " SELECT * FROM sms_user  ";
        if (id > 0) {
            sql += " where `id`=" + id + " ";
        }
        synchronizeUsersExecutor(sql);
    }

    public void synchronizeUsersExecutor(String sql) {
        try {
            List users = MysqlOperator.I.query(sql, new RowMapper() {
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    SmsUser userBean = new SmsUser();
                    userBean.setId(rs.getLong("id"));
                    userBean.setSysId(rs.getLong("sys_user_id"));
                    userBean.setName(rs.getString("name"));
                    userBean.setAccount(rs.getString("email"));
                    userBean.setPhone(rs.getString("phone"));
                    userBean.setBalance(0.2);
                    userBean.setKey(rs.getString("key"));
                    userBean.setState(rs.getInt("state"));
                    userBean.setSignature(rs.getString("signature"));
                    userBean.setReportUrl(rs.getString("report_url"));
                    userBean.setReplyUrl(rs.getString("reply_url"));
                    String checkValue = rs.getString("signature_check");
                    if (checkValue != null && checkValue.equals("1")) {
                        userBean.setSignatureCheck(false);
                    }

                    userBean.setCmppProtocol(rs.getInt("cmpp_protocol") == 1);
                    userBean.setHttpProtocol(rs.getInt("http_protocol") == 1);

                    userBean.setJoinupCoding(rs.getString("joinup_coding"));
                    userBean.setFirmName(rs.getString("firm_name"));
                    userBean.setFirmPwd(rs.getString("firm_pwd"));
                    userBean.setJoinuoMax(rs.getInt("joinuo_max"));

                    userBean.setDefaultGid(rs.getLong("default_agid"));
                    userBean.setAisleGroupId(rs.getInt("aisle_group_id"));
                    System.out.println(" username=" + rs.getString("name")
                            + " key=" + rs.getString("key")
                            + " reportUrl=" + rs.getString("report_url")
                            + " replyUrl=" + rs.getString("reply_url")
                            + " cmpp_protocol=" + rs.getString("cmpp_protocol")
                    );
                    return userBean;
                }
            });

            for (Object user : users) {
                SmsUser smsUser = ((SmsUser) user);
                String key = smsUser.getKey();
                if (key != null) {
                    redisClient.hsetObject(Define.KEY_CACHE_SMS_USER, key, user);
                    redisClient.hset(Define.KEY_CACHE_SMS_USER_ID_KEY, smsUser.getId().toString(), key);
                    redisClient.hset(Define.KEY_CACHE_SYS_USER_ID_KEY, smsUser.getSysId().toString(), key);
                }

                if (smsUser.isCmppProtocol()) {
                    //openCmpp( smsUser );
                }
            }
            logger.info("synchronizeUsers sucess ! count=" + users.size());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 同步用户帐户余额信息
     */
    public void synchronizeUserBlank() {
        synchronizeUserBlank(0L);
    }

    public void synchronizeUserBlank(Long id) {
        try {
            String sql = " SELECT * FROM sms_user_blank ";
            if (id > 0) {
                sql += " where user_id=" + id;
            }
            List<SmsUserBlank> userBlanks = MysqlOperator.I.query(sql, new RowMapper() {
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    SmsUserBlank userBlank = new SmsUserBlank();
                    userBlank.setId(rs.getLong("id"));
                    userBlank.setUserId(rs.getLong("user_id"));
                    userBlank.setBalance(rs.getDouble("money"));
                    userBlank.setSurplus_num(rs.getInt("surplus_num"));
                    return userBlank;
                }
            });
            for (SmsUserBlank userBlank : userBlanks) {
                logger.info("uid=" + userBlank.getId() + ", surplus_num=" + userBlank.getSurplus_num());
                redisClient.hsetObject(Define.KEY_CACHE_SMS_USER_BLANK, (userBlank).getUserId().toString(), userBlank);
            }
            logger.info("synchronizeUserBlank sucess ! count=" + userBlanks.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 运营商信息加载
     */
    public void synchronizeOperators() {
        synchronizeOperatorsB(false);
    }//

    public void synchronizeOperatorsB(boolean isFouce) {
        try {

            String key = KEY_CACHE_PHONE_INFO;
            if (!isFouce && redisClient.hlen(key) > 10000) {
                System.out.println("缓存中已存在" + redisClient.hlen(key) + "条, 不加载.");
                return;
            }

            System.out.println("开始加载..");
            String sql = " SELECT prefix,province,iid,supplier FROM sms_phone_card_info ";
            List<PhoneInfo> phoneInfos = MysqlOperator.I.query(sql, new RowMapper() {
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    PhoneInfo phoneInfo = new PhoneInfo();
                    phoneInfo.setPrefix(rs.getString("prefix"));
                    phoneInfo.setProvince(rs.getString("province"));
                    phoneInfo.setRegionId(rs.getLong("iid"));
                    phoneInfo.setSupplier(rs.getInt("supplier"));
                    return phoneInfo;
                }
            });

            System.out.println("从数据库加载完成 ，共" + phoneInfos.size() + "条");
            Map<byte[], byte[]> map = new HashMap<byte[], byte[]>();

            for (PhoneInfo phoneInfo : phoneInfos) {
                map.put(phoneInfo.getPrefix().toString().getBytes(), ObjectUtils.serialize(phoneInfo));
            }
            redisClient.hmset(KEY_CACHE_PHONE_INFO, map);

            logger.info("synchronizeOperators sucess ! count=" + phoneInfos.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void synchronizeOperatorsC(boolean isFouce) {
        try {
            // 创建缓存文件存放目录
            createFile();

            // isFouce强制重新加载
            File phoneinfo = new File(cacheFiile + "//phoneinfo.text");
            if (phoneinfo.exists() && phoneinfo.length() > 1024) {
                System.out.println("缓存中已存在, 不加载.");
                return;
            }

            System.out.println("开始加载..");
            String sql = " SELECT prefix,province,iid,supplier FROM sms_phone_card_info ";
            List<PhoneInfo> phoneInfos = MysqlOperator.I.query(sql, new RowMapper() {
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    PhoneInfo phoneInfo = new PhoneInfo();
                    phoneInfo.setPrefix(rs.getString("prefix"));
                    phoneInfo.setProvince(rs.getString("province"));
                    phoneInfo.setRegionId(rs.getLong("iid"));
                    phoneInfo.setSupplier(rs.getInt("supplier"));
                    return phoneInfo;
                }
            });

            System.out.println("从数据库加载完成 ，共" + phoneInfos.size() + "条");
            
            /*Map<String, PhoneInfo> map = new HashMap<String, PhoneInfo>();

            for (PhoneInfo phoneInfo : phoneInfos) {
                map.put(phoneInfo.getPrefix(), phoneInfo);
            }
            redisClient.setObject(KEY_CACHE_PHONE_INFO , map);*/

            BufferedWriter bwo = new BufferedWriter(new FileWriter(cacheFiile + "//phoneinfo.text"));

            StringBuffer strbWrite = new StringBuffer();
            for (PhoneInfo phoneInfo : phoneInfos) {
                strbWrite.append(phoneInfo.getPrefix());
                strbWrite.append("," + phoneInfo.getProvince());
                strbWrite.append("," + phoneInfo.getRegionId());
                strbWrite.append("," + phoneInfo.getSupplier());
                strbWrite.append("\r\n");
            }
            bwo.write(strbWrite.toString());
            bwo.flush();
            bwo.close();

            logger.info("synchronizeOperators sucess ! count=" + phoneInfos.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createFile() {
        String userDir = System.getProperty("user.dir");
        int separatorLastPos = userDir.lastIndexOf(File.separator);
        cacheFiile = userDir.substring(0, separatorLastPos) + File.separator + "cacheFiile";
        System.out.println("cacheFiile path=" + cacheFiile);

        java.io.File f = new File(cacheFiile);
        if (!f.exists()) {
            if (f.mkdirs()) {
                //创建目录
                System.out.println("------- plugins create success !--------");
            } else {
                System.out.println("*************** plugins create fail !*********");
            }
        } else {
            System.out.println(" pluginDir is exists!");
        }
    }

    /**
     * @throws
     * @Title: loadOperatorsToMap
     * @Description: 使用领域Map封装运营商信息，提高性能
     * @author: hz-liang
     * @return: void
     */
    public void loadOperatorsToMap() {
        /*String key = Define.KEY_CACHE_PHONE_INFO_MAP;
        if (CollectionUtils.isEmpty(phoneInfoMap)) {
            phoneInfoMap = (Map<String,PhoneInfo>) redisClient.getObject(key);
        }*/

        BufferedReader bfr = null;
        String line = null;
        try {
            bfr = new BufferedReader(new FileReader(cacheFiile + "//phoneinfo.text"));

            while ((line = bfr.readLine()) != null) {
                phoneInfoMap2.put(line.substring(0, 7), line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bfr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 缓存通道组
     */
    public void synchronizeAisleGroup() {
        synchronizeAisleGroup(0L);
    }

    public void synchronizeAisleGroup(Long id) {
        try {
            String sql = " SELECT * FROM sms_aisle_group ";
            if (id > 0) {
                sql += " where id=" + id;
            }
            List<SmsAisleGroup> smsAisleGroup = MysqlOperator.I.query(sql, new RowMapper() {
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    SmsAisleGroup smsAisleGroup1 = new SmsAisleGroup();
                    smsAisleGroup1.setId(rs.getLong("id"));
                    smsAisleGroup1.setTid(rs.getInt("tid"));
                    smsAisleGroup1.setSort(rs.getInt("sort"));

                    smsAisleGroup1.setMobilePrice(rs.getDouble("m_price"));
                    smsAisleGroup1.setUnicomPrice(rs.getDouble("u_price"));
                    smsAisleGroup1.setTelecPrice(rs.getDouble("t_price"));
                    return smsAisleGroup1;
                }
            });

            for (SmsAisleGroup smsAisleGroup1 : smsAisleGroup) {
                logger.info("---------smsAisleGroup1 id=" + smsAisleGroup1.getId());
                redisClient.hsetObject(Define.KEY_CACHE_AISLE_GROUP, smsAisleGroup1.getId().toString(), smsAisleGroup1);
            }

            logger.info("synchronizeAisleGroup sucess ! count=" + smsAisleGroup.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 缓存通道
     */
    public void synchronizeAisle() {
        synchronizeAisle(0L);
    }

    public void synchronizeAisle(Long id) {
        try {
            String sql = " SELECT * FROM sms_aisle where state=1  ";
            if (id > 0) {
                sql += " and id=" + id;
            }


            List<SmsAisle> smsAisles = MysqlOperator.I.query(sql, new RowMapper() {
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    SmsAisle aisle = new SmsAisle();
                    aisle.setId(rs.getLong("id"));
                    aisle.setType(rs.getInt("sms_aisle_type_id"));
                    aisle.setRegionId(rs.getInt("sms_region_id"));
                    aisle.setName(rs.getString("name"));
                    aisle.setClassName(rs.getString("class_name"));
                    aisle.setPrice(rs.getDouble("money"));
                    aisle.setState(rs.getInt("state"));
                    aisle.setParamValue(rs.getString("option_value"));
                    aisle.setSingleNum(rs.getInt("single_num"));
                    aisle.setMaxNum(rs.getInt("max_num"));
                    aisle.setExtra(rs.getString("extra"));
                    if (rs.getInt("mobile_sate") == 1) {
                        aisle.setMobile(true);
                    }
                    if (rs.getInt("unicom_sate") == 1) {
                        aisle.setUnicom(true);
                    }
                    if (rs.getInt("telecom_state") == 1) {
                        aisle.setIstelecom(true);
                    }
                    return aisle;
                }
            });

            for (SmsAisle aisle : smsAisles) {
                logger.info("aisle id=" + aisle.getId() + " price=" + aisle.getPrice() + " option_value=" + aisle.getParamValue());
                if (this.containsBean(aisle.getClassName())) {
                    //设置通道发送信息
                    BaseAisle baseAisle = (BaseAisle) this.getBean(aisle.getClassName());
                    baseAisle.setSmsAisle(aisle);
                    this.addAisle(baseAisle);
                } else {
                    logger.error(" bean no exsist =" + aisle.getClassName());
                    if (this.hasBaseAisleAdapter(aisle.getClassName())) {
                        BaseAisle baseAisle = this.newBaseAisle(aisle.getClassName(), aisle, null);
                        this.addAisle(baseAisle);
                        logger.info("add baseAisle to SmsWarehouse aisle.getClassName=" + aisle.getClassName());
                    }
                }
                redisClient.hsetObject(Define.KEY_CACHE_AISLE, aisle.getId().toString(), aisle);
            }

            logger.info("synchronizeAisle sucess ! count=" + smsAisles.size());


            Map<Long, String> idMapMd5String = new HashMap<Long, String>();
            Map<String, List<String>> strMapIds = new HashMap<String, List<String>>();

            //由于一个通道可以加多个通道配置， 所以要做个映射
            List<SmsAisle> smsAisles1 = new ArrayList<SmsAisle>();// smsAisles;
            if (id > 0) {
                Map<byte[], byte[]> map = redisClient.hgetAll(Define.KEY_CACHE_AISLE);
                for (Map.Entry<byte[], byte[]> entry : map.entrySet()) {
                    SmsAisle aisle = (SmsAisle) ObjectUtils.unserialize(entry.getValue());
                    if (aisle != null) {
                        smsAisles1.add(aisle);
                    }
                }
            } else {
                smsAisles1 = smsAisles;
            }

            for (SmsAisle aisle : smsAisles1) {
                String no = aisle.getClassName();
                if (aisle.getExtra() != null) {
                    no += aisle.getExtra();
                }
                no = StringUtil.string2MD5(no);
                idMapMd5String.put(aisle.getId(), no);
                if (strMapIds.containsKey(no)) {
                    strMapIds.get(no).add(aisle.getId().toString());
                } else {
                    List<String> idList = new ArrayList<String>();
                    idList.add(aisle.getId().toString());
                    strMapIds.put(no, idList);
                }
            }

            for (Long key : idMapMd5String.keySet()) {
                String aisleIds = StringUtil.arrayToString(strMapIds.get(idMapMd5String.get(key)), ",");
                logger.info("aisle id map:" + key + "=" + strMapIds.get(idMapMd5String.get(key).toString()) + ",aisleIds=" + aisleIds);
                redisClient.hset(Define.KEY_CACHE_AISLE_ID_MAP, key.toString(), aisleIds);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 缓存用户通道组相关联信息
     */
    public void synchronizeAisleGroupUser() {
        synchronizeAisleGroupUser(0L);
    }

    public void synchronizeAisleGroupUser(Long userId) {
        try {
            String sql = " SELECT * FROM sms_aisle_group_has_sms_user ";
            if (userId > 0) {
                sql += " where sms_user_id=" + userId + "  and sms_aisle_group_id is not null ";
            } else {
                sql += " where sms_aisle_group_id is not null ";
            }
            List<SmsAisleGroupUserRelation> aisleGroupUserRelations = MysqlOperator.I.query(sql, new RowMapper() {
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    SmsAisleGroupUserRelation groupUserRelation = new SmsAisleGroupUserRelation();
                    groupUserRelation.setId(rs.getLong("id"));
                    groupUserRelation.setGroupId(rs.getLong("sms_aisle_group_id"));
                    groupUserRelation.setUserId(rs.getLong("sms_user_id"));
                    groupUserRelation.setMobilePrice(rs.getDouble("m_price"));
                    groupUserRelation.setUnicomPrice(rs.getDouble("u_price"));
                    groupUserRelation.setTelecPrice(rs.getDouble("t_price"));
                    return groupUserRelation;
                }
            });


            //Map<Long, List<String>> userRelations = new HashedMap();

            List<String> userRelationKeyList = new ArrayList<String>();
            List<Long> userIds = new ArrayList<Long>();
            for (SmsAisleGroupUserRelation groupUserRelation : aisleGroupUserRelations) {
                //唯一值
                String oneKey = groupUserRelation.getGroupId() + "-" + groupUserRelation.getUserId();
                //目前的用户相关组信息 唯一值加进去
                userRelationKeyList.add(oneKey);
                userIds.add(groupUserRelation.getUserId());
                logger.info("oneKey=" + oneKey + " groupId=" + groupUserRelation.getGroupId() + " , userId=" + groupUserRelation.getUserId());
                //获取通道组的类型
                SmsAisleGroup smsAisleGroup = getSmsAislGroup(groupUserRelation.getGroupId());
                logger.info("oneKey=" + oneKey + " groupId=" + groupUserRelation.getGroupId() + " , userId=" + groupUserRelation.getUserId() + " smsAisleGroup:" + smsAisleGroup);
                groupUserRelation.setSmsType(smsAisleGroup.getTid());


                logger.info(" id=" + groupUserRelation.getId() + " userid=" + groupUserRelation.getUserId() + ", groupid=" + groupUserRelation.getGroupId() + " , mobile="
                        + groupUserRelation.getMobilePrice() + ", unicom=" + groupUserRelation.getUnicomPrice()
                        + ", telec=" + groupUserRelation.getTelecPrice());


                redisClient.hsetObject((Define.KEY_CACHE_AISLE_GROUP_USER + groupUserRelation.getUserId()), oneKey, groupUserRelation);
                redisClient.hsetObject((Define.KEY_CACHE_AISLE_GROUP_USER_BY_ID + groupUserRelation.getUserId()), groupUserRelation.getId().toString(), groupUserRelation);

            }

            for (Long uid : userIds) {
                Map<byte[], byte[]> map = redisClient.hgetAll((Define.KEY_CACHE_AISLE_GROUP_USER + uid));
                for (Map.Entry<byte[], byte[]> entry : map.entrySet()) {
                    String oneKey = new String(entry.getKey());
                    if (!userRelationKeyList.contains(oneKey)) {
                        logger.info("del userid=" + uid + " onekey=" + oneKey);
                        redisClient.hdelObject((Define.KEY_CACHE_AISLE_GROUP_USER + uid), oneKey);
                    }
                }
            }


            logger.info("synchronizeAisleGroupUser sucess ! count=" + aisleGroupUserRelations.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 缓存通道组 和通道 关联信息
     */
    public void synchronizeAisleGroupAisle() {
        synchronizeAisleGroupAisle(0L);
    }

    public void synchronizeAisleGroupAisle(Long groupId) {
        try {
            String sql = " SELECT * FROM sms_aisle_group_has_sms_aisle ";
            if (groupId > 0) {
                sql += " where sms_aisle_group_id=" + groupId;
            }
            List<SmsAisleGroupAisleRelation> aisleGroupAisleRelations = MysqlOperator.I.query(sql, new RowMapper() {
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    SmsAisleGroupAisleRelation groupAisleRelation = new SmsAisleGroupAisleRelation();
                    groupAisleRelation.setId(rs.getLong("id"));
                    groupAisleRelation.setGroupId(rs.getLong("sms_aisle_group_id"));
                    groupAisleRelation.setAisleId(rs.getLong("sms_aisle_id"));
                    groupAisleRelation.setSort(rs.getInt("sorts"));
                    groupAisleRelation.setKeyword(rs.getString("import_name"));
                    groupAisleRelation.setOperatorId(rs.getInt("operator_id"));
                    return groupAisleRelation;
                }
            });


            Map<Long, List<SmsAisleGroupAisleRelation>> listMap = new HashMap<Long, List<SmsAisleGroupAisleRelation>>();
            //把关联信息存入MAP
            for (SmsAisleGroupAisleRelation groupAisleRelation : aisleGroupAisleRelations) {
                System.out.println("id=" + groupAisleRelation.getId() + " getGroupId=" + groupAisleRelation.getGroupId());

                //获取通道信息
                SmsAisle smsAisle = getSmsAisle(groupAisleRelation.getAisleId());
                //判断关联的通道是否可用
                if (smsAisle != null && smsAisle.getState().intValue() == 1) {
                    groupAisleRelation.setSmsAisle(smsAisle);
                    //存在则添加到关联LIST
                    if (listMap.containsKey(groupAisleRelation.getGroupId())) {
                        listMap.get(groupAisleRelation.getGroupId()).add(groupAisleRelation);
                    } else {
                        List<SmsAisleGroupAisleRelation> smsAisleGroupAisleRelationList = new ArrayList<SmsAisleGroupAisleRelation>();
                        smsAisleGroupAisleRelationList.add(groupAisleRelation);
                        listMap.put(groupAisleRelation.getGroupId(), smsAisleGroupAisleRelationList);
                    }
                }
            }

            for (Long key : listMap.keySet()) {
                System.out.println("Group ID Key = " + key);
                //获取组ID， 然后取SORT方式
                SmsAisleGroup smsAisleGroup = getSmsAislGroup(key);
                final int sortType = smsAisleGroup.getSort();
                List<SmsAisleGroupAisleRelation> smsAisleGroupAisleRelationList = listMap.get(key);
                Collections.sort(smsAisleGroupAisleRelationList, new Comparator<SmsAisleGroupAisleRelation>() {
                    /*
                     * int compare(  o1,   o2) 返回一个基本类型的整型，
                     * 返回负数表示：o1 小于o2，
                     * 返回0 表示：o1和o2相等，
                     * 返回正数表示：o1大于o2。
                     */
                    public int compare(SmsAisleGroupAisleRelation o1, SmsAisleGroupAisleRelation o2) {
                        if (sortType == 1) {
                            //进行升序排列
                            if (o1.getSort().intValue() > o2.getSort().intValue()) {
                                return 1;
                            }
                            if (o1.getSort().intValue() == o2.getSort().intValue()) {
                                return 0;
                            }
                            return -1;
                        } else {
                            //进行升序排列
                            if (o1.getSmsAisle().getPrice().doubleValue() > o2.getSmsAisle().getPrice().doubleValue()) {
                                return 1;
                            }
                            if (o1.getSmsAisle().getPrice().doubleValue() == (o2.getSmsAisle().getPrice().doubleValue())) {
                                return 0;
                            }
                            return -1;
                        }
                    }
                });

                //调试，查看通道排序列表
                for (SmsAisleGroupAisleRelation relation : smsAisleGroupAisleRelationList) {
                    logger.info(relation.getId() + " aisle id=" + relation.getSmsAisle().getId() + " relation sort=" + relation.getSort() + ", aisle price=" + relation.getSmsAisle().getPrice() + " sorttype=" + sortType);
                }
                //以通道组ID
                redisClient.hsetObject(Define.KEY_CACHE_AISLE_GROUP_AISLE, key.toString(), smsAisleGroupAisleRelationList);

            }

            //没有通道组关联信息的时候，删除该通道组下面的缓存
            if (aisleGroupAisleRelations.size() == 0) {
                //以通道组ID
                redisClient.hdelObject(Define.KEY_CACHE_AISLE_GROUP_AISLE, groupId.toString());
            }

            logger.info("synchronizeAisleGroupAisle sucess ! count=" + aisleGroupAisleRelations.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 缓存屏蔽词信息
     */
    public void synchronizeShield() {
        try {
            String sql = " SELECT * FROM sms_shield_word ";
            List<ShieldWord> shields = MysqlOperator.I.query(sql, new RowMapper() {
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

                    ShieldWord s = new ShieldWord();
                    s.setWordName(rs.getString("word_name"));
                    s.setLevel(rs.getInt("level"));
                    return s;
                }
            });

            shieldWords = shields;
            //redisClient.set(Define.KEY_CACHE_SHIELD, keywords);
            logger.info("synchronizeShield sucess ! count=" + shieldWords.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 缓存黑名单信息
     */
    public void synchronizeBlacklist() {
        try {
            String sql = " SELECT * FROM sms_blacklist  ";
            List<String> blacklists = MysqlOperator.I.query(sql, new RowMapper() {
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return rs.getString("phone");
                }
            });

            for (String k : blacklists) {
                if (k.length() == 11) {
                    System.out.println("k=" + k);
                    redisClient.hset(Define.KEY_CACHE_BLACKLIST, k, k);
                }
            }
            logger.info("synchronizeBlacklist sucess ! count=" + blacklists.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 缓存白名单信息
     */
    public void synchronizeWhitelist() {
        try {
            String sql = " SELECT * FROM sms_white_list  ";
            List<String> blacklists = MysqlOperator.I.query(sql, new RowMapper() {
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return rs.getString("phone");
                }
            });

            for (String k : blacklists) {
                if (k.length() == 11) {
                    System.out.println("k=" + k);
                    redisClient.set(Define.KEY_CACHE_WHITELIST, k);
                } else {
                    System.out.println("k=" + k);
                    String[] phones = StringUtil.split(k, ",");
                    for (int i = 0; i < phones.length; i++) {
                        if (phones[i].trim().length() == 11) {
                            redisClient.set(Define.KEY_CACHE_WHITELIST, phones[i].trim());
                        }
                    }

                }
            }
            logger.info("synchronizeBlacklist sucess ! count=" + blacklists.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 缓存用户的审核过的模板
     */
    public void synchronizeFreeTrial() {
        synchronizeFreeTrial(0l);
    }

    public void synchronizeFreeTrial(Long uid) {
        try {
            String sql = " SELECT * FROM sms_user_free_trial  where free_trial_state=1";
            if (uid > 0) {
                sql += " and sms_user_id=" + uid;
            }
            List<Map<Long, String>> freeTrialList = MysqlOperator.I.query(sql, new RowMapper() {
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Map<Long, String> freeTrials = new HashMap<Long, String>();
                    freeTrials.put(rs.getLong("sms_user_id"), rs.getString("content"));
                    return freeTrials;
                }
            });
            for (Map<Long, String> item : freeTrialList) {
                for (Long userId : item.keySet()) {
                    String content = item.get(userId);
                    String key = StringUtil.string2MD5(content);
                    redisClient.hset(Define.KEY_CACHE_FREE_TRIAL + userId, key, content);
                    logger.info("user id=" + userId + "  key=" + key + " content=" + content);
                }
            }
            logger.info("synchronizeFreeTrial sucess ! count=" + freeTrialList.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 缓存插件列表
     */
    public void synchronizePlugins() {
        synchronizePlugins(0l);
    }

    public void synchronizePlugins(Long id) {
        try {
            String sql = " SELECT * FROM sms_plugin  where state=1";
            if (id > 0) {
                sql += " and id=" + id;
            }


            List<SmsPlugin> pluginList = MysqlOperator.I.query(sql, new RowMapper() {
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    SmsPlugin plugin = new SmsPlugin();
                    plugin.setId(rs.getLong("id"));
                    plugin.setAisleName(rs.getString("aisle_name"));
                    plugin.setClassName(rs.getString("class_name"));
                    plugin.setName(rs.getString("name"));
                    plugin.setPath(rs.getString("path"));
                    Date updateTime = rs.getDate("update_time");
                    if (updateTime != null) {
                        updateTime = new Date(rs.getTimestamp("update_time").getTime());
                    }

                    //先构建插件的文件位置
                    File plgFile = null;
                    String pluginFile = plugin.getPath();
                    int pos = plugin.getPath().lastIndexOf("/");
                    if (pos != -1) {
                        pluginFile = plugin.getPath().substring(pos + 1);
                    }
                    String pluginFilePath = pluginDir + File.separator + pluginFile;
                    plgFile = new File(pluginFilePath);
                    if (plgFile != null && updateTime != null) {
                        //logger.info("1plgFile.lastModified:" + plgFile.lastModified() + "-updateTime.getTime():" + updateTime.getTime());
                        logger.info("2plgFile.lastModified:" + DateUtil.formatDateTime(new Date(plgFile.lastModified())) + "-updateTime.getTime():" + DateUtil.formatDateTime(new Date(updateTime.getTime())));
                    } else {
                        logger.error("pluginFilePath=" + pluginFilePath + ", updateTime=" + updateTime);
                    }
                    //插件文件不存在， 或大小不对， 或最后修改时间小于更新的时间，则更新
                    if (!plgFile.exists() || plgFile.length() < 10 || updateTime == null || plgFile.lastModified() < updateTime.getTime()) {
                        //logger.info("update plugin="+plgFile);
                        byte[] fileBytes = rs.getBytes("jar");
                        if (fileBytes != null && fileBytes.length > 10) {
                            try {
                                final OutputStream os = new FileOutputStream(plgFile);
                                if (os != null) {
                                    logger.info("read jar from mysql path=" + pluginFilePath);
                                    FileCopyUtils.copy(rs.getBytes("jar"), os);
                                    os.close();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            logger.error("update plugins error ! file=" + plgFile);
                        }
                    }

                    return plugin;
                }
            });
            for (SmsPlugin plugin : pluginList) {
                redisClient.hsetObject(Define.KEY_CACHE_SMS_PLUGIN, plugin.getId().toString(), plugin);
                logger.info("id=" + plugin.getId() + "  path=" + plugin.getPath() + " className=" + plugin.getClassName());
            }

            for (SmsPlugin plugin : pluginList) {
                // pluginPath = getSysConfig
                String pluginFile = plugin.getPath();
                int pos = plugin.getPath().lastIndexOf("/");
                if (pos != -1) {
                    pluginFile = plugin.getPath().substring(pos + 1);
                }
                String pluginFilePath = pluginDir + File.separator + pluginFile;
                File plgFile = new File(pluginFilePath);
                if (!plgFile.exists()) {

                    String downloadUrl = getSysConfig("plugin_path") + plugin.getPath();
                    NetUtil.downloadFile(downloadUrl, pluginFilePath);

                    logger.info("down file=" + pluginFilePath);

                } else {
                    logger.info(pluginFilePath + " exists !");
                }

                JarClassLoader jcl = new JarClassLoader();

                //Loading classes from different sources
                jcl.add(pluginFilePath);
//                jcl.add(new URL("http://myserver.com/myjar.jar"));
//                jcl.add(new FileInputStream("myotherjar.jar"));
//                jcl.add("myclassfolder/");

                //Recursively load all jar files in the folder/sub-folder(s)
                //jcl.add("myjarlib/");

                JclObjectFactory factory = JclObjectFactory.getInstance();
                try {
                    //Create object of loaded class
                    Object obj = factory.create(jcl, "com.dzd.sms.addons.aisle." + plugin.getClassName());
                    BaseAisleAdapter baseAisleAdapter = JclUtils.cast(obj, BaseAisleAdapter.class);
                    //baseAisleAdapter.setAisleClassName(plugin.getClassName());
                    addBaseAisleAdapter(plugin.getClassName(), baseAisleAdapter);

                    logger.info("obj=" + obj + " package=com.dzd.gosms.addons.aisle." + plugin.getClassName());

                    //BaseAisle baseAisle = (BaseAisle) JclUtils.toCastable(obj, BaseAisle.class);
//                    logger.info( "obj=" + obj +" getAisleId="+baseAisleAdapter.getAisleId());
                    BaseAisle baseAisle = new BaseAisle();
                    baseAisle.initAdapter(plugin.getClassName(), baseAisleAdapter);
                    logger.info("obj=" + obj + " getClassNaem=" + baseAisle.aisleClassName);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            logger.info("synchronizePlugins sucess ! count=" + pluginList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void synchronizeSysConfig() {
        try {
            String sql = " SELECT * FROM sys_config";
            List<Map<String, String>> mapList = MysqlOperator.I.query(sql, new RowMapper() {
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Map<String, String> conf = new HashMap<String, String>();
                    conf.put(rs.getString("key"), rs.getString("values"));
                    return conf;
                }
            });
            for (Map<String, String> item : mapList) {
                for (String syskey : item.keySet()) {
                    String sysvalue = item.get(syskey);
                    redisClient.hset(Define.KEY_CACHE_SYS_CONFIG, syskey, sysvalue);
                    logger.info("  syskey=" + syskey + " sysvalue=" + sysvalue);
                }
            }
            logger.info("synchronizeSysConfig sucess ! count=" + mapList.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 同步所有数据
     */
    public void synchronousAllData() {

        //同步任务ID
        sysTaskId();
        synchronizeSysConfig();
        synchronizePlugins();
        synchronizeUsers();
        synchronizeUserBlank();
        //synchronizeOperators();
        synchronizeOperatorsC(false);
        loadOperatorsToMap();

        synchronizeAisle();
        synchronizeAisleGroup();
        synchronizeAisleGroupAisle();
        synchronizeAisleGroupUser();
        synchronizeShield();
        synchronizeBlacklist();
        synchronizeWhitelist();
        synchronizeFreeTrial();


    }

    /**
     * 订阅
     */
    public void sub() {
        logger.info("sub start..");

        new Thread(new Runnable() {
            public void run() {
                try {
                    logger.info("Subscribing to \"commonChannel\". This thread will be blocked.");
                    redisClient.subscribe(subscriber, "ab");
                    logger.info("Subscription ended.");
                } catch (Exception e) {
                    logger.error("Subscribing failed.", e);
                }
            }
        }).start();

        logger.info("sub success..");
    }

    /**
     * 同步任务ID
     */
    public void sysTaskId() {
        //获取mysql表主键值--redis启动时  
        long id = 0L;// MySQL.getID(tbname);
        try {
            if (redisClient.exists(Define.KEY_SMS_TASK_ID)) {
                id = Long.valueOf(redisClient.get(Define.KEY_SMS_TASK_ID));
            }
            //if( id<1 ){
            String sql = "select max(id) as maxid from sms_send_task ";
            Map map = (Map) MysqlOperator.I.queryForMap(sql);
            Object obj = map.get("maxid");
            if (obj instanceof Integer) {
                id = (Integer) map.get("maxid");
            } else if (obj instanceof Long) {
                id = (Long) map.get("maxid");
            }
            //}
            redisClient.set(Define.KEY_SMS_TASK_ID, String.valueOf(id));
            System.out.println("sysTaskId=" + id);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("任务ID同步失败");
        }

    }

    /**
     * 获取处理时间
     */
    public Date getProcessDate(String key) {
        //判断是否存在当前KEY
        if (redisClient.hexists(Define.KEY_CACHE_PROCESS_TIME, key)) {
            Date d = DateUtil.parseDateTime(redisClient.hget(Define.KEY_CACHE_PROCESS_TIME, key));
            return d;
        } else {
            //logger.info("getProcessDate="+DateUtil.formatDateTime( new Date(  new Date().getTime() - 20*12*60*60*1000 ) ) );
            return new Date(new Date().getTime() - 20 * 12 * 60 * 60 * 1000);
        }
    }

    /**
     * 列新处理时间为当前时间
     *
     * @param key
     */
    public void updateProcessDate(String key) {
        redisClient.hset(Define.KEY_CACHE_PROCESS_TIME, key, DateUtil.formatDateTime());
    }

    public void updateProcessDateToInit(String key) {
        redisClient.hset(Define.KEY_CACHE_PROCESS_TIME, key, "2000-01-01 00:00:00");
    }

    /**
     * 判断并更新处理时间
     */

    public boolean isCanHandle(String key, int second) {
        Date d = new Date();
        Date k = getProcessDate(key);
        int difSecond = DateUtil.diffDate(DateUtil.SECOND, k, d);
        //System.out.println(" isCanHandle key="+key+" difSecond="+difSecond+" k="+ DateUtil.formatDateTime(k)+" d="+DateUtil.formatDateTime(d));
        //当前时间比历史处理时间大， 处理
        if (difSecond >= second) {
            updateProcessDate(key);
            return true;
        }
        return false;
    }

    /*public Map<String, PhoneInfo> getPhoneInfos()
    {
		return phoneInfoMap;
	}*/

    public Map<String, String> getPhoneInfoMap2() {
        return phoneInfoMap2;
    }


}
