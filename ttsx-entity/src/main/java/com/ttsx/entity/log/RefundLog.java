package com.ttsx.entity.log;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

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
@Getter
@Setter
@TableName("t_refund_log")
@ApiModel(value = "RefundLog对象", description = "退款日志")
public class RefundLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("order_no")
    private String orderNo;


    @TableField("refund_time")
    private LocalDateTime refundTime;

    @TableField("refund_amount")
    private String refundAmount;
    @TableField("refund_reason")
    private String refundReason;
    @TableField("refund_type")
    private Byte refundType;

    @Override
    public String toString() {
        return "RefundLog{" +
            "orderNo = " + orderNo +
            ", refundTime = " + refundTime +
            ", refundAmount = " + refundAmount +
            ", refundReason = " + refundReason +
            ", refundType = " + refundType +
        "}";
    }
}
