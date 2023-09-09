package com.ttsx.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author dx
 * @since 2023-08-27
 */
@Data
@TableName("t_order_info")
@ApiModel(value = "OrderInfo对象", description = "秒杀商品订单")
public class SecKillOrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final Byte STATUS_ARREARAGE = 0;//未付款
    public static final Byte STATUS_ACCOUNT_PAID = 1;//已付款
    public static final Byte STATUS_CANCEL = 2;//手动取消订单
    public static final Byte STATUS_TIMEOUT = 3;//超时取消订单
    public static final Byte STATUS_REFUND = 4;//已退款
    public static final Byte PAYTYPE_ONLINE = 0;//在线支付
    public static final Byte PAYTYPE_INTERGRAL = 1;//积分支付


    @ApiModelProperty("订单id,主键")
    @TableId("order_no")
    private String orderNo;

    @ApiModelProperty("用户id")
    @TableField(value = "user_id")
    private Long userId;

    @ApiModelProperty("商品id")
    @TableField(value = "product_id")
    private Integer productId;

    @ApiModelProperty("商品图片")
    @TableField(value = "product_img")
    private String productImg;

    @ApiModelProperty("发货地址")
    @TableField(value = "delivery_addr_id")
    private Long deliveryAddrId;

    @ApiModelProperty("商品名字")
    @TableField(value = "product_name")
    private String productName;

    @ApiModelProperty("商品价格")
    @TableField(value = "product_price")
    private Double productPrice;

    @ApiModelProperty("秒杀价格")
    @TableField(value = "seckill_price")
    private Double seckillPrice;

    @ApiModelProperty("积分价格")
    private BigDecimal intergral;

    @ApiModelProperty("订单状态")
    private Byte status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(value = "create_date")
    private Date createDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(value = "pay_date")
    private Date payDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(value = "seckill_date")
    private Date seckillDate;

    @TableField(value = "seckill_time")
    private Integer seckillTime;

    @TableField(value = "seckill_id")
    private Integer seckillId;

    @TableField(value = "pay_type")
    private Byte payType;

}
