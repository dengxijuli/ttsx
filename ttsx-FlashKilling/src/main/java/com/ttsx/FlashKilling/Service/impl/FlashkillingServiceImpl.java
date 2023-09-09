package com.ttsx.FlashKilling.Service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ttsx.FlashKilling.Service.IFlashkillingService;
import com.ttsx.FlashKilling.Service.IOrderInfoService;
import com.ttsx.FlashKilling.component.RedisComponent;
import com.ttsx.FlashKilling.mapper.FlashkillingMapper;
import com.ttsx.FlashKilling.mapper.OrderInfoMapper;
import com.ttsx.entity.enums.SeckillRedisKey;
import com.ttsx.entity.pojo.FlashKilling;
import com.ttsx.entity.pojo.SecKillOrderInfo;
import com.ttsx.entity.vo.FlashKillingVO;
import com.ttsx.msg.SeckillCodeMsg;
import com.ttsx.utils.R;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dx
 * @since 2023-08-22
 */
@Service
public class FlashkillingServiceImpl extends ServiceImpl<FlashkillingMapper, FlashKilling> implements IFlashkillingService {
    @Resource
    private RedisComponent redisComponent;
    @Resource
    private FlashkillingMapper flashkillingMapper;
    @Resource
    private IOrderInfoService iOrderInfoService;
    @Resource
    private OrderInfoMapper orderInfoMapper;


    @Override
    public List<FlashKillingVO> GetGoodsbyTimeFromCache(Object time) {
        List<FlashKillingVO> list = redisComponent.GetGoodsbyTime(time);
        if (list.isEmpty()) {
            R.error("无法获取商品数据");
        }
        return list;
    }

    @Override
    public FlashKillingVO GetGoodsById(String time, Integer fno) {
        return redisComponent.GetGoodsDetail(time, String.valueOf(fno));
    }

    @Override
    @Transactional
    public String doSeckill(Integer fno, String userid, FlashKillingVO flashKillingVO) {
        int count = flashkillingMapper.decrStock(flashKillingVO.getFno());
        if (count == 0) {
            throw new RuntimeException(SeckillCodeMsg.SECKILL_STOCK_OVER.getMsg());
        }
        return iOrderInfoService.createOrderInfo(userid, flashKillingVO);
    }


    @Override
    public void syncRedisStock(Integer time, Integer seckillId) {
        int stockCount = flashkillingMapper.selectById(seckillId).getCount();
        if (stockCount > 0) {
            String key = SeckillRedisKey.SECKILL_STOCK_COUNT_HASH.getRealKey(String.valueOf(time));
            redisComponent.UpdateGoodsCount(key, String.valueOf(seckillId), String.valueOf(stockCount));
        }
    }

    @Override
    @Transactional
    public void cancelOrder(String orderNo) {
        SecKillOrderInfo orderInfo = orderInfoMapper.selectById(orderNo);
        if (SecKillOrderInfo.STATUS_ARREARAGE.equals(orderInfo.getStatus())) {
            orderInfo.setStatus(SecKillOrderInfo.STATUS_CANCEL);
            orderInfoMapper.updateById(orderInfo);
            //增加真实库存
            flashkillingMapper.incrStock(orderInfo.getSeckillId());
            //同步预库存
            syncRedisStock(orderInfo.getSeckillTime(), orderInfo.getSeckillId());
            System.out.println("取消订单成功");
        }
    }

}
