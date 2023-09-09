package com.ttsx.order.controller;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ttsx.order.biz.OrderBiz;
import com.ttsx.utils.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: 86150
 * @CreateDate: 2023-05-11 上午 11:20
 */
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderBiz biz;

    @RequestMapping("addOrder")
    public Map<String,Object> addOrder(HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> map = new HashMap<>();
        String cartgoods = request.getParameter("cartgoods");
        String ano = request.getParameter("ano");
        List<Map<String, Object>> lists = JSON.parseObject(cartgoods, new TypeReference<List<Map<String, Object>>>(){});
        Integer integer = this.biz.addOrder(lists, ano);
        if(integer!=0){
            map.put("code",1);
        }else{
            map.put("code",0);
        }
        return map;
    }

    @RequestMapping("showOrderbyPage")
    public Map<String,Object> showOrderbyPage(PageBean pageBean){
        Map<String,Object> map = new HashMap<>();
        PageBean bean = this.biz.showOrderbyPage(pageBean);
        if(bean!=null){
            map.put("code",1);
            map.put("data",pageBean);
        }else {
            map.put("code",0);
        }
        return map;
    }
}
