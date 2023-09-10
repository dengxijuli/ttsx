# ttsx

## 前言

本项目是个人学习微服务的第一个练手项目，由于本人尚学疏才浅，本项目尚存在还有诸多不足的地方，欢迎各位不吝赐教

## 项目介绍
`tstx mall`项目是一套电商系统，包括前台系统与后台系统,基于微服务实现，采用Docker容器化部署。

### 项目演示
项目演示地址：尚在部署.....

## 系统架构图

![image](https://github.com/dengxijuli/ttsx/assets/132116099/a3e533fb-9ea5-46e2-bb53-a7b34d018ddc)


### 组织结构
``` lua
ttsx
├──canal-boot -- canal客户端
├── ttsx-FlashKilling -- 秒杀模块
├── ttsx-background -- 后台服务模块
├── ttsx-entity -- 统一实体类管理模块
├── ttsx-feign -- 基于openfeign的远程调用模块
├── ttsx-foods -- 商品模块
├── ttsx-gateway -- 基于Spring Cloud Gateway的微服务API网关服务
├── ttsx-index -- 统一前端静态资源模块
├── ttsx-order -- 订单模块
├── ttsx-pay -- 支付模块
├── ttsx-scheduling -- 分布式任务调度模块
├── ttsx-user -- 用户模块
└── websocket-server -- 实时通信模块
```

## 技术选型

### 后端技术

| 技术                   | 说明                 | 官网                                                 |
| ---------------------- | -------------------- | ---------------------------------------------------- |
| Spring Cloud           | 微服务框架           | https://spring.io/projects/spring-cloud              |
| Spring Cloud Alibaba   | 微服务框架           | https://github.com/alibaba/spring-cloud-alibaba      |
| Spring Boot            | 容器+MVC框架         | https://spring.io/projects/spring-boot               |
| MyBatis-plus           | ORM框架              | http://www.mybatis.org/mybatis-3/zh/index.html       |
| RocketMQ               | 消息队列             | https://rocketmq.apache.org/                         |
| Redis                  | 分布式缓存           | https://redis.io/                                    |
| Docker                 | 应用容器引擎         | https://www.docker.com/                              |
| Druid                  | 数据库连接池         | https://github.com/alibaba/druid                     |
| canal                  | 增量订阅&消费组件    | https://github.com/alibaba/canal                     |
| JWT                    | JWT登录支持          | https://github.com/jwtk/jjwt                         |
| nacos                  | 动态服务发现配置管理管理             | https://nacos.io/zh-cn/index.html    |
| Lombok                 | 简化对象封装工具     | https://github.com/rzwitserloot/lombok               |
| Seata                  | 全局事务管理框架     | https://github.com/seata/seata                       |
| Portainer              | 可视化Docker容器管理 | https://github.com/portainer/portainer               |
| mycat                  | 自动化部署工具       | https://github.com/jenkinsci/jenkins                 |
| websocket              | 即时通信服务         | https://developer.mozilla.org/en-US/docs/Web/API/WebSockets_API                             |

### 前端技术

| 技术       | 说明                  | 官网                           |
| ---------- | --------------------- | ------------------------------ |
| Vue        | 前端框架              | https://vuejs.org/             |
| Element    | 前端UI框架            | https://element.eleme.io/      |
| Axios      | 前端HTTP框架          | https://github.com/axios/axios |
| v-charts   | 基于Echarts的图表框架 | https://v-charts.js.org/       |






