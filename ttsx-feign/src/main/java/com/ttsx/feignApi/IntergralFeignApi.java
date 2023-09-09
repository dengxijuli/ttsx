package com.ttsx.feignApi;


import com.ttsx.entity.vo.OperateIntergralVo;
import com.ttsx.feignApi.fallback.IntergralFeignApiFallback;
import com.ttsx.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "alipayFeignApi",path = "intergral",fallback = IntergralFeignApiFallback.class)
public interface IntergralFeignApi {

    @RequestMapping("/decrIntergral")
    R<String> decrIntergral(@RequestBody OperateIntergralVo vo);


    @RequestMapping("/incrIntergral")
    R<String> incrIntergral(@RequestBody OperateIntergralVo vo);



}
