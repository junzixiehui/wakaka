package com.junzixiehui.wakaka.infrastructure.dao.entity;

import com.zhouyutong.zorm.annotation.PK;
import com.zhouyutong.zorm.dao.jdbc.annotation.Column;
import com.zhouyutong.zorm.dao.jdbc.annotation.Table;
import com.zhouyutong.zorm.entity.IdEntity;
import lombok.Data;

@Table(value = "t_customer")
@Data
public class CustomerDo implements IdEntity {

	@PK
	@Column(value = "id")
	private Long id;
	@Column(value = "user_name")
	private String userName;
	@Column(value = "user_id")
	private String userId;
	@Column(value = "phone")
	private String phone;
	@Column(value = "is_black")
	private Integer isBlack;
	@Column(value = "create_time")
	private Long createTime;
}
