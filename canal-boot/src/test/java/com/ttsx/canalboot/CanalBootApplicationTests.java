package com.ttsx.canalboot;

import com.ttsx.entity.pojo.Goodsinfo;
import com.ttsx.feignApi.FoodFeignApi;
import com.ttsx.utils.R;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
class CanalBootApplicationTests {

    @Resource
    private FoodFeignApi foodFeignApi;

    @Test
    void contextLoads() {
    }

    @Test
    public void testFeign(){
        R<Goodsinfo> byId = foodFeignApi.findById(1);
        System.out.println(byId.toString());
    }
}
