package com.junzixiehui.wakaka.application.dto;

import lombok.Data;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2019/7/3  19:08
 * @version: 1.0
 */
@Data
public class OrderQueue {

	private String orderId;
	private String phone;
	private String lng;
	private String lat;
	private String userId;


	private String driverPhone;
	private String driverId;
}
