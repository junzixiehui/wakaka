package com.junzixiehui.wakaka.infrastructure.rpc;

import com.junzixiehui.wakaka.infrastructure.rpc.adapter.LbsServiceAdapter;
import com.junzixiehui.wakaka.infrastructure.rpc.converter.LbsServiceConverter;
import com.junzixiehui.wakaka.infrastructure.rpc.dataobject.CityPositionDo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2019/7/3  16:35
 * @version: 1.0
 */
@Service
@Slf4j
public class LbsService {

	@Autowired
	private LbsServiceAdapter lbsServiceAdapter;

	public CityPositionDo getCityIdByLocation(String lng,String lat){
		final String cityIdByLoction = lbsServiceAdapter.getCityIdByLocation(lng, lat);
		return LbsServiceConverter.tanslatorToCityPositionDo(cityIdByLoction);
	}
}
