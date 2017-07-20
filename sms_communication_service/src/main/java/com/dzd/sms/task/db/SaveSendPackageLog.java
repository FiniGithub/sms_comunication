package com.dzd.sms.task.db;

import com.dzd.cache.redis.manager.RedisClient;
import com.dzd.cache.redis.manager.RedisManager;

import com.dzd.db.mysql.MysqlOperator;
import com.dzd.sms.application.Define;
import com.dzd.sms.service.data.SmsSendPackage;
import com.dzd.sms.task.base.BaseTask;
import com.dzd.utils.DateUtil;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017-1-17.
 */

public class SaveSendPackageLog  extends BaseTask {

    private static Logger logger = Logger.getLogger(SaveReportLog.class);

    String mkey = Define.KEY_SMS_AISLE_SEND_PACKAGE_DB;
    RedisClient redisClient = RedisManager.I.getRedisClient();
    public SaveSendPackageLog() {
        this.singlePackageNumber = 100;
        this.threadNumber = 1;
    }



    /**
     * 判断是否有任务可以执行
     */
    public boolean existTask() {
        return redisClient.llen(mkey) > 0;
    }

    @Override
    public void singleExecutor() {
        List<SmsSendPackage> smsSendPackages = new ArrayList<SmsSendPackage>();
        try {
            long len = redisClient.llen(mkey);
            if (len > singlePackageNumber) len = singlePackageNumber;
            for (int i = 0; i < len; i++) {
                Object objectValue = redisClient.lpop(mkey);
                if ( objectValue != null) {
                    SmsSendPackage smsSendPackage = (SmsSendPackage) objectValue;
                    smsSendPackages.add(smsSendPackage);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (smsSendPackages.size() > 0) {
                //sms_task_id 任务ID
                //user_id 用户ID
                //send_params 发送参数
                //send_result 发送结果
                //create_date datetime 时间
                //sms_aisle_id 通道ID
                //phone_num 号码数量
                //state 状态，0：发送成功，1：发送失败
                //content 内容
                //describe 描述
                //phone_all 发送的所有号码


                final List<SmsSendPackage> tmpList = smsSendPackages;
                String sql = "insert into sms_send_package_log( sms_task_id, user_id, send_params, send_result , create_date," +
                        " sms_aisle_id,phone_num,state,content,`describe`,phone_all ) values(?, ?, ?, ?, ?,?,?,?,?,?,?  )";
                MysqlOperator.I.batchUpdate(sql, new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, tmpList.get(i).getTaskId());
                        ps.setLong(2, tmpList.get(i).getUserId());
                        ps.setString(3, tmpList.get(i).getParams());
                        ps.setString(4, tmpList.get(i).getResult());
                        ps.setString(5, DateUtil.formatDateTime(tmpList.get(i).getCreateDate()));
                        ps.setLong(6, tmpList.get(i).getAisleId());
                        ps.setInt(7, tmpList.get(i).getPhoneNum());
                        ps.setInt(8, tmpList.get(i).getState());
                        ps.setString(9, tmpList.get(i).getContent());
                        ps.setString(10, tmpList.get(i).getDescribe());
                        ps.setString(11, tmpList.get(i).getPhoneAll());
                    }

                    public int getBatchSize() {
                        return tmpList.size();
                    }
                });

            }
        } catch (Exception e) {
            excep = true;
            e.printStackTrace();

        }

    }
}
