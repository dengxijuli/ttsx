package com.example.ttsxscheduling.scheduling;


import com.ttsx.entity.vo.FlashKillingVO;
import com.ttsx.feignApi.FlashKillingFeignApi;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @program: -
 * @description: 定时任务，查询秒杀商品，存到redis里
 * @author: dx
 * @create: 2023/5/25 20:28
 */
//@Component
public class JobScheduling {
    @Resource
    private FlashKillingFeignApi flashKillingFeignApi;
    @Resource
    private RedisTemplate redisTemplate;
    //每天执行一次
    @Scheduled(cron =" 0 0 0 * * ?")
    public void selectjob(){
        System.out.println("执行定时上架....");

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


        redisTemplate.delete(yesterdayTime+":"+"10");
        redisTemplate.delete(yesterdayTime+":"+"16");
        redisTemplate.delete(yesterdayTime+":"+"22");


        //今天数据
        List<FlashKillingVO> data = flashKillingFeignApi.selectmsGoodsInfo(10).getData();
        for (FlashKillingVO flashKillingVO : data) {
            redisTemplate.opsForHash().put(nowTime+":"+"10",flashKillingVO.getFno().toString(),flashKillingVO);
        }
        List<FlashKillingVO> data2 = flashKillingFeignApi.selectmsGoodsInfo(16).getData();
        for (FlashKillingVO flashKillingVO : data2) {
            redisTemplate.opsForHash().put(nowTime+":"+"16",flashKillingVO.getFno().toString(),flashKillingVO);
        }
        List<FlashKillingVO> data3 = flashKillingFeignApi.selectmsGoodsInfo(22).getData();
        for (FlashKillingVO flashKillingVO : data3) {
            redisTemplate.opsForHash().put(nowTime+":"+"22",flashKillingVO.getFno().toString(),flashKillingVO);
        }

        System.out.println("完成....");
    }
    @PostConstruct
    public void init() {
        selectjob();
    }

}
