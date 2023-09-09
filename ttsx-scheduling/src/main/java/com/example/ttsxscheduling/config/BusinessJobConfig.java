package com.example.ttsxscheduling.config;

import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.example.ttsxscheduling.scheduling.SeckillProductCacheJob;
import com.example.ttsxscheduling.util.ElasticJobUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lanxw
 */
@Configuration
public class BusinessJobConfig {

    @Bean(initMethod = "init")
    public SpringJobScheduler initSeckillProductCacheJob(CoordinatorRegistryCenter registryCenter, SeckillProductCacheJob seckillProductCacheJob){
        LiteJobConfiguration jobConfiguration = ElasticJobUtil.createJobConfiguration(
                seckillProductCacheJob.getClass(),
                seckillProductCacheJob.getCron(),
                seckillProductCacheJob.getShardingTotalCount(),
                seckillProductCacheJob.getShardingItemParameters(),false);
        return new SpringJobScheduler(seckillProductCacheJob, registryCenter,jobConfiguration);
    }
}
