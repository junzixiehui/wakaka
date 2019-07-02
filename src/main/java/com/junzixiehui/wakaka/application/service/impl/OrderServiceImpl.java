package com.junzixiehui.wakaka.application.service.impl;

import com.junzixiehui.wakaka.application.dto.resp.OrderCreateResp;
import com.junzixiehui.wakaka.application.service.OrderServiceI;
import com.junzixiehui.wakaka.application.dto.resp.OrderCreateResp;
import com.zhouyutong.zapplication.api.Req;

/**
 * <p>Description: </p>
 * @author: by zt
 * @date: 2019/7/1  19:14
 * @version: 1.0
 */
public class OrderServiceImpl implements OrderServiceI {

	//不要用用户手机版本和类型做逻辑判断
	@Override
	public OrderCreateResp createOrder(Req req) {

		/**
		 * 1. 验证参数
		 */

		/**
		 * 2. 验证用户
		 */

		/**
		 * 3. 获取下单城市
		 */

		/**
		 * 4. 验证下单渠道-不同渠道不同策略
		 */

		/**
		 * 5. 生成订单预约id
		 */


		/**
		 * 6. 组装参数-加入派单队列
		 */

        /**
		 * 7. 放入kafka
		 */

		return null;
	}
}
