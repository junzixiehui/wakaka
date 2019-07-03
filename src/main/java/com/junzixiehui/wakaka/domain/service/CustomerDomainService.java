package com.junzixiehui.wakaka.domain.service;

import com.junzixiehui.wakaka.domain.customer.CustomerDomain;
import com.junzixiehui.wakaka.infrastructure.dao.CustomerDao;
import com.junzixiehui.wakaka.infrastructure.dao.converter.CustomerConverter;
import com.junzixiehui.wakaka.infrastructure.dao.entity.CustomerDo;
import com.zhouyutong.zapplication.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>Description: 领域服务</p>
 * @author: by qulibin
 * @date: 2019/7/3  17:03
 * @version: 1.0
 */
@Service
public class CustomerDomainService {

	@Autowired
	private CustomerDao customerDao;


	public void checkCustomerByPhone(String phone) {

		final CustomerDo customerDo = customerDao.findByPhone(phone);

		final CustomerDomain customerDomain = CustomerConverter.customerDomain(customerDo);

		if (customerDomain.isBlackList()){
			throw new ServiceException("黑名单不允许下单");
		}
	}
}
