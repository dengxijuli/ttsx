package com.ttsx.feignApi;

import com.ttsx.entity.pojo.Goodsinfo;
import com.ttsx.feignApi.fallback.FoodFeignApiFallback;
import com.ttsx.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 商品模块--feign api
 */
@FeignClient(value = "ttsx-foods", path = "goods", fallback = FoodFeignApiFallback.class)
public interface FoodFeignApi {

    //open feign 支持SpringMVC 注解
    @RequestMapping("findById/{fid}")
    R<Goodsinfo> findById(@PathVariable Integer fid);


    @PostMapping("/getListByIdList")
    List<Goodsinfo> GetListByIdList(@RequestParam("GoodIDList") List<Integer> GoodIDList);

}



