package com.ttsx.FlashKilling.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ttsx.FlashKilling.Service.SeckillService;
import com.ttsx.FlashKilling.mapper.flashKillingDao;
import com.ttsx.constant.Constant;
import com.ttsx.entity.pojo.FlashKilling;
import com.ttsx.entity.vo.FlashKillingVO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * 基于redlock分布式锁实现,(已废弃)
 */

@Service
@Slf4j
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    private RedisTemplate redisTemplate;
//    @Autowired
//    private Order_fkDao order_fkDao;
    @Autowired
    private flashKillingDao flashKillingDao;
    @Autowired
    private Redisson redisson;

    @Override
    public Boolean doSeckill(Integer fno,Integer userid,Integer time,String nowTime){
        RLock lock = redisson.getLock("myLock");
        lock.lock();
        try
        {
            FlashKillingVO fkvo = (FlashKillingVO) redisTemplate.opsForHash().get(nowTime + ":" + time, String.valueOf(fno));
            Integer count = fkvo.getCount();

            FlashKilling flashKilling = flashKillingDao.selectById(fno);
            Integer flashKillingCount = flashKilling.getCount();
            if (!flashKillingCount .equals(count)){
                count = flashKillingCount;
                log.info("redis与mysql数据不一致,更改redis值");
            }
            //TODO:保持事务的一致性, redis扣减库存，Mysql扣减库存,插入订单表
            //从redis中扣减库存
            int UpdateCount = count - 1;
            fkvo.setCount(UpdateCount);
            redisTemplate.opsForHash().put(nowTime + ":" + time,String.valueOf(fno),fkvo);
            flashKilling.setCount(UpdateCount);
            int i = flashKillingDao.update(flashKilling, new QueryWrapper<FlashKilling>()
                    .eq("fno",String.valueOf(fno))
                    .eq("count",count)
            );
            if (i<=0){
               throw new Exception("更新MySQL数据不一致");
            }
            String order_id = UUID.randomUUID().toString();
//            int i1 = order_fkDao.insert(order_fk.builder().order_id(order_id).fno(fno).good_id(fkvo.getGno()).user_id(userid).build());
            redisTemplate.opsForValue().set(Constant.REDIS_SECKILL_ORDER+fno+userid,
                    1,Constant.REDIS_SECKILL_ORDER_EXPIRE_DAY, TimeUnit.DAYS);
        }catch (Exception e){
            log.error("multilock exception:{}",e.getCause()+":"+e.getMessage());
            return false;
        }finally {
            lock.unlock();
            log.info("释放分布式锁成功key:{}",Constant.CACHE_KEY_REDLOCK);
        }
        return true;
    }

}
