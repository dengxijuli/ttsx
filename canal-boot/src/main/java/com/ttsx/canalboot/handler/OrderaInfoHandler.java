package com.ttsx.canalboot.handler;

import com.ttsx.constant.Constant;
import com.ttsx.entity.pojo.FlashKilling;
import com.ttsx.entity.pojo.Goodsinfo;
import com.ttsx.entity.vo.FlashKillingVO;
import com.ttsx.feignApi.FoodFeignApi;
import com.ttsx.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import top.javatool.canal.client.annotation.CanalTable;
import top.javatool.canal.client.handler.EntryHandler;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 集成canal server  ,canal客户端，flashKillingVO同步MySQL
 */


@Slf4j
@Component
@CanalTable(value = "flashkilling")
public class OrderaInfoHandler implements EntryHandler<FlashKilling> {
    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    private FoodFeignApi foodFeignApi;
    @Override
    public void insert(FlashKilling flashKilling) {
        log.info("当有数据插入的时候会触发这个方法");
        try {
            if (DateUtils.isToday(flashKilling.getStart_data())) {
                upateOrInsert(flashKilling);
            }
        } catch (Exception e) {
            log.error("canal客户端进行redis insert异常,msg:{}", e.getMessage());
        }
    }

    @Override
    public void update(FlashKilling before, FlashKilling after) {
        log.info("当有数据更新的时候会触发这个方法");
        try {
            if (DateUtils.isToday(after.getStart_data())) {
                upateOrInsert(after);
                if (before.getCount() != null) {
                    //更新秒杀预库存
                    redisTemplate.opsForHash().put(
                            Constant.REDIS_SECKILL_STOCK + after.getTime(),
                            String.valueOf(after.getFno()),
                            before.getCount());
                }
            }
        } catch (Exception e) {
            log.error("canal客户端进行redis update异常,redis同步MySQL数据失败,msg:{}", e.getMessage());
            e.printStackTrace();
        }
    }

    private void upateOrInsert(FlashKilling after) {
        String nowTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE);
        FlashKillingVO flashKillingVO = new FlashKillingVO();
        Goodsinfo goodsinfo = foodFeignApi.findById(after.getGno()).getData();
        BeanUtils.copyProperties(goodsinfo,flashKillingVO);
        BeanUtils.copyProperties(after,flashKillingVO);
        redisTemplate.opsForHash().put(
                nowTime +":"+ after.getTime(), String.valueOf(after.getFno()), flashKillingVO);
    }

    @Override
    public void delete(FlashKilling flashKilling) {
        log.info("当有数据删除的时候会触发这个方法");
        try {
            if (DateUtils.isToday(flashKilling.getStart_data())) {
                String nowTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE);
                redisTemplate.opsForHash().delete(
                        nowTime +":"+ flashKilling.getTime(), String.valueOf(flashKilling.getFno()));
                //删除当天秒杀预库存
                redisTemplate.opsForHash().delete(Constant.REDIS_SECKILL_STOCK + nowTime, String.valueOf(flashKilling.getFno()));
            }
        } catch (Exception e) {
            log.error("canal客户端进行redis delete异常,redis同步MySQL数据失败,msg:{}", e.getMessage());
        }
    }


}