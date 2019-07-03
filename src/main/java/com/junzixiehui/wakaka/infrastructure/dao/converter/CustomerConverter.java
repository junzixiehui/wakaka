package com.junzixiehui.wakaka.infrastructure.dao.converter;

import com.junzixiehui.wakaka.domain.customer.CustomerDomain;
import com.junzixiehui.wakaka.infrastructure.dao.entity.CustomerDo;


public class CustomerConverter {

	public static CustomerDomain customerDomain(CustomerDo customerDo) {

		CustomerDomain customerDomain = new CustomerDomain();
		customerDomain.setUserId(customerDo.getUserId());
		customerDomain.setIsBlack(customerDo.getIsBlack());
		customerDomain.setUserId(customerDo.getUserId());
		return customerDomain;
	}
}
