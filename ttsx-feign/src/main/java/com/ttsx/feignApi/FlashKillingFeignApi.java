package com.ttsx.feignApi;


import com.ttsx.entity.vo.FlashKillingVO;
import com.ttsx.feignApi.fallback.FlashKillingFeignApiFallback;
import com.ttsx.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient( value = "ttsx-FlashKilling", path = "fkFegin", fallback = FlashKillingFeignApiFallback.class)
public interface FlashKillingFeignApi {
    //展示秒杀商品详情
    @GetMapping("/showmsGoodsDetail")
    public R<FlashKillingVO> showmsGoodsDetail(@RequestParam(value = "time", required = false) String time,
                                               @RequestParam("seckillId") String fno);

    @GetMapping("/showmsGoodsInfo")
    public R<List<FlashKillingVO>> selectmsGoodsInfo(@RequestParam(value = "time") Object time);

    @GetMapping("/showmsGoodsInfoAll")
    public R<List<FlashKillingVO>> selectmsGoodsInfoAll();
}

