package com.ttsx.entity.vo;

import lombok.Data;

@Data
public class OrderInfoBeanVO {
    private Integer ono;
    private String odate;
    private Integer ano;
    private String sdate;
    private String rdate;
    private Integer status;
    private Double price;
    private Integer invoice;
    private String addr;
    private String sstatus;
    private String sinvoice;
}
