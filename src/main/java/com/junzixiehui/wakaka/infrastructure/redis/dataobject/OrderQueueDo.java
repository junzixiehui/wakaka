package com.junzixiehui.wakaka.infrastructure.redis.dataobject;

import lombok.Data;

/**
 * <p>Description: </p>
 * @author: by zt
 * @date: 2Da9/7/2  14:47
 * @version: 1.0
 */
@Data
public class OrderQueueDo {

	private String orderId;
	private String phone;
}
