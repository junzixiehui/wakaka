package com.junzixiehui.wakaka.domain.customer;

import lombok.Data;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2019/7/3  18:12
 * @version: 1.0
 */
@Data
public class CustomerDomain {

	private String userId;
	private String phone;
	private int cityId;
	private int isBlack;


	public boolean isBlackList(){
		return isBlack == 1;
	}
}
