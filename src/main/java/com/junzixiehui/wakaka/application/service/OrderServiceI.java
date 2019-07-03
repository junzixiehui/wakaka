package com.junzixiehui.wakaka.application.service;

import com.junzixiehui.wakaka.application.dto.req.OrderCreateReq;
import com.junzixiehui.wakaka.application.dto.resp.OrderCreateResp;
import com.zhouyutong.zapplication.api.Req;

/**
 * <p>Description: </p>
 * @author: by zt
 * @date: 2019/7/1  19:13
 * @version: 1.0
 */
public interface OrderServiceI {

	OrderCreateResp createOrder(OrderCreateReq req);

}
