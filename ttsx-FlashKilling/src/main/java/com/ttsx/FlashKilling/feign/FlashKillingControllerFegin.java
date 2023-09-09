package com.ttsx.FlashKilling.feign;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ttsx.entity.pojo.FlashKilling;
import com.ttsx.entity.pojo.Goodsinfo;
import com.ttsx.entity.vo.FlashKillingVO;
import com.ttsx.feignApi.FoodFeignApi;
import com.ttsx.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: -
 * @description:  秒杀的fegin类,供其他模块远程调用
 * @author: dx
 * @create: 2023/5/23 20:54
 */
@RestController
@RequestMapping("/fkFegin")
public class FlashKillingControllerFegin {
    @Autowired
    private FoodFeignApi foodFeignApi;
    @Autowired
    private com.ttsx.FlashKilling.mapper.flashKillingDao flashKillingDao;
    //展示秒杀商品信息
    @GetMapping("/showmsGoodsInfo")
    public R<List<FlashKillingVO>> selectmsGoodsInfo(@RequestParam(value = "time")  Object time ) {
        //获取当天秒杀商品集合
        QueryWrapper<FlashKilling> qw = new QueryWrapper<>();
        qw.eq("time", Integer.parseInt((String)time));
        long timestamp = new Date().getTime();
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(date);
        qw.eq("start_data",dateString);
        List<FlashKilling> list = flashKillingDao.selectList(qw);
        if (list.isEmpty()) {
            return R.error("查不到当天商品信息");
        }
        //获取秒杀商品的商品id集合
        List<Integer> GoodIDList = new ArrayList<>();
        for (FlashKilling f : list) {
            GoodIDList.add(f.getGno());
        }
        //利用id集合获取商品集合
        Map<Integer, Goodsinfo> map = new HashMap();
        List<Goodsinfo> goodsinfos = this.foodFeignApi.GetListByIdList(GoodIDList);
        for (Goodsinfo goodsinfo : goodsinfos) {
            map.put(goodsinfo.getGno(), goodsinfo);
        }
        //商品集合与秒杀商品集合   合并  组成前端所需的VO集合
        List<FlashKillingVO> flashKillingVOS = new ArrayList<>();
        for (FlashKilling fk : list) {
            FlashKillingVO vo = new FlashKillingVO();
            Goodsinfo goodsinfo = map.get(fk.getGno());
            //将数据copy到vo对象中
            BeanUtils.copyProperties(goodsinfo,vo);
            BeanUtils.copyProperties(fk,vo);
            flashKillingVOS.add(vo);
        }
        return R.success(flashKillingVOS);
    }
    //查询所有秒杀商品
    @GetMapping("/showmsGoodsInfoAll")
    public R<List<FlashKillingVO>> selectmsGoodsInfoAll() {
        //获取当天秒杀商品集合
        QueryWrapper<FlashKilling> qw = new QueryWrapper<>();
        List<FlashKilling> list = flashKillingDao.selectList(qw);
        //获取秒杀商品的商品id集合
        List<Integer> GoodIDList = new ArrayList<>();
        for (FlashKilling f : list) {
            GoodIDList.add(f.getGno());
        }
        //TODO:利用远程调用
        //利用id集合获取商品集合
        Map<Integer, Goodsinfo> map = new HashMap();
        List<Goodsinfo> goodsinfos = this.foodFeignApi.GetListByIdList(GoodIDList);
        for (Goodsinfo goodsinfo : goodsinfos) {
            map.put(goodsinfo.getGno(), goodsinfo);
        }
        //商品集合与秒杀商品集合   合并  组成前端所需的VO集合
        List<FlashKillingVO> flashKillingVOS = new ArrayList<>();
        for (FlashKilling fk : list) {
            FlashKillingVO vo = new FlashKillingVO();
            Goodsinfo goodsinfo = map.get(fk.getGno());
            //将数据copy到vo对象中
            BeanUtils.copyProperties(goodsinfo,vo);
            BeanUtils.copyProperties(fk,vo);
            flashKillingVOS.add(vo);
        }
        return R.success(flashKillingVOS);
    }
    
    //展示秒杀商品详情
    @GetMapping("/showmsGoodsDetail")
    public R<FlashKillingVO> showmsGoodsDetail(@RequestParam(value = "time",required = false)  String time,
                                               @RequestParam("seckillId")  String fno){

        //获取秒杀商品详情
        QueryWrapper<FlashKilling> qw1 = new QueryWrapper<>();
        qw1.eq("fno",Integer.parseInt(fno));
        FlashKilling flashKilling = this.flashKillingDao.selectOne(qw1);
        Goodsinfo goodsinfo = this.foodFeignApi.findById(flashKilling.getGno()).getData();
        FlashKillingVO vo = new FlashKillingVO();
        BeanUtils.copyProperties(goodsinfo,vo);
        BeanUtils.copyProperties(flashKilling,vo);
        return R.success(vo);
    }
}
