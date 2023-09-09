package com.ttsx.FlashKilling.mq;


import com.ttsx.FlashKilling.Service.IFlashkillingService;
import com.ttsx.constant.MQConstant;
import com.ttsx.entity.vo.FlashKillingVO;
import com.ttsx.msg.SeckillCodeMsg;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * 监听controller层发的下单消息
 */
@Component
@RocketMQMessageListener(consumerGroup = "OrderPeddingQueueGroup",topic = MQConstant.ORDER_PEDDING_TOPIC)
public class OrderPeddingQueueListener implements RocketMQListener<OrderMessage> {
    @Autowired
    private IFlashkillingService iFlashkillingService;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Override
    public void onMessage(OrderMessage message) {
        OrderMQResult result = new OrderMQResult();
        result.setTime(message.getTime());
        result.setSeckillId(message.getFno());
        String tag = null;
        try{
            tag = MQConstant.ORDER_RESULT_SUCCESS_TAG;
            FlashKillingVO vo = iFlashkillingService.GetGoodsById(String.valueOf(message.getTime()), message.getFno());
            String orderNo = iFlashkillingService.doSeckill(message.getFno(),message.getUserid(), vo);
            result.setOrderNo(orderNo);
            rocketMQTemplate.syncSend(MQConstant.ORDER_PAY_TIMEOUT_TOPIC, MessageBuilder.withPayload(result).build(),3000,MQConstant.ORDER_PAY_TIMEOUT_DELAY_LEVEL);
        }catch(Exception e){
            e.printStackTrace();
            result.setCode(SeckillCodeMsg.SECKILL_ERROR.getCode());
            result.setMsg(SeckillCodeMsg.SECKILL_ERROR.getMsg());
            tag = MQConstant.ORDER_RESULT_FAIL_TAG;
        }
        result.setToken(message.getToken());
        rocketMQTemplate.syncSend(MQConstant.ORDER_RESULT_TOPIC+":"+tag,result);
    }
}
