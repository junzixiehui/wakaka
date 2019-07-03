package com.junzixiehui.wakaka.application.service.impl;

import com.google.common.eventbus.EventBus;
import com.junzixiehui.wakaka.application.dto.req.OrderCreateReq;
import com.junzixiehui.wakaka.application.dto.resp.OrderCreateResp;
import com.junzixiehui.wakaka.application.event.OrderCreateEvent;
import com.junzixiehui.wakaka.application.service.OrderServiceI;
import com.junzixiehui.wakaka.domain.order.OrderDomain;
import com.junzixiehui.wakaka.domain.service.CustomerDomainService;
import com.junzixiehui.wakaka.infrastructure.dao.OrderDao;
import com.junzixiehui.wakaka.infrastructure.rpc.LbsService;
import com.junzixiehui.wakaka.infrastructure.rpc.dataobject.CityPositionDo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>Description:
 *
 *  用户下单抽象模板
 *
 * </p>
 * @author: by zt
 * @date: 2019/7/1  19:14
 * @version: 1.0
 */
public abstract class AbstractOrderServiceImpl implements OrderServiceI {

	@Autowired
	private LbsService lbsService;
	@Autowired
	private CustomerDomainService customerDomainService;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private EventBus eventBus;

	//不要用用户手机版本和类型做逻辑判断
	@Override
	public OrderCreateResp createOrder(OrderCreateReq req) {

		/**
		 * 1. 验证参数
		 */
		validateParam(req);

		/**
		 * 2. 验证用户
		 */
		customerDomainService.checkCustomerByPhone(req.getPhone());

		/**
		 * 3. 获取下单城市
		 */
		final CityPositionDo cityIdByLocation = lbsService.getCityIdByLocation(req.getLng(), req.getLat());
		int cityId = cityIdByLocation.getCityId();

		/**
		 * 4. 验证下单渠道-不同渠道不同策略
		 */




		/**
		 * 5. 组装参数-加入派单队列
		 */
		final OrderDomain orderDomain = OrderDomain.genOrder(req.getPhone(), null);
		orderDao.save(orderDomain);
		/**
		 * 6. 放入kafka
		 */
		OrderCreateEvent orderCreateEvent = new OrderCreateEvent();
		orderCreateEvent.setOrderId(orderDomain.getOrderId());
		orderCreateEvent.setPhone(orderDomain.getPhone());
		eventBus.post(orderCreateEvent);

		return null;
	}

	private void validateParam(OrderCreateReq req) {

	}
}
