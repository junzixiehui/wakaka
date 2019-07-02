package com.junzixiehui.wakaka.infrastructure.common.config.job;

/**
 * @author zhoutao
 * @date 2018/10/12
 */

import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.junzixiehui.wakaka.web.job.DemoJob;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Getter
@Setter
@Configuration
@AutoConfigureAfter(BaseJobConfig.class)
@ConditionalOnProperty(value = "elasticJob.demoJobConfig.enabled", matchIfMissing = true)
@ConfigurationProperties(prefix = "elasticJob.demoJobConfig")
public class DemoJobConfig extends BaseJobConfig {
    private String cron;
    private int shardingTotalCount;

    @Bean(name = "demoJobScheduler", initMethod = "init")
    public JobScheduler jobScheduler(DemoJob demoJob, CoordinatorRegistryCenter registryCenter) {
        JobScheduler jobScheduler = new SpringJobScheduler(demoJob, registryCenter,
                super.createSimpleJobConfiguration(demoJob.getClass(), cron, shardingTotalCount));
        return jobScheduler;
    }
}
