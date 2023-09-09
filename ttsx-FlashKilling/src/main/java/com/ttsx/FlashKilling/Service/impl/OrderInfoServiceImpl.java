package com.ttsx.FlashKilling.Service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ttsx.FlashKilling.Service.IOrderInfoService;
import com.ttsx.FlashKilling.Service.IPayLogService;
import com.ttsx.FlashKilling.Service.IRefundLogService;
import com.ttsx.FlashKilling.mapper.OrderInfoMapper;
import com.ttsx.constant.Constant;
import com.ttsx.entity.log.PayLog;
import com.ttsx.entity.log.RefundLog;
import com.ttsx.entity.pojo.SecKillOrderInfo;
import com.ttsx.entity.vo.FlashKillingVO;
import com.ttsx.entity.vo.OperateIntergralVo;
import com.ttsx.entity.vo.PayVo;
import com.ttsx.entity.vo.RefundVo;
import com.ttsx.feignApi.AlipayFeignApi;
import com.ttsx.feignApi.IntergralFeignApi;
import com.ttsx.msg.SeckillCodeMsg;
import com.ttsx.utils.IdGenerateUtil;
import com.ttsx.utils.R;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dx
 * @since 2023-08-27
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, SecKillOrderInfo> implements IOrderInfoService {
    @Resource
    private OrderInfoMapper orderInfoMapper;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private AlipayFeignApi alipayFeignApi;
    @Resource
    private IRefundLogService iRefundLogService;
    @Resource
    private IPayLogService iPayLogService;
    @Resource
    private IntergralFeignApi intergralFeignApi;
    @Value("${alipay.returnUrl}")
    private String returlUrl;
    @Value("${alipay.notifyUrl}")
    private String notifyUrl;


    @Override
    public String createOrderInfo(String userid, FlashKillingVO flashKillingVO) {
        SecKillOrderInfo info = new SecKillOrderInfo();
        info.setCreateDate(new Date());
        info.setSeckillDate(flashKillingVO.getStart_data());
        info.setSeckillId(flashKillingVO.getFno());
        info.setProductId(flashKillingVO.getGno());
        info.setProductImg(flashKillingVO.getPics());
        info.setProductName(flashKillingVO.getGname());
        info.setProductPrice(flashKillingVO.getPrice());
        info.setSeckillPrice(flashKillingVO.getFk_price());
        info.setSeckillTime(flashKillingVO.getTime());
        info.setUserId(Long.parseLong(userid));
        info.setOrderNo(String.valueOf(IdGenerateUtil.get().nextId()));
        info.setIntergral(flashKillingVO.getIntergral());
        orderInfoMapper.insert(info);
        redisTemplate.opsForSet().add(Constant.REDIS_SECKILL_ORDER + flashKillingVO.getFno(), Integer.valueOf(userid));
        return info.getOrderNo();

    }

    @Override
    public String payOnline(Object orderNo) {
        SecKillOrderInfo orderInfo = this.getById((Serializable) orderNo);
        PayVo vo = new PayVo();
        vo.setOutTradeNo((String) orderNo);
        vo.setSubject(orderInfo.getProductName());
        vo.setTotalAmount(String.valueOf(orderInfo.getSeckillPrice()));
        vo.setBody(orderInfo.getProductName());
        vo.setReturnUrl(returlUrl);
        vo.setNotifyUrl(notifyUrl);
        R<String> result = alipayFeignApi.pay(vo);
        if (result == null || result.getCode()==0) {
            throw new RuntimeException(SeckillCodeMsg.PAY_SERVER_ERROR.getMsg());
        }
        return result.getData();
    }

    @Override
    @Transactional
    public void refundIntergral(SecKillOrderInfo secKillOrderInfo) {
        //插入日志
        RefundLog refundLog = new RefundLog();
        refundLog.setOrderNo(secKillOrderInfo.getOrderNo());
        refundLog.setRefundTime(LocalDateTime.now());
        refundLog.setRefundAmount(secKillOrderInfo.getIntergral().toString());
        refundLog.setRefundReason("取消订单");
        iRefundLogService.save(refundLog);
        //调用远程方法扣减积分
        OperateIntergralVo vo = new OperateIntergralVo();
        vo.setUserId(secKillOrderInfo.getUserId());
        vo.setValue(secKillOrderInfo.getIntergral().longValue());
        R<String> result = intergralFeignApi.incrIntergral(vo);
        if (result == null || result.getCode()==0) {
            throw new RuntimeException(SeckillCodeMsg.INTERGRAL_SERVER_ERROR.getMsg());
        }
        //更新订单状态
        int count = orderInfoMapper.changeRefundStatus(secKillOrderInfo.getOrderNo(), SecKillOrderInfo.STATUS_REFUND.intValue());
        if (count == 0) {
            throw new RuntimeException(SeckillCodeMsg.REFUND_ERROR.getMsg());
        }
    }

    @Override
    @Transactional
    public void paySuccess(String orderNo) {
        SecKillOrderInfo orderInfo = this.getById(orderNo);
        //插入支付日志
        PayLog log = new PayLog();
        log.setOrderNo(orderNo);
        log.setPayTime(LocalDateTime.now());
        log.setTotalAmount(orderInfo.getSeckillPrice().intValue());
        log.setPayType(SecKillOrderInfo.PAYTYPE_ONLINE.intValue());
        iPayLogService.save(log);
        //更新订单状态
        int count = orderInfoMapper.changePayStatus(orderNo, SecKillOrderInfo.STATUS_ACCOUNT_PAID.intValue(), orderInfo.getPayType());
        if(count==0){
            //记录日志
            throw new RuntimeException(SeckillCodeMsg.PAY_ERROR.getMsg());
        }
    }

    @Override
    public void refundOnline(SecKillOrderInfo secKillOrderInfo) {
        RefundVo vo = new RefundVo();
        vo.setOutTradeNo(secKillOrderInfo.getOrderNo());
        vo.setRefundReason("不想要了。");
        vo.setRefundAmount(String.valueOf(secKillOrderInfo.getSeckillPrice()));
        R<Boolean> result = alipayFeignApi.refund(vo);
        if (result == null || result.getCode()==0 || !result.getData()){
            throw new RuntimeException(SeckillCodeMsg.REFUND_ERROR.getMsg());
        }
    }

    @Override
    @GlobalTransactional
    public void payIntergral(String orderNo) {
        SecKillOrderInfo orderInfo = this.getById(orderNo);
        //1.插入日志,保证幂等性
        PayLog payLog = new PayLog();
        payLog.setOrderNo(orderNo);
        payLog.setPayTime(LocalDateTime.now());
        payLog.setTotalAmount(orderInfo.getIntergral().intValue());
        payLog.setPayType(PayLog.PAY_TYPE_INTERGRAL);
        iPayLogService.save(payLog);
        //2.调用积分远程方法
        OperateIntergralVo vo = new OperateIntergralVo();
        vo.setUserId(orderInfo.getUserId());
        vo.setValue(orderInfo.getIntergral().longValue());
        R<String> result = intergralFeignApi.decrIntergral(vo);
        if(result==null || result.getCode()==0){
            throw new RuntimeException(SeckillCodeMsg.INTERGRAL_SERVER_ERROR.getMsg());
        }
        //3.更新订单状态
        int count = orderInfoMapper.changePayStatus(orderNo, SecKillOrderInfo.STATUS_ACCOUNT_PAID.intValue(), SecKillOrderInfo.PAYTYPE_INTERGRAL);
        if(count==0){
            throw new RuntimeException(SeckillCodeMsg.PAY_ERROR.getMsg());
        }
    }
}
