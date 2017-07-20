import com.dzd.cache.redis.manager.RedisClient;
import com.dzd.cache.redis.manager.RedisManager;
import com.dzd.sms.application.SmsServerManager;
import org.junit.Test;

/**
 * @Author WHL
 * @Date 2017-3-24.
 */
public class SmsServerTest {
    @Test
    public void test(){

    }
    public void test2(){
        SmsServerManager.I.start();
        try{
            Thread.sleep(1000000);
        }catch (Exception e){

        }
    }
}
