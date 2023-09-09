package com.ttsx.background.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ttsx.background.Service.GoodsService;
import com.ttsx.entity.pojo.FlashKilling;
import com.ttsx.entity.pojo.Goodsinfo;
import com.ttsx.entity.vo.FlashKillingVO;
import com.ttsx.feignApi.FlashKillingFeignApi;
import com.ttsx.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: -
 * @description:
 * @author: dx
 * @create: 2023/5/26 10:44
 */
@RestController
@Slf4j
@RequestMapping("/backgroud/FlashKilling")
public class FlashKillingController {
    @Autowired
    private FlashKillingFeignApi flashKillingFeignApi;
    @Autowired
    private com.ttsx.background.mapper.flashKillingDao flashKillingDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private GoodsService goodsService;

    //查询
    @RequestMapping("/showmsGoodsInfo")
    public R<List<FlashKillingVO>> selectmsGoodsInfo() {
        List<FlashKillingVO> data = this.flashKillingFeignApi.selectmsGoodsInfoAll().getData();
        if (data == null) {
            R.error("无法获取数据");
        }
        return R.success(data);
    }

    //添加秒杀商品
    @RequestMapping("/addGoodInfo")
    @Transactional
    public Map addGoodInfo(@RequestParam("gno") String gno,
                           @RequestParam("fk_price") String fk_price,
                           @RequestParam("count") String count,
                           @RequestParam("start_dateString") String start_dateString,
                           @RequestParam("time") String time) throws ParseException {
        Map map = new HashMap();
        try {
            FlashKilling fk = new FlashKilling();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(start_dateString);
            fk.setStart_data(date);
            fk.setGno(Integer.parseInt(gno));
            fk.setFk_price(Double.parseDouble(fk_price));
            fk.setCount(Integer.parseInt(count));
            fk.setTime(Integer.parseInt(time));
            int i = this.flashKillingDao.insert(fk);
            if (i == 0) {
                map.put("code", 0);
                map.put("msg", "无法插入数据");
            }
            //获取fno
            QueryWrapper<FlashKilling> qw = new QueryWrapper<>();
            qw.eq("gno", gno).eq("time", time);
            FlashKilling flashKilling = flashKillingDao.selectOne(qw);
            Integer fno = flashKilling.getFno();
            QueryWrapper<Goodsinfo> qw2 = new QueryWrapper<>();
            qw2.eq("gno", flashKilling.getGno());
            Goodsinfo goodsinfo = this.goodsService.getOne(qw2);
            FlashKillingVO flashKillingVO = new FlashKillingVO();
            BeanUtils.copyProperties(goodsinfo, flashKillingVO);
            BeanUtils.copyProperties(flashKilling, flashKillingVO);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 0);
            map.put("msg", "无法插入数据,请检查是否有重复的键或者异常类型数据");
            return map;
        }
        map.put("code", 1);
        return map;
    }

    //删除
    @RequestMapping("/toDeletX")
    @Transactional
    public Map toDeletX(@RequestParam("fno") String fno,
                        @RequestParam("start_dateString") String start_dateString,
                        @RequestParam("time") String time) {
        Map map = new HashMap();
        try {
            QueryWrapper<FlashKilling> qw = new QueryWrapper<>();
            qw.eq("fno", fno);
            FlashKilling flashKilling = this.flashKillingDao.selectOne(qw);
            if (flashKilling == null) {
                throw new RuntimeException("查无此数据!请联系管理员!");
            }
            int i = this.flashKillingDao.delete(qw);
            if (i == 0) {
                throw new RuntimeException("删除失败,无法删除指定数据...");
            }
            map.put("code", 1);
            map.put("data", i);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 0);
            map.put("msg","删除数据失败");
        }
        return map;
    }

    //修改
    @RequestMapping("/modifyGoodInfo")
    @Transactional
    public Map modifyGoodInfo(@RequestParam("fno") String fno,
                              @RequestParam("gno") String gno,
                              @RequestParam("fk_price") String fk_price,
                              @RequestParam("count") String count,
                              @RequestParam("currentCount") String currentCount,
                              @RequestParam("start_dateString") String start_dateString,
                              @RequestParam("time") String time) {
        Map map = new HashMap();
        try {
            QueryWrapper<FlashKilling> qw = new QueryWrapper<>();
            qw.eq("fno", fno);
            FlashKilling flag = this.flashKillingDao.selectOne(qw);
            if (flag == null) {
                throw new RuntimeException("查无此编号,请联系管理员.");
            }
            FlashKilling fk = new FlashKilling();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(start_dateString);
            fk.setFno(Integer.parseInt(fno));
            fk.setStart_data(date);
            fk.setGno(Integer.parseInt(gno));
            fk.setFk_price(Double.parseDouble(fk_price));
            fk.setCount(Integer.parseInt(count));
            fk.setTime(Integer.parseInt(time));

            int i = this.flashKillingDao.updateById(fk);
            if (i == 0) {
                throw new RuntimeException("更改失败,更改数据为0,请联系管理员");
            }

            QueryWrapper<Goodsinfo> qw2 = new QueryWrapper<>();
            qw2.eq("gno", gno);
            Goodsinfo goodsinfo = this.goodsService.getOne(qw2);

            FlashKillingVO flashKillingVO = new FlashKillingVO();
            BeanUtils.copyProperties(goodsinfo, flashKillingVO);
            BeanUtils.copyProperties(fk, flashKillingVO);
            flashKillingVO.setCurrentCount(Integer.parseInt(currentCount));

            map.put("code", 1);
            map.put("data", i);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 0);
            map.put("msg", e.getMessage());
        }
        return map;
    }

}
