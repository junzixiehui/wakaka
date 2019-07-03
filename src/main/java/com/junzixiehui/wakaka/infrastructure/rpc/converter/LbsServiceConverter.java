package com.junzixiehui.wakaka.infrastructure.rpc.converter;

import com.junzixiehui.wakaka.infrastructure.rpc.dataobject.CityPositionDo;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2019/7/3  16:50
 * @version: 1.0
 */
public class LbsServiceConverter {

	public static CityPositionDo tanslatorToCityPositionDo(String result){
		return CityPositionDo.builder().cityId(1).build();
	}

}
