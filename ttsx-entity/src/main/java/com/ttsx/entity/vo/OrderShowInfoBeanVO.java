package com.ttsx.entity.vo;

import lombok.Data;

@Data
public class OrderShowInfoBeanVO {
    private String gname;
    private Integer nums;
    private Double price;

    public String toString(){
        return "<b>商品名:"+gname+", 数量:"+nums+", 价格"+price+"</b> <br/>";
    }
}
