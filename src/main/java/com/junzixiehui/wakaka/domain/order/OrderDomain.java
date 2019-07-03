package com.junzixiehui.wakaka.domain.order;

import com.junzixiehui.wakaka.domain.order.valueobject.OrderCustomer;
import com.junzixiehui.wakaka.domain.order.valueobject.OrderDriver;
import com.junzixiehui.wakaka.domain.order.valueobject.PathSpecification;
import lombok.Data;

import java.util.UUID;

/**
 * <p>Description: </p>
 * @author: by zt
 * @date: 2019/7/1  19:52
 * @version: 1.0
 */
@Data
public class OrderDomain {

	private String phone;
	private String orderId;
	private PathSpecification pathSpecification;
	private OrderCustomer orderCustomer;
	private OrderDriver orderDriver;

	public static OrderDomain genOrder(String phone, PathSpecification pathSpecification) {
		OrderDomain orderDomain = new OrderDomain();
		orderDomain.setOrderId(genOrderId());
		orderDomain.setPhone(phone);
		orderDomain.setPathSpecification(pathSpecification);
		return orderDomain;
	}

	public static String genOrderId() {
		return UUID.randomUUID().toString();
	}
}
