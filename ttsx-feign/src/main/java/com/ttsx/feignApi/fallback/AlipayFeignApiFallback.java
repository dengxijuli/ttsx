package com.ttsx.feignApi.fallback;

import com.ttsx.entity.vo.PayVo;
import com.ttsx.entity.vo.RefundVo;
import com.ttsx.feignApi.AlipayFeignApi;
import com.ttsx.utils.R;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AlipayFeignApiFallback implements AlipayFeignApi {
    @Override
    public R<String> pay(PayVo payVo) {
        return null;
    }

    @Override
    public R<Boolean> rsaCheckV1(Map<String, String> params) {
        return null;
    }

    @Override
    public R<Boolean> refund(RefundVo vo) {
        return null;
    }
}
