package com.ttsx.FlashKilling.component;

import com.ttsx.constant.Constant;
import com.ttsx.entity.vo.FlashKillingVO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Component("redisComponent")
public class RedisComponent {

    @Resource
    private RedisTemplate redisTemplate;


    public  Boolean IsUserOrder ( Integer userid,String fno ){
        return redisTemplate.opsForSet().isMember(Constant.REDIS_SECKILL_ORDER + fno, userid);
    }

    //秒杀商品库存减1
    public Long SeckillStockSubstrateOne (String hkey, String fno ,int count){
        return redisTemplate.opsForHash().increment(hkey,fno,count);
    }


    //通过当天场次和秒杀id,获取秒杀商品详情，
    public FlashKillingVO GetGoodsDetail(String time, String fno){
        String nowTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE);
        return (FlashKillingVO) redisTemplate.opsForHash().get(nowTime+":"+time,fno);
    }
    // 通过当天场次,获取秒杀商品
    public List<FlashKillingVO> GetGoodsbyTime(Object time){
        String nowTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE);
        List list = this.redisTemplate.opsForHash().values(nowTime + ":" + time);
        return list;
    }
    //更新redis预库存的数量,根据time,fno
    public void UpdateGoodsCount(String hkey,String fno,String count){
         redisTemplate.opsForHash().put(hkey,fno,count);
    }


}
