package com.ttsx.entity.pojo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_usable_integral")
public class UsableIntegral implements Serializable {
    @TableId("user_id")
    private Long userId;//用户id
    @TableField("gmt_created")
    private Date gmtCreated;//创建时间
    @TableField("gmt_modified")
    private Date gmtModified;//更新时间
    @TableField("amount")
    private Long amount;//目前可用积分
    @TableField("freezed_amount")
    private Long freezedAmount;//冻结金额
}