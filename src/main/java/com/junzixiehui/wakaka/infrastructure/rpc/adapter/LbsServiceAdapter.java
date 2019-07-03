package com.junzixiehui.wakaka.infrastructure.rpc.adapter;

import com.zhouyutong.zapplication.httpclient.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2019/7/3  16:38
 * @version: 1.0
 */
@Component
public class LbsServiceAdapter {


	@Autowired
	HttpClientUtils httpClientUtils;

	public String getCityIdByLocation(String lng,String lat){
		final String result = httpClientUtils.httpCallGet("");
		return result;
	}
}
