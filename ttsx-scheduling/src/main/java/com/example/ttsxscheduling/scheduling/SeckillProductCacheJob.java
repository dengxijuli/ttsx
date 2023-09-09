package com.example.ttsxscheduling.scheduling;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.ttsx.constant.Constant;
import com.ttsx.entity.vo.FlashKillingVO;
import com.ttsx.feignApi.FlashKillingFeignApi;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@Getter
@RefreshScope
@Slf4j
public class SeckillProductCacheJob implements SimpleJob {
    @Value("${jobCron.initSeckillProduct}")
    private String cron;
    @Value("${shardingItemParameters.initSeckillProduct}")
    private String shardingItemParameters;
    @Value("${shardingTotalCount.initSeckillProduct}")
    private Integer shardingTotalCount;
    @Resource
    private RedisTemplate redisTemplate;
    @Autowired
    private FlashKillingFeignApi flashKillingFeignApi;
    @Override
    public void execute(ShardingContext shardingContext) {
        //任务分片,进行任务调度
        String shardingParameter = shardingContext.getShardingParameter();
        log.info("执行定时上架,此分片为:{},代表:{}",shardingContext.getShardingItem(), shardingParameter);
        log.info("corn表达式此时为:{}或者为{}",cron,this.getCron());
        Date now = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();

        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 昨天时间
        String yesterdayTime = sdf.format(yesterday);
        // 现在时间
        String nowTime = sdf.format(now);
        // 明天时间
        LocalDate today = LocalDate.now();
        LocalDate tomorrowTime = today.plusDays(1);

        redisTemplate.delete(yesterdayTime+":"+shardingParameter);
        //今天数据
        List<FlashKillingVO> data = flashKillingFeignApi.selectmsGoodsInfo(shardingParameter).getData();
        for (FlashKillingVO flashKillingVO : data) {
            //预缓存
            redisTemplate.opsForHash().put(
                    nowTime+":"+shardingParameter,
                    flashKillingVO.getFno().toString(),
                    flashKillingVO);
            //预库存
            redisTemplate.opsForHash().put(
                    Constant.REDIS_SECKILL_STOCK+shardingParameter,
                    String.valueOf(flashKillingVO.getFno()),
                    flashKillingVO.getCount());
        }
    }


}
