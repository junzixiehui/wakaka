package com.junzixiehui.wakaka.domain.order.valueobject;

import lombok.Data;

/**
 * @author: qulibin
 * @description:
 * @date: 19:19 2019/7/3
 * @modify：
 */
@Data
public class OrderDriver {

	private String driverId;
	private String driverPhone;
	private String driverBackupPhone;//备用手机
	private String driverName;
}
