package com.junzixiehui.wakaka.infrastructure.dao.converter;

import com.junzixiehui.wakaka.domain.order.OrderDomain;
import com.junzixiehui.wakaka.infrastructure.dao.entity.OrderDo;

public class OrderConverter {

	public static OrderDo toOrderDo(OrderDomain domain) {

		OrderDo orderDo = new OrderDo();
		orderDo.setPhone(domain.getPhone());
		orderDo.setCustomerId(domain.getOrderCustomer().getCustomerId());
		return orderDo;
	}
}
