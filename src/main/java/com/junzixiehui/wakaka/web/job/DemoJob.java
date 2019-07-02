package com.junzixiehui.wakaka.web.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.junzixiehui.wakaka.infrastructure.common.config.redis.RedisTemplateWarpper;
import com.zhouyutong.zorm.dao.jdbc.transaction.TransactionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author zhoutao
 * @Description: 基于elastic-job的simpleJob演示
 * @date 2018/4/27
 */
@Component
@Slf4j
public class DemoJob implements SimpleJob {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DemoJob demoDao;

    @Autowired
    private TransactionManager transactionManager;
    @Autowired
    private RedisTemplateWarpper redisTemplateWarpper;

    /**
     * 运行job的业务逻辑
     *
     * @param shardingContext
     */
    @Override
    public void execute(ShardingContext shardingContext) {

    }
}
