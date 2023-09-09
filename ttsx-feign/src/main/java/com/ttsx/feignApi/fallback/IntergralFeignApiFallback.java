package com.ttsx.feignApi.fallback;

import com.ttsx.entity.vo.OperateIntergralVo;
import com.ttsx.feignApi.IntergralFeignApi;
import com.ttsx.utils.R;
import org.springframework.stereotype.Component;

@Component
public class IntergralFeignApiFallback implements IntergralFeignApi {
    @Override
    public R<String> decrIntergral(OperateIntergralVo vo) {
        return null;
    }
    @Override
    public R<String> incrIntergral(OperateIntergralVo vo) {
        return null;
    }
}
