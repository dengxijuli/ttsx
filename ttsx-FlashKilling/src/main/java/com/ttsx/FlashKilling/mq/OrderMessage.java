package com.ttsx.FlashKilling.mq;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderMessage implements Serializable {
    private Integer time;//秒杀场次
    private Integer fno;//秒杀商品ID
    private String token;//用户的token信息
    private String userid;//用户id
}
