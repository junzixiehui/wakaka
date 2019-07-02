package com.junzixiehui.wakaka.infrastructure.common.config.job;

import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * job配置基础类
 *
 * @author zhoutao
 * @date 2018/5/3
 */
@Configuration
@ConditionalOnProperty(value = "elasticJob.enabled", matchIfMissing = true)
public class BaseJobConfig {
    private static final String PROXY_CLASS_PREFIX = "$";
    @Value("${elasticJob.registryCenter.zkHost}")
    private String zkHost;
    @Value("${elasticJob.registryCenter.namespace}")
    private String namespace;

    @Bean
    public CoordinatorRegistryCenter zookeeperRegistryCenter() {
        ZookeeperConfiguration configuration = new ZookeeperConfiguration(zkHost, namespace);
        CoordinatorRegistryCenter regCenter = new ZookeeperRegistryCenter(configuration);
        regCenter.init();
        return regCenter;
    }

    /**
     * 创建job配置对象
     *
     * @param jobClass
     * @param cron               - 作业运行cron表达式
     * @param shardingTotalCount - 分片总数
     *                           分片数：1 机器数：2，将使用主备模式
     *                           分片数：2 机器数：2，每台机器通过运行时的下发的分片项(0或1)自行映射要处理的数据
     *                           分片数：3 机器数：2，第一台机器的job得到分片项(0,1)，第二台得到分片项(2)
     */
    public LiteJobConfiguration createSimpleJobConfiguration(Class<? extends SimpleJob> jobClass, String cron, int shardingTotalCount) {
        String jobName = jobClass.getSimpleName();
        if (jobName.contains(PROXY_CLASS_PREFIX)) {
            jobName = jobName.substring(0, jobName.indexOf(PROXY_CLASS_PREFIX));
        }
        String jobClassName = jobClass.getCanonicalName();
        if (jobClassName.contains(PROXY_CLASS_PREFIX)) {
            jobClassName = jobClassName.substring(0, jobClassName.indexOf(PROXY_CLASS_PREFIX));
        }
        // 定义作业核心配置
        JobCoreConfiguration.Builder jobCoreConfigBuilder = JobCoreConfiguration.newBuilder(jobName, cron, shardingTotalCount);
        JobCoreConfiguration simpleCoreConfig = jobCoreConfigBuilder.build();
        // 定义SIMPLE类型配置
        SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig, jobClassName);
        // 定义Lite作业根配置
        LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).overwrite(true).build();
        return simpleJobRootConfig;
    }
}
