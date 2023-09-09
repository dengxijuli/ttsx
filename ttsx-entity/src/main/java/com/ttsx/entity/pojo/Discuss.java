package com.ttsx.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: mqb
 * @Date: 2023/5/7
 * @Time: 19:41
 * @Description:
 */
@Data
public class Discuss implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer did;
    private Integer mno;
    private Integer gno;
    private String dis;
    private String publishtime;
}
