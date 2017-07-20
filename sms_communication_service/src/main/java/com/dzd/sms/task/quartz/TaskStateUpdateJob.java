package com.dzd.sms.task.quartz;

import com.dzd.cache.redis.manager.RedisClient;
import com.dzd.cache.redis.manager.RedisManager;
import com.dzd.db.mysql.MysqlOperator;
import com.dzd.sms.application.Define;
import com.dzd.sms.application.SmsServerManager;
import com.dzd.sms.service.data.SmsTaskData;
import com.dzd.sms.task.db.SaveSendTaskPending;
import com.dzd.utils.DateUtil;
import com.dzd.utils.MathUtil;
import com.dzd.utils.SerializeUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.dzd.sms.application.Define.INTERFACE_STATE_BALANCE_NONE;
import static com.dzd.sms.application.Define.KEY_SMS_TASK_CACHE;

/**
 * @Author WHL
 * @Date 2017-4-24.
 * @Desc 更新任务和详细信息的发送状态
 */
public class TaskStateUpdateJob implements Job {


    private  static Logger logger = Logger.getLogger(TaskStateUpdateJob.class );
    //REDIS操作对象
    public RedisClient redisClient = RedisManager.I.getRedisClient();

    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        try{
            update();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void update(){

        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ":更新任务状态");

        //hkeysObject

        //更新指定任务的状态
        long taskId = 29;
        int state = -1;
        String sql = "";

        if( SmsServerManager.I.isCanHandle("taskUpdateTaskPhoneDatabase",5 )) {

            Set<byte[]> taskKeys = redisClient.hkeysObject( KEY_SMS_TASK_CACHE );
            int len = taskKeys.size();
            logger.info("task-update-num="+len);

            long startTaskId = 1000000000000000l;


            Iterator<byte[]> iter = taskKeys.iterator();
            int i = 0;
            while (i++ < len && iter.hasNext()) {
                byte[] key = iter.next();
                //System.out.println("key="+key);
                if (key != null) {
                    Object value = redisClient.hgetObject( KEY_SMS_TASK_CACHE, new String(key));
                    //System.out.println("value="+value);
                    if (value != null) {
                        SmsTaskData smsTaskData = (SmsTaskData) value;
                        if (smsTaskData != null) {
                            startTaskId = Math.min(startTaskId,smsTaskData.getTaskId());

                            //////////////更新任务状态///////////////////
                            try {

//                            logger.info("Scheduled: taskId=" + smsTaskData.getTaskId() + " count=" + smsTaskData.getCount()
//                                    + " savePhoneNumber=" + smsTaskData.getSavePhoneNumber()
//                                    + " saveSendPhoneNumber=" + smsTaskData.getSaveSendPhoneNumber()
//                                    + " saveReportPhoneNumber=" + smsTaskData.getSaveReportPhoneNumber()
//                            + " updateSendLog=" + smsTaskData.isUpdateSendLogDatabase() );

                                Date sendTime = smsTaskData.getTaskSendTime();
                                if( sendTime != null &&   DateUtil.diffDate( DateUtil.SECOND, sendTime, new Date() )>2 ){
                                    if (smsTaskData.isUpdateSendLogDatabase() == false ) {
                                        if( smsTaskData.getCount()==smsTaskData.getSaveSendPhoneNumber() ) {
                                            try {
                                                //更新号码为已提交
                                                sql = "UPDATE sms_send_task_phone p, sms_send_log l " +
                                                    " SET p.state = l.state " +
                                                    " , p.send_time=l.send_time, p.aid=l.aid "+
                                                    " WHERE p.sms_send_task_id="+( smsTaskData.getTaskId() ) +
                                                    " AND p.state=-1 " +
                                                    " AND p.sms_send_task_id=l.sms_send_task_id " +
                                                    " AND p.phone=l.receive_phone ";
                                                logger.info(sql);
                                                MysqlOperator.I.execute(sql);

                                                //更新任务状态-提交完成
                                                sql = " update sms_send_task set state=100, blacklist_phone_num=" + smsTaskData.getBlacklist_phone_num() + " where id=" + smsTaskData.getTaskId() + " and state<100 and state<>9 ";
                                                logger.info(sql);
                                                MysqlOperator.I.execute(sql);


                                                smsTaskData.setUpdateSendLogDatabase(true);
                                                SmsServerManager.I.cacheSmsTaskDate(smsTaskData);
                                            }catch (Exception e){
                                                e.printStackTrace();
                                                logger.error("update.sms_send_task.state=100");
                                            }
                                        }


                                    }

                                    //由于收到状态报告时就更新了日志,但没有添加回报告的数量， 所有这里没有执行
                                    if (   smsTaskData.getSaveReportPhoneNumber()> smsTaskData.getUpdateReportPhoneNumber() && smsTaskData.getSaveReportPhoneNumber() >= smsTaskData.getCount() ) {

                                        sql = "UPDATE sms_send_task_phone p, sms_receive_log l " +
                                                " SET p.state = l.state_code,p.receive_state = l.state,p.receive_code = l.state_code,p.receive_time = l.create_time " +
                                                " WHERE p.sms_send_task_id="+(smsTaskData.getTaskId()) +
                                                " AND p.state=99  " + //只有状态为99 才提交成功
                                                " AND p.sms_send_task_id=l.sms_task_id " +
                                                " AND p.phone=l.phone ";

                                        logger.info(sql);
                                        MysqlOperator.I.execute(sql);

                                        smsTaskData.setUpdateReportPhoneNumber(smsTaskData.getSaveReportPhoneNumber() );
                                        SmsServerManager.I.cacheSmsTaskDate(smsTaskData);
                                    }

                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

//                            ////////////////////更新任务下的号码发送状态//////////////////////////
//                            sql = "UPDATE sms_send_task_phone p, sms_send_log l " +
//                                    " SET p.state = l.state " +
//                                    " , p.send_time=l.send_time, p.aid=l.aid "+
//                                    " WHERE p.sms_send_task_id="+( smsTaskData.getTaskId() ) +
//                                    " AND p.state=-1 " +
//                                    " AND p.sms_send_task_id=l.sms_send_task_id " +
//                                    " AND p.phone=l.receive_phone ";
//                            try{
//                                MysqlOperator.I.execute(sql);
//                            }catch (Exception e){
//                                e.printStackTrace();
//                            }
//
//
//                            ////////////////////更新接收状态//////////////////////////
//                            sql = "UPDATE sms_send_task_phone p, sms_receive_log l " +
//                                    " SET p.state = l.state_code,p.receive_state = l.state,p.receive_code = l.state_code,p.receive_time = l.create_time " +
//                                    " WHERE p.sms_send_task_id="+(smsTaskData.getTaskId()) +
//                                    " AND p.state=99  " + //只有状态为99 才提交成功
//                                    " AND p.sms_send_task_id=l.sms_task_id " +
//                                    " AND p.phone=l.phone ";
//                            try{
//                                logger.info(sql);
//                                MysqlOperator.I.execute(sql);
//                            }catch (Exception e){
//                                e.printStackTrace();
//                            }
//                            ///////////////////////////



                            //72小时
                            ///////////////////////判断是否返还///////////////////////////
                            try {
                                float returnHour = 72f;
                                if (smsTaskData.isUpdateSendLogDatabase()) {
                                    if (smsTaskData.getReturnState() == 0 ) {
                                        Date sendDate = smsTaskData.getTaskSendTime();
                                        //logger.info("task id="+smsTaskData.getTaskId() + ",  send Date="+DateUtil.formatDateTime(sendDate));
                                        //过72小时， 需要返还数据
                                        if ( sendDate !=null ) {
                                            int difMinute = DateUtil.diffDate(DateUtil.MINUTE, sendDate, new Date());
                                            if ( difMinute > returnHour * 60) {
                                                logger.info("return task id="+smsTaskData.getTaskId() + ",  send Date="+DateUtil.formatDateTime(sendDate)+" difMinute="+difMinute);
                                                //先更新返还状态
                                                //smsTaskData.setReturnState(1);
                                                //SmsServerManager.I.cacheSmsTaskDate(smsTaskData);

                                                //删除任务及发送号码缓存
                                                SmsServerManager.I.delSmsTaskCache(smsTaskData);

                                                //审核通过，或免审的任务需要结算
                                                if( smsTaskData.isAuditState() || smsTaskData.isFree() ) {
                                                    //查询是否返还的任务
                                                    Integer count = 0;
                                                    Integer fee = 0;
                                                    //成功的数量
                                                    int successNum = 0;

                                                    int auditFailNum = 0;

                                                    //查询需要返还的数量
                                                    Map<Integer, Integer> re = getTaskSendResult(smsTaskData.getTaskId());
                                                    if (re != null) {
                                                        //返还的数
                                                        int returnNum = 0;

                                                        for(Integer stateKey: re.keySet()){
                                                            if( stateKey.intValue()>=99 ){
                                                                successNum+=re.get(stateKey);
                                                            }else if( stateKey.intValue() !=9 && stateKey.intValue() !=25 ){
                                                                returnNum+= re.get(stateKey);
                                                            }
                                                        }

                                                        if (returnNum > 0) {
                                                            //给用户返款
                                                            SmsServerManager.I.addUserBalance(smsTaskData.getUserId(), returnNum, "任务ID：" + smsTaskData.getTaskId());
                                                        }
                                                        logger.info("返还金额=" + fee + ",失败数量=" + count);
                                                    } else {
                                                        logger.info("失败-返还金额=" + fee + ",失败数量=" + count);
                                                    }



                                                    //更新返还状态， 及返还金额
                                                    updateTaskReturnState(smsTaskData.getTaskId(), successNum, count);
                                                }//////////////
                                            }
                                        }
                                    } else {
                                        //SmsWarehouse.getInstance().delSmsTaskCache(smsTaskData);
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {

                        }
                    }
                }
            }


            //存在任务
            if( len>0){


//                ////////////////////更新任务下的号码发送状态//////////////////////////
//                sql = "UPDATE sms_send_task_phone p, sms_send_log l " +
//                        " SET p.state = l.state " +
//                        " , p.send_time=l.send_time, p.aid=l.aid "+
//                        " WHERE p.sms_send_task_id>"+(startTaskId-1) +
//                        " AND p.state=-1 " +
//                        " AND p.sms_send_task_id=l.sms_send_task_id " +
//                        " AND p.phone=l.receive_phone ";
//                try{
//                    MysqlOperator.I.execute(sql);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//
//
//                ////////////////////更新接收状态//////////////////////////
//                sql = "UPDATE sms_send_task_phone p, sms_receive_log l " +

//                        " SET p.state = l.state_code,p.receive_state = l.state,p.receive_code = l.state_code,p.receive_time = l.create_time " +
//                        " WHERE p.sms_send_task_id>"+(startTaskId-1) +
//                        " AND ( p.state=99 ) " + //只有状态为99 才提交成功
//                        " AND p.sms_send_task_id=l.sms_task_id " +
//                        " AND p.phone=l.phone ";
//                try{
//                    logger.info(sql);

//                    MysqlOperator.I.execute(sql);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                ///////////////////////////

            }

        }




    }



    /**
     * 查询返还的数量和金额
     * 警告： state=25 为审核失败， 即为终止发送
     * state=99为提交成功
     * state=100为发送成功
     * 以上几种状态不返还
     */
    public Map<Integer, Integer> getTaskSendResult(Long taskId) {
         Map<Integer, Integer> re = new HashedMap();
        try {

            String sql = "select state, sum(billing_num) as f from sms_send_task_phone where sms_send_task_id=" + taskId + " group by state ";



            //String sql = "select count(id) as c, sum(billing_num) as f from sms_send_task_phone where sms_send_task_id=" + taskId + " and (state<>25 and state<99) and state<>0 ";
            //String sql = "select count(id) as c, sum(billing_num) as f from sms_send_task_phone where sms_send_task_id=" + taskId + " and  state<99 ";

            logger.info(sql);
            List<Map<Integer, Integer>> lists =MysqlOperator.I.query(sql, new RowMapper() {
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Map<Integer, Integer> b = new HashedMap();
                    Integer state = 0;
                    Integer f = 0;

                    state = rs.getInt("state");


                    if( rs.getObject("f") !=null ) {
                        if (rs.getObject("f") instanceof Long) {
                            f =  ( (Long)rs.getLong("f")).intValue();
                        } else {
                            f = ( (BigDecimal)rs.getBigDecimal("f")).intValue();
                        }
                    }

                    b.put(state, f);
                    return b;
                }
            });

            for( int i=0;i<lists.size();i++){
                for(Integer state: lists.get(i).keySet()){
                    re.put( state, lists.get(i).get(state));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return re;
    }


    /**
     * 更新任务状态
     */
    public void updateTaskReturnState(Long taskId, int fee, int count) {
        String sql = " update sms_send_task set return_state=1, state=101,  actual_num=" + fee + ",update_time = NOW() where id = " + taskId;
        try {
            System.out.println(sql);
            MysqlOperator.I.update(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}