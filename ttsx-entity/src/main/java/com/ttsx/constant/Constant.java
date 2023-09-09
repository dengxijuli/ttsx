package com.ttsx.constant;

public class Constant {
    //秒杀用户下单的Redis,key
    public static final String  REDIS_SECKILL_ORDER = "REDIS_SECKILL_ORDER:";
    //
    public static final int REDIS_SECKILL_ORDER_EXPIRE_DAY = 1;
    //redis分布式锁的key
    public static final String CACHE_KEY_REDLOCK = "ORDER_REDLOCK";
    //redis , 秒杀商品库存key: + time (场次)
    public static final String REDIS_SECKILL_STOCK = "REDIS_SECKILL_STOCK:";

    public static final String TOKEN_NAME = "token";
    //token默认过期时间
    public static final int EXPIRETIME=3600*5; //5小时

}
