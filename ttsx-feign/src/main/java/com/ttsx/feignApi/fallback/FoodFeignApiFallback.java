package com.ttsx.feignApi.fallback;

import com.ttsx.entity.pojo.Goodsinfo;
import com.ttsx.feignApi.FoodFeignApi;
import com.ttsx.utils.R;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FoodFeignApiFallback implements FoodFeignApi {
    @Override
    public R<Goodsinfo> findById(Integer fid) {
        return null;
    }

    @Override
    public List<Goodsinfo> GetListByIdList(List<Integer> GoodIDList) {
        return null;
    }
}
