package com.ttsx.FlashKilling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ttsx.entity.pojo.SecKillOrderInfo;
import org.apache.ibatis.annotations.Param;

/**
 *秒杀订单mapper接口
 */
public interface OrderInfoMapper extends BaseMapper<SecKillOrderInfo> {

    int changePayStatus(@Param("orderNo") String orderNo, @Param("status") Integer status, @Param("payType") int payType);

    /**
     * 将订单状态修改成已退款状态
     * @param outTradeNo
     * @param statusRefund
     * @return
     */
    int changeRefundStatus(@Param("orderNo") String outTradeNo, @Param("status") Integer statusRefund);

}
