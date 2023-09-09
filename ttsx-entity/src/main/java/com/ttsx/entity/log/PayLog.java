package com.ttsx.entity.log;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author dx
 * @since 2023-08-31
 */
@TableName("t_pay_log")
@ApiModel(value = "PayLog对象", description = "支付日志")
@Data
public class PayLog implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int PAY_TYPE_ONLINE = 0;//在线支付
    public static final int PAY_TYPE_INTERGRAL = 1;//积分支付

    @TableId("order_no")
    private String orderNo;
    @TableField("pay_time")
    private LocalDateTime payTime;
    @TableField("total_amount")
    private Integer totalAmount;
    @TableField("pay_type")
    private Integer payType;

}
