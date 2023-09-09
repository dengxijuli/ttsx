package com.ttsx.entity.log;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lanxw
 * 用户TCC事务控制的日志表
 */
@Data
@TableName("t_account_transaction")
public class AccountTransaction implements Serializable {
    public static final int STATE_TRY = 1;
    public static final int STATE_COMMIT = 2;
    public static final int STATE_CANCEL = 3;
    @TableField("tx_id")
    private String txId;//全局事务ID
    @TableField("action_id")
    private Long actionId;//分支事务ID
    @TableField("user_id")
    private Long userId;//用户ID
    @TableField("gmt_created")
    private Date gmtCreated;//事务日志记录创建时间
    @TableField("gmt_modified")
    private Date gmtModified;//事务日志记录修改时间
    private Long amount;//此次积分变更数值
    private int state = STATE_TRY;//事务状态
}
