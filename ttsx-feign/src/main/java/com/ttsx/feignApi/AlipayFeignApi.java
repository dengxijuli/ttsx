package com.ttsx.feignApi;


import com.ttsx.entity.vo.PayVo;
import com.ttsx.entity.vo.RefundVo;
import com.ttsx.feignApi.fallback.AlipayFeignApiFallback;
import com.ttsx.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "ttsx-alipay", path = "alipay", fallback = AlipayFeignApiFallback.class)
public interface AlipayFeignApi {
    @RequestMapping("/pay")
    R<String> pay(PayVo payVo);

    @RequestMapping("/rsaCheckV1")
    R<Boolean> rsaCheckV1(@RequestParam Map<String, String> params);
    @RequestMapping("/refund")
    R<Boolean> refund(RefundVo refundVo);

}
