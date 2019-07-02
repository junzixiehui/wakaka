package com.junzixiehui.wakaka.web;


import com.junzixiehui.wakaka.application.dto.req.OrderCreateReq;
import com.junzixiehui.wakaka.application.service.OrderServiceI;
import com.zhouyutong.zapplication.api.Resp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Api(tags = {"1. 订单接口"})
@RestController
@Slf4j
public class OrderController {

	@Autowired
	private OrderServiceI orderServiceI;

	@ApiOperation(value = "用户下单接口", notes = "")
	@PostMapping(path = "/v1/api/order/createOrder")
	public Resp createOrder(@Valid OrderCreateReq orderCreateReq, @ApiIgnore BindingResult bindingResult) {
		return Resp.success(orderServiceI.createOrder(orderCreateReq));
	}

	/*@ApiOperation(value = "查询", notes = "")
	@GetMapping(path = "/v1/api/order/orderList")
	public Resp list(@Valid DemoListReq demoListReq, BindingResult bindingResult) {
		return Resp.success(demoService.list(demoListReq));
	}*/
}
