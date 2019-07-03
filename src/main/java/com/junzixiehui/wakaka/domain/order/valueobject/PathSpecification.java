package com.junzixiehui.wakaka.domain.order.valueobject;

import lombok.Data;

/**
 * @author: qulibin
 * @description: 路径
 * @date: 19:19 2019/7/3
 * @modify：
 */
@Data
public class PathSpecification {

	private String startLng;
	private String startLat;
	private String startLocationName;

	private String endLng;
	private String endLat;
	private String endLocationName;
}
