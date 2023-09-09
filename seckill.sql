
use fresh;
CREATE TABLE "t_order_info" (
                                "order_no" varchar(50) NOT NULL,
                                "user_id" bigint(20) DEFAULT NULL,
                                "product_id" bigint(20) DEFAULT NULL,
                                "product_img" varchar(255) DEFAULT NULL,
                                "delivery_addr_id" bigint(20) DEFAULT NULL,
                                "product_name" varchar(200) DEFAULT NULL,
                                "product_price" decimal(10,2) DEFAULT NULL,
                                "seckill_price" decimal(10,2) DEFAULT NULL,
                                "status" tinyint(4) DEFAULT NULL,
                                "create_date" datetime DEFAULT NULL,
                                "pay_date" datetime DEFAULT NULL,
                                "seckill_date" date DEFAULT NULL,
                                "seckill_time" int(11) DEFAULT NULL,
                                "intergral" decimal(10,0) DEFAULT NULL,
                                "seckill_id" bigint(20) DEFAULT NULL,
                                "pay_type" tinyint(4) DEFAULT NULL,
                                PRIMARY KEY ("order_no")
)
select * from t_order_info ;


DROP TABLE IF EXISTS `t_refund_log`;
CREATE TABLE "t_refund_log" (
                                "order_no" varchar(255) NOT NULL,
                                "refund_time" datetime DEFAULT NULL,
                                "refund_amount" varchar(255) DEFAULT NULL,
                                "refund_reason" varchar(255) DEFAULT NULL,
                                "refund_type" tinyint(4) DEFAULT NULL,
                                PRIMARY KEY ("order_no")
) ;

DROP TABLE IF EXISTS `t_pay_log`;
CREATE TABLE "t_pay_log" (
                             "order_no" varchar(255) NOT NULL,
                             "pay_time" datetime DEFAULT NULL,
                             "total_amount" int(11) DEFAULT NULL,
                             "pay_type" varchar(255) NOT NULL,
                             PRIMARY KEY ("order_no")
);


