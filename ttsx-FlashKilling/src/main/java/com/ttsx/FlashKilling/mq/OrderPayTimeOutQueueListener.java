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
@RocketMQMessageListener(consumerGroup = "orderPayTimeOutQueueGroup",topic = MQConstant.ORDER_PAY_TIMEOUT_TOPIC)
public class OrderPayTimeOutQueueListener implements RocketMQListener<OrderMQResult> {
    @Autowired
    private IFlashkillingService iFlashkillingService;
    @Override
    public void onMessage(OrderMQResult message) {
        iFlashkillingService.cancelOrder(message.getOrderNo());
    }
}
