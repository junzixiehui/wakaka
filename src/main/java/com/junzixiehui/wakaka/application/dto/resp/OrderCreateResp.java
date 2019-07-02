package com.junzixiehui.wakaka.application.dto.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class OrderCreateResp {

	@ApiModelProperty(value = "记录总数")
	private String orderId;
}
