package com.junzixiehui.wakaka.domain.order.valueobject;

import lombok.Data;

/**
 * @author: qulibin
 * @description: 路径
 * @date: 19:19 2019/7/3
 * @modify：
 */
@Data
public class OrderCustomer {

	private String customerId;
	private String customerPhone;
	private String customerBackupPhone;//备用手机
	private String customerName;
}
