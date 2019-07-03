package com.junzixiehui.wakaka.infrastructure.dao;

import com.junzixiehui.wakaka.infrastructure.dao.entity.CustomerDo;
import com.zhouyutong.zorm.annotation.Dao;
import com.zhouyutong.zorm.dao.jdbc.JdbcBaseDao;
import org.springframework.stereotype.Repository;


@Dao(settingBeanName = "")
@Repository
public class CustomerDao extends JdbcBaseDao<CustomerDo> {

	public CustomerDo findByUserId(String userId){
		CustomerDo customerDo = new CustomerDo();
		customerDo.setPhone("1861246347*");
		customerDo.setUserId("ohggyftfty89767566gyg5656");
		return new CustomerDo();
	}

	public CustomerDo findByPhone(String phone){
		CustomerDo customerDo = new CustomerDo();
		customerDo.setPhone("1861246347*");
		customerDo.setUserId("ohggyftfty89767566gyg5656");
		return new CustomerDo();
	}

}
