package com.ttsx.FlashKilling.mapper;

import com.alibaba.fastjson.JSON;
import com.ttsx.entity.pojo.FlashKilling;
import com.ttsx.entity.vo.FlashKillingVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * @program: -
 * @description:
 * @author: dx
 * @create: 2023/5/25 15:58
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class testMS {
    @Autowired
    private com.ttsx.FlashKilling.mapper.flashKillingDao flashKillingDao;
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void  testD(){
        FlashKilling flashKilling = flashKillingDao.selectById(1);
        flashKilling.setFk_price(null);
        flashKilling.setStart_data(null);
        FlashKillingVO flashKillingVO = new FlashKillingVO();
        BeanUtils.copyProperties(flashKilling,flashKillingVO);
        String jsonString = JSON.toJSONString(flashKillingVO);
        System.out.println(jsonString);
    }


    @Test
    public void test1(){
//        String dateTimeStr = "2023-05-26T16:00:00.000+00:00";
//        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTimeStr);
//        LocalDate localDate = zonedDateTime.withZoneSameInstant(ZoneOffset.UTC).toLocalDate();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        String dateStr = localDate.format(formatter);
//        System.out.println(new Date().getTime());
        long time = new Date().getTime();
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String s = sdf.format(date);
        System.out.println(s);
    }

    @Test
    public void test5(){
        System.out.println("开始。。。。。。。。。。。。。");
        Set members = redisTemplate.opsForSet().members("REDIS_SECKILL_ORDER:5");

        System.out.println("测试结果1:"+ members.toString());

        Boolean member = redisTemplate.opsForSet().isMember("REDIS_SECKILL_ORDER:" + "5", 3);

        System.out.println("测试结果2:"+ member);


    }
}
