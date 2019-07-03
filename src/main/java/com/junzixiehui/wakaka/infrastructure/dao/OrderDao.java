package com.junzixiehui.wakaka.infrastructure.dao;

import com.junzixiehui.wakaka.domain.order.OrderDomain;
import com.junzixiehui.wakaka.infrastructure.dao.converter.CustomerConverter;
import com.junzixiehui.wakaka.infrastructure.dao.converter.OrderConverter;
import com.junzixiehui.wakaka.infrastructure.dao.entity.CustomerDo;
import com.junzixiehui.wakaka.infrastructure.dao.entity.OrderDo;
import com.zhouyutong.zorm.annotation.Dao;
import com.zhouyutong.zorm.dao.jdbc.JdbcBaseDao;
import org.springframework.stereotype.Repository;

@Dao(settingBeanName = "")
@Repository
public class OrderDao extends JdbcBaseDao<OrderDo> {

	public void save(OrderDomain orderDomain) {
		OrderDo orderDo = OrderConverter.toOrderDo(orderDomain);
		this.insert(orderDo);
	}
}
