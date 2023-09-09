package com.ttsx.FlashKilling.mq;


import com.ttsx.FlashKilling.Service.IFlashkillingService;
import com.ttsx.constant.MQConstant;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lanxw
 */
@Component
@RocketMQMessageListener(consumerGroup = "orderResultFailConsumer",topic = MQConstant.ORDER_RESULT_TOPIC,selectorExpression = MQConstant.ORDER_RESULT_FAIL_TAG)
public class OrderResultFailListener implements RocketMQListener<OrderMQResult> {
    @Autowired
    private IFlashkillingService iFlashkillingService;
    @Override
    public void onMessage(OrderMQResult message) {
        try{
            iFlashkillingService.syncRedisStock(message.getTime(),message.getSeckillId());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
