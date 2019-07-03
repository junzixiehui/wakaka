package com.junzixiehui.wakaka.application.event;

import lombok.Data;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2019/7/3  19:37
 * @version: 1.0
 */
@Data
public class OrderCreateEvent {

	private String orderId;
	private String phone;

}
