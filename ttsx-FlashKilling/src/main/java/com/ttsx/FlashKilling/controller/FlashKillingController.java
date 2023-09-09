package com.ttsx.FlashKilling.controller;

import com.ttsx.FlashKilling.Service.IFlashkillingService;
import com.ttsx.FlashKilling.component.RedisComponent;
import com.ttsx.FlashKilling.mq.OrderMessage;
import com.ttsx.constant.Constant;
import com.ttsx.constant.MQConstant;
import com.ttsx.entity.vo.FlashKillingVO;
import com.ttsx.msg.CommonCodeMsg;
import com.ttsx.msg.SeckillCodeMsg;
import com.ttsx.utils.JWTUtils;
import com.ttsx.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @program: -
 * @description:
 * @author: dx
 * @create: 2023/5/23 20:54
 */
@RestController
@RequestMapping("/fk")
@Slf4j
public class FlashKillingController {

    @Resource
    private RedisComponent redisComponent;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private IFlashkillingService iFlashkillingService;


    private static final String UV = "恭喜你抢到了";

    //根据场次，从缓存里获取当天秒杀商品集合
    @GetMapping("/showmsGoodsInfo")
    public R<List<FlashKillingVO>> GetGoodsFromCache(@RequestParam(value = "time") Object time) {
        List<FlashKillingVO> voList = iFlashkillingService.GetGoodsbyTimeFromCache(time);
        return R.success(voList);
    }

    //根据场次和秒杀id,从redis缓存查询秒杀商品详情
    @GetMapping("/showmsGoodsDetail")
    public R<FlashKillingVO> GetGoodsDetailFromCache(@RequestParam(value = "time", required = false) String time,
                                                     @RequestParam("seckillId") String fno) {
        FlashKillingVO vo = redisComponent.GetGoodsDetail(time, fno);
        return R.success(vo);
    }

    //下单操作
    @PostMapping("/doSeckill")
    public R<String> doSeckill(@RequestParam("time") String time,
                               @RequestParam("seckillId") String fno,
                               HttpServletRequest request) {

        FlashKillingVO flashKillingVO = redisComponent.GetGoodsDetail(time, fno);
        if (flashKillingVO == null) {
            return R.error(CommonCodeMsg.ILLEGAL_OPERATION.getMsg());
        }
        //判断时间
        if (new Date().getTime() < flashKillingVO.getStart_data().getTime()) {
            return R.error("非法操作");
        }
        String userid = null;
        String token = request.getHeader("Token");
        try {
            userid = (String) JWTUtils.getTokenInfo(token).get("userid");
            //判断是否重复下单
            Boolean Is_exist = redisComponent.IsUserOrder(Integer.valueOf(userid), fno);
            if (Is_exist) {
                return R.error(SeckillCodeMsg.REPEAT_SECKILL.getMsg());
            }
        } catch (Exception e) {
            return R.error("登陆超时,请重新登陆");
        }

        try {
            //判断是否还有库存
            Long remainCount = redisComponent.SeckillStockSubstrateOne(
                    Constant.REDIS_SECKILL_STOCK + time, fno, -1);
            if (remainCount < 0) {
                return R.error(SeckillCodeMsg.SECKILL_STOCK_OVER.getMsg());
            }
            log.info("恭喜用户id为:{},抢到秒杀商品id:{},开始秒杀业务", userid, fno);
            OrderMessage message = new OrderMessage(Integer.parseInt(time), Integer.valueOf(fno), token, userid);
            Message<OrderMessage> msg = MessageBuilder.withPayload(message).build();
            rocketMQTemplate.syncSend(MQConstant.ORDER_PEDDING_TOPIC, msg);
        } catch (Exception e) {
            log.error("秒杀下单出现异常:{}", e.getMessage());
            redisComponent.SeckillStockSubstrateOne(Constant.REDIS_SECKILL_STOCK + time, fno, 1);
        }
        return R.success("进入抢购队列,请等待结果");
    }


}
