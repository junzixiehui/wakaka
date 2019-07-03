package com.junzixiehui.wakaka.infrastructure.dao.entity;

import com.zhouyutong.zorm.annotation.PK;
import com.zhouyutong.zorm.dao.jdbc.annotation.Column;
import com.zhouyutong.zorm.dao.jdbc.annotation.Table;
import com.zhouyutong.zorm.entity.IdEntity;
import lombok.Data;

@Table(value = "t_order")
@Data
public class OrderDo implements IdEntity {

	@PK
	@Column(value = "id")
	private Long id;
	@Column(value = "order_id")
	private String orderId;
	@Column(value = "customer_id")
	private String customerId;
	@Column(value = "phone")
	private String phone;
	private Long createTime;
}
