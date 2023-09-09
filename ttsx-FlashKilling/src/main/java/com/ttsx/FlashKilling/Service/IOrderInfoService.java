package com.ttsx.FlashKilling.Service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ttsx.entity.pojo.SecKillOrderInfo;
import com.ttsx.entity.vo.FlashKillingVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dx
 * @since 2023-08-27
 */
public interface IOrderInfoService extends IService<SecKillOrderInfo> {

    String createOrderInfo(String userid, FlashKillingVO flashKillingVO);

    String payOnline(Object orderNo);

    void refundIntergral(SecKillOrderInfo orderInfo);

    void paySuccess(String orderNo);

    void refundOnline(SecKillOrderInfo secKillOrderInfo);

    void payIntergral(String orderNo);
}
