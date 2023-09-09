package com.ttsx.FlashKilling.Service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ttsx.entity.pojo.FlashKilling;
import com.ttsx.entity.vo.FlashKillingVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dx
 * @since 2023-08-22
 */
public interface IFlashkillingService extends IService<FlashKilling> {


    List<FlashKillingVO> GetGoodsbyTimeFromCache(Object time);

    FlashKillingVO GetGoodsById(String s, Integer fno);

    String doSeckill(Integer fno, String userid,FlashKillingVO flashKillingVO);

    void syncRedisStock(Integer time, Integer seckillId);

    void cancelOrder(String orderNo);
}
