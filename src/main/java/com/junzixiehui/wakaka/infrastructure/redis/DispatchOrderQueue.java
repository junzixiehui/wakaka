package com.junzixiehui.wakaka.infrastructure.redis;

import com.junzixiehui.wakaka.infrastructure.common.config.redis.RedisTemplateWarpper;
import com.junzixiehui.wakaka.infrastructure.constant.AppConstant;
import com.junzixiehui.wakaka.infrastructure.redis.dataobject.OrderQueueDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>Description: 派单队列</p>
 * @author: by zt
 * @date: 2019/7/2  14:39
 * @version: 1.0
 */
@Component
public class DispatchOrderQueue {

	@Autowired
	private RedisTemplateWarpper redisTemplateWarpper;

	public long put(OrderQueueDo orderQueueDo) {
		return redisTemplateWarpper.lLeftPush(AppConstant.DISPATCH_QUEUE_NAME, orderQueueDo);
	}

	public OrderQueueDo getFirst() {
		return (OrderQueueDo) redisTemplateWarpper.lLeftPop(AppConstant.DISPATCH_QUEUE_NAME);
	}
}
